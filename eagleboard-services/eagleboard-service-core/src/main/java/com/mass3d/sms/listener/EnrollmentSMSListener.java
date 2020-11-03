package com.mass3d.sms.listener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import com.mass3d.category.CategoryOptionCombo;
import com.mass3d.category.CategoryService;
import com.mass3d.common.IdentifiableObjectManager;
import com.mass3d.dataelement.DataElementService;
import com.mass3d.message.MessageSender;
import com.mass3d.organisationunit.OrganisationUnit;
import com.mass3d.organisationunit.OrganisationUnitService;
import com.mass3d.program.Program;
import com.mass3d.program.ProgramInstance;
import com.mass3d.program.ProgramInstanceService;
import com.mass3d.program.ProgramService;
import com.mass3d.program.ProgramStage;
import com.mass3d.program.ProgramStageInstanceService;
import com.mass3d.program.ProgramStageService;
import com.mass3d.sms.incoming.IncomingSms;
import com.mass3d.sms.incoming.IncomingSmsService;
import org.hisp.dhis.smscompression.SmsConsts.SubmissionType;
import org.hisp.dhis.smscompression.SmsResponse;
import org.hisp.dhis.smscompression.models.EnrollmentSmsSubmission;
import org.hisp.dhis.smscompression.models.SmsAttributeValue;
import org.hisp.dhis.smscompression.models.SmsEvent;
import org.hisp.dhis.smscompression.models.SmsSubmission;
import org.hisp.dhis.smscompression.models.Uid;
import com.mass3d.trackedentity.TrackedEntityAttribute;
import com.mass3d.trackedentity.TrackedEntityAttributeService;
import com.mass3d.trackedentity.TrackedEntityInstance;
import com.mass3d.trackedentity.TrackedEntityInstanceService;
import com.mass3d.trackedentity.TrackedEntityType;
import com.mass3d.trackedentity.TrackedEntityTypeService;
import com.mass3d.trackedentityattributevalue.TrackedEntityAttributeValue;
import com.mass3d.trackedentityattributevalue.TrackedEntityAttributeValueService;
import com.mass3d.user.User;
import com.mass3d.user.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component( "com.mass3d.sms.listener.EnrollmentSMSListener" )
@Transactional
public class EnrollmentSMSListener
    extends
    CompressionSMSListener
{
    private final TrackedEntityInstanceService teiService;

    private final ProgramInstanceService programInstanceService;

    private final TrackedEntityAttributeValueService attributeValueService;

    private final ProgramStageService programStageService;

    private final UserService userService;

    public EnrollmentSMSListener( IncomingSmsService incomingSmsService,
        @Qualifier( "smsMessageSender" ) MessageSender smsSender, UserService userService,
        TrackedEntityTypeService trackedEntityTypeService, TrackedEntityAttributeService trackedEntityAttributeService,
        ProgramService programService, OrganisationUnitService organisationUnitService, CategoryService categoryService,
        DataElementService dataElementService, ProgramStageService programStageService,
        ProgramStageInstanceService programStageInstanceService,
        TrackedEntityAttributeValueService attributeValueService, TrackedEntityInstanceService teiService,
        ProgramInstanceService programInstanceService, IdentifiableObjectManager identifiableObjectManager )
    {
        super( incomingSmsService, smsSender, userService, trackedEntityTypeService, trackedEntityAttributeService,
            programService, organisationUnitService, categoryService, dataElementService, programStageInstanceService,
            identifiableObjectManager );

        this.teiService = teiService;
        this.programStageService = programStageService;
        this.programInstanceService = programInstanceService;
        this.attributeValueService = attributeValueService;
        this.userService = userService;
    }

    @Override
    protected SmsResponse postProcess( IncomingSms sms, SmsSubmission submission )
        throws SMSProcessingException
    {
        EnrollmentSmsSubmission subm = (EnrollmentSmsSubmission) submission;

        Date enrollmentDate = subm.getEnrollmentDate();
        Date incidentDate = subm.getIncidentDate();
        Uid teiUid = subm.getTrackedEntityInstance();
        Uid progid = subm.getTrackerProgram();
        Uid tetid = subm.getTrackedEntityType();
        Uid ouid = subm.getOrgUnit();
        Uid enrollmentid = subm.getEnrollment();
        OrganisationUnit orgUnit = organisationUnitService.getOrganisationUnit( ouid.getUid() );

        Program program = programService.getProgram( progid.getUid() );

        if ( program == null )
        {
            throw new SMSProcessingException( SmsResponse.INVALID_PROGRAM.set( progid ) );
        }

        TrackedEntityType entityType = trackedEntityTypeService.getTrackedEntityType( tetid.getUid() );

        if ( entityType == null )
        {
            throw new SMSProcessingException( SmsResponse.INVALID_TETYPE.set( tetid ) );
        }

        if ( !program.hasOrganisationUnit( orgUnit ) )
        {
            throw new SMSProcessingException( SmsResponse.OU_NOTIN_PROGRAM.set( ouid, progid ) );
        }

        TrackedEntityInstance entityInstance;
        boolean teiExists = teiService.trackedEntityInstanceExists( teiUid.getUid() );

        if ( teiExists )
        {
            log.info( String.format( "Given TEI [%s] exists. Updating...", teiUid ) );
            entityInstance = teiService.getTrackedEntityInstance( teiUid.getUid() );
        }
        else
        {
            log.info( String.format( "Given TEI [%s] does not exist. Creating...", teiUid ) );
            entityInstance = new TrackedEntityInstance();
            entityInstance.setUid( teiUid.getUid() );
            entityInstance.setOrganisationUnit( orgUnit );
            entityInstance.setTrackedEntityType( entityType );
        }

        Set<TrackedEntityAttributeValue> attributeValues = getSMSAttributeValues( subm, entityInstance );

        if ( teiExists )
        {
            updateAttributeValues( attributeValues, entityInstance.getTrackedEntityAttributeValues() );
            entityInstance.setTrackedEntityAttributeValues( attributeValues );
            teiService.updateTrackedEntityInstance( entityInstance );
        }
        else
        {
            teiService.createTrackedEntityInstance( entityInstance, attributeValues );
        }

        TrackedEntityInstance tei = teiService.getTrackedEntityInstance( teiUid.getUid() );

        // TODO: Unsure about this handling for enrollments, this needs to be
        // checked closely
        ProgramInstance enrollment;
        boolean enrollmentExists = programInstanceService.programInstanceExists( enrollmentid.getUid() );
        if ( enrollmentExists )
        {
            enrollment = programInstanceService.getProgramInstance( enrollmentid.getUid() );
            // Update these dates in case they've changed
            enrollment.setEnrollmentDate( enrollmentDate );
            enrollment.setIncidentDate( incidentDate );
        }
        else
        {
            enrollment = programInstanceService.enrollTrackedEntityInstance( tei, program, enrollmentDate, incidentDate,
                orgUnit, enrollmentid.getUid() );
        }
        if ( enrollment == null )
        {
            throw new SMSProcessingException( SmsResponse.ENROLL_FAILED.set( teiUid, progid ) );
        }
        enrollment.setStatus( getCoreProgramStatus( subm.getEnrollmentStatus() ) );
        enrollment.setGeometry( convertGeoPointToGeometry( subm.getCoordinates() ) );
        programInstanceService.updateProgramInstance( enrollment );

        // We now check if the enrollment has events to process
        User user = userService.getUser( subm.getUserId().getUid() );
        List<Object> errorUIDs = new ArrayList<>();
        if ( subm.getEvents() != null )
        {
            for ( SmsEvent event : subm.getEvents() )
            {
                errorUIDs.addAll( processEvent( event, user, enrollment, sms ) );
            }
        }
        enrollment.setStatus( getCoreProgramStatus( subm.getEnrollmentStatus() ) );
        enrollment.setGeometry( convertGeoPointToGeometry( subm.getCoordinates() ) );
        programInstanceService.updateProgramInstance( enrollment );

        if ( !errorUIDs.isEmpty() )
        {
            return SmsResponse.WARN_DVERR.setList( errorUIDs );
        }

        if ( attributeValues == null || attributeValues.isEmpty() )
        {
            // TODO: Is this correct handling?
            return SmsResponse.WARN_AVEMPTY;
        }

        return SmsResponse.SUCCESS;
    }


    private TrackedEntityAttributeValue findAttributeValue( TrackedEntityAttributeValue attributeValue,
        Set<TrackedEntityAttributeValue> attributeValues )
    {
        return attributeValues.stream()
            .filter( v -> v.getAttribute().getUid().equals( attributeValue.getAttribute().getUid() ) ).findAny()
            .orElse( null );
    }

    private void updateAttributeValues( Set<TrackedEntityAttributeValue> attributeValues,
        Set<TrackedEntityAttributeValue> oldAttributeValues )
    {
        // Update existing and add new values
        for ( TrackedEntityAttributeValue attributeValue : attributeValues )
        {
            TrackedEntityAttributeValue oldAttributeValue = findAttributeValue( attributeValue, oldAttributeValues );
            if ( oldAttributeValue != null )
            {
                oldAttributeValue.setValue( attributeValue.getValue() );
                attributeValueService.updateTrackedEntityAttributeValue( oldAttributeValue );
            }
            else
            {
                attributeValueService.addTrackedEntityAttributeValue( attributeValue );
            }
        }

        // Delete any that don't exist anymore
        for ( TrackedEntityAttributeValue oldAttributeValue : oldAttributeValues )
        {
            if ( findAttributeValue( oldAttributeValue, attributeValues ) == null )
            {
                attributeValueService.deleteTrackedEntityAttributeValue( oldAttributeValue );
            }
        }
    }

    @Override
    protected boolean handlesType( SubmissionType type )
    {
        return (type == SubmissionType.ENROLLMENT);
    }

    private Set<TrackedEntityAttributeValue> getSMSAttributeValues( EnrollmentSmsSubmission submission,
        TrackedEntityInstance entityInstance )
    {
        if ( submission.getValues() == null )
        {
            return null;
        }
        return submission.getValues().stream().map( v -> createTrackedEntityValue( v, entityInstance ) )
            .collect( Collectors.toSet() );
    }

    protected TrackedEntityAttributeValue createTrackedEntityValue( SmsAttributeValue SMSAttributeValue,
        TrackedEntityInstance tei )
    {
        Uid attribUid = SMSAttributeValue.getAttribute();
        String val = SMSAttributeValue.getValue();

        TrackedEntityAttribute attribute = trackedEntityAttributeService
            .getTrackedEntityAttribute( attribUid.getUid() );
            
        if ( attribute == null )
        {
            throw new SMSProcessingException( SmsResponse.INVALID_ATTRIB.set( attribUid ) );
        }
        else if ( val == null )
        {
            // TODO: Is this an error we can't recover from?
            throw new SMSProcessingException( SmsResponse.NULL_ATTRIBVAL.set( attribUid ) );
        }
        TrackedEntityAttributeValue trackedEntityAttributeValue = new TrackedEntityAttributeValue();
        trackedEntityAttributeValue.setAttribute( attribute );
        trackedEntityAttributeValue.setEntityInstance( tei );
        trackedEntityAttributeValue.setValue( val );
        return trackedEntityAttributeValue;
    }

    protected List<Object> processEvent( SmsEvent event, User user, ProgramInstance programInstance, IncomingSms sms )
    {
        Uid stageid = event.getProgramStage();
        Uid aocid = event.getAttributeOptionCombo();
        Uid orgunitid = event.getOrgUnit();

        OrganisationUnit orgUnit = organisationUnitService.getOrganisationUnit( orgunitid.getUid() );
        if ( orgUnit == null )
        {
            throw new SMSProcessingException( SmsResponse.INVALID_ORGUNIT.set( orgunitid ) );
        }

        ProgramStage programStage = programStageService.getProgramStage( stageid.getUid() );
        if ( programStage == null )
        {
            throw new SMSProcessingException( SmsResponse.INVALID_STAGE.set( stageid ) );
        }

        CategoryOptionCombo aoc = categoryService.getCategoryOptionCombo( aocid.getUid() );
        if ( aoc == null )
        {
            throw new SMSProcessingException( SmsResponse.INVALID_AOC.set( aocid ) );
        }

        List<Object> errorUIDs = saveNewEvent( event.getEvent().getUid(), orgUnit, programStage, programInstance, sms,
            aoc, user, event.getValues(), event.getEventStatus(), event.getEventDate(), event.getDueDate(),
            event.getCoordinates() );

        return errorUIDs;
    }
}