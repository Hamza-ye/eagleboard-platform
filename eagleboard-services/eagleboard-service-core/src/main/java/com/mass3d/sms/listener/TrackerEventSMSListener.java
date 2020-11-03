package com.mass3d.sms.listener;

import java.util.List;
import com.mass3d.category.CategoryOptionCombo;
import com.mass3d.category.CategoryService;
import com.mass3d.common.IdentifiableObjectManager;
import com.mass3d.dataelement.DataElementService;
import com.mass3d.message.MessageSender;
import com.mass3d.organisationunit.OrganisationUnit;
import com.mass3d.organisationunit.OrganisationUnitService;
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
import org.hisp.dhis.smscompression.models.SmsSubmission;
import org.hisp.dhis.smscompression.models.TrackerEventSmsSubmission;
import org.hisp.dhis.smscompression.models.Uid;
import com.mass3d.trackedentity.TrackedEntityAttributeService;
import com.mass3d.trackedentity.TrackedEntityTypeService;
import com.mass3d.user.User;
import com.mass3d.user.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component( "com.mass3d.sms.listener.TrackerEventSMSListener" )
@Transactional
public class TrackerEventSMSListener
    extends
    CompressionSMSListener
{

    private final ProgramStageService programStageService;

    private final ProgramInstanceService programInstanceService;

    public TrackerEventSMSListener( IncomingSmsService incomingSmsService,
        @Qualifier( "smsMessageSender" ) MessageSender smsSender, UserService userService,
        TrackedEntityTypeService trackedEntityTypeService, TrackedEntityAttributeService trackedEntityAttributeService,
        ProgramService programService, OrganisationUnitService organisationUnitService, CategoryService categoryService,
        DataElementService dataElementService, ProgramStageInstanceService programStageInstanceService,
        ProgramStageService programStageService, ProgramInstanceService programInstanceService,
        IdentifiableObjectManager identifiableObjectManager )
    {
        super( incomingSmsService, smsSender, userService, trackedEntityTypeService, trackedEntityAttributeService,
            programService, organisationUnitService, categoryService, dataElementService, programStageInstanceService,
            identifiableObjectManager );

        this.programStageService = programStageService;
        this.programInstanceService = programInstanceService;
    }

    @Override
    protected SmsResponse postProcess( IncomingSms sms, SmsSubmission submission )
        throws SMSProcessingException
    {
        TrackerEventSmsSubmission subm = (TrackerEventSmsSubmission) submission;

        Uid ouid = subm.getOrgUnit();
        Uid stageid = subm.getProgramStage();
        Uid enrolmentid = subm.getEnrollment();
        Uid aocid = subm.getAttributeOptionCombo();

        OrganisationUnit orgUnit = organisationUnitService.getOrganisationUnit( ouid.getUid() );
        User user = userService.getUser( subm.getUserId().getUid() );

        ProgramInstance programInstance = programInstanceService.getProgramInstance( enrolmentid.getUid() );

        if ( programInstance == null )
        {
            throw new SMSProcessingException( SmsResponse.INVALID_ENROLL.set( enrolmentid ) );
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
        return (type == SubmissionType.TRACKER_EVENT);
    }

}