package com.mass3d.sms.listener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
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
import com.mass3d.program.ProgramStatus;
import com.mass3d.sms.incoming.IncomingSms;
import com.mass3d.sms.incoming.IncomingSmsService;
import org.hisp.dhis.smscompression.SmsConsts.SubmissionType;
import org.hisp.dhis.smscompression.SmsResponse;
import org.hisp.dhis.smscompression.models.SimpleEventSmsSubmission;
import org.hisp.dhis.smscompression.models.SmsSubmission;
import org.hisp.dhis.smscompression.models.Uid;
import com.mass3d.trackedentity.TrackedEntityAttributeService;
import com.mass3d.trackedentity.TrackedEntityTypeService;
import com.mass3d.user.User;
import com.mass3d.user.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component( "org.hisp.dhis.sms.listener.SimpleEventSMSListener" )
@Transactional
public class SimpleEventSMSListener
    extends
    CompressionSMSListener
{
    private final ProgramInstanceService programInstanceService;

    public SimpleEventSMSListener( IncomingSmsService incomingSmsService,
        @Qualifier( "smsMessageSender" ) MessageSender smsSender, UserService userService,
        TrackedEntityTypeService trackedEntityTypeService, TrackedEntityAttributeService trackedEntityAttributeService,
        ProgramService programService, OrganisationUnitService organisationUnitService, CategoryService categoryService,
        DataElementService dataElementService, ProgramStageInstanceService programStageInstanceService,
        ProgramInstanceService programInstanceService, IdentifiableObjectManager identifiableObjectManager )
    {
        super( incomingSmsService, smsSender, userService, trackedEntityTypeService, trackedEntityAttributeService,
            programService, organisationUnitService, categoryService, dataElementService, programStageInstanceService,
            identifiableObjectManager );

        this.programInstanceService = programInstanceService;
    }

    @Override
    protected SmsResponse postProcess( IncomingSms sms, SmsSubmission submission )
        throws SMSProcessingException
    {
        SimpleEventSmsSubmission subm = (SimpleEventSmsSubmission) submission;

        Uid ouid = subm.getOrgUnit();
        Uid aocid = subm.getAttributeOptionCombo();
        Uid progid = subm.getEventProgram();

        OrganisationUnit orgUnit = organisationUnitService.getOrganisationUnit( ouid.getUid() );
        User user = userService.getUser( subm.getUserId().getUid() );

        Program program = programService.getProgram( subm.getEventProgram().getUid() );

        if ( program == null )
        {
            throw new SMSProcessingException( SmsResponse.INVALID_PROGRAM.set( progid ) );
        }

        CategoryOptionCombo aoc = categoryService.getCategoryOptionCombo( aocid.getUid() );

        if ( aoc == null )
        {
            throw new SMSProcessingException( SmsResponse.INVALID_AOC.set( aocid ) );
        }

        if ( !program.hasOrganisationUnit( orgUnit ) )
        {
            throw new SMSProcessingException( SmsResponse.OU_NOTIN_PROGRAM.set( ouid, progid ) );
        }

        List<ProgramInstance> programInstances = new ArrayList<>(
            programInstanceService.getProgramInstances( program, ProgramStatus.ACTIVE ) );

        // For Simple Events, the Program should have one Program Instance
        // If it doesn't exist, this is the first event, we can create it here
        if ( programInstances.isEmpty() )
        {
            ProgramInstance pi = new ProgramInstance();
            pi.setEnrollmentDate( new Date() );
            pi.setIncidentDate( new Date() );
            pi.setProgram( program );
            pi.setStatus( ProgramStatus.ACTIVE );

            programInstanceService.addProgramInstance( pi );

            programInstances.add( pi );
        }
        else if ( programInstances.size() > 1 )
        {
            // TODO: Are we sure this is a problem we can't recover from?
            throw new SMSProcessingException( SmsResponse.MULTI_PROGRAMS.set( progid ) );
        }

        ProgramInstance programInstance = programInstances.get( 0 );
        Set<ProgramStage> programStages = programInstance.getProgram().getProgramStages();
        if ( programStages.size() > 1 )
        {
            throw new SMSProcessingException( SmsResponse.MULTI_STAGES.set( progid ) );
        }
        ProgramStage programStage = programStages.iterator().next();

        List<Object> errorUIDs = saveNewEvent( subm.getEvent().getUid(), orgUnit, programStage, programInstance, sms,
            aoc, user, subm.getValues(), subm.getEventStatus(), subm.getEventDate(), subm.getDueDate(),
            subm.getCoordinates() );
        if ( !errorUIDs.isEmpty() )
        {
            return SmsResponse.WARN_DVERR.setList( errorUIDs );
        }
        else if ( subm.getValues() == null || subm.getValues().isEmpty() )
        {
            // TODO: Should we save the event if there are no data values?
            return SmsResponse.WARN_DVEMPTY;
        }

        return SmsResponse.SUCCESS;
    }

    @Override
    protected boolean handlesType( SubmissionType type )
    {
        return (type == SubmissionType.SIMPLE_EVENT);
    }

}