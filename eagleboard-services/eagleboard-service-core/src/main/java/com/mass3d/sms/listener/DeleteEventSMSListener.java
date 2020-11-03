package com.mass3d.sms.listener;

import com.mass3d.category.CategoryService;
import com.mass3d.common.IdentifiableObjectManager;
import com.mass3d.dataelement.DataElementService;
import com.mass3d.message.MessageSender;
import com.mass3d.organisationunit.OrganisationUnitService;
import com.mass3d.program.ProgramService;
import com.mass3d.program.ProgramStageInstance;
import com.mass3d.program.ProgramStageInstanceService;
import com.mass3d.sms.incoming.IncomingSms;
import com.mass3d.sms.incoming.IncomingSmsService;
import org.hisp.dhis.smscompression.SmsConsts.SubmissionType;
import org.hisp.dhis.smscompression.SmsResponse;
import org.hisp.dhis.smscompression.models.DeleteSmsSubmission;
import org.hisp.dhis.smscompression.models.SmsSubmission;
import org.hisp.dhis.smscompression.models.Uid;
import com.mass3d.trackedentity.TrackedEntityAttributeService;
import com.mass3d.trackedentity.TrackedEntityTypeService;
import com.mass3d.user.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component( "com.mass3d.sms.listener.DeleteEventSMSListener" )
@Transactional
public class DeleteEventSMSListener
    extends
    CompressionSMSListener
{
    public DeleteEventSMSListener( IncomingSmsService incomingSmsService,
        @Qualifier( "smsMessageSender" ) MessageSender smsSender, UserService userService,
        TrackedEntityTypeService trackedEntityTypeService, TrackedEntityAttributeService trackedEntityAttributeService,
        ProgramService programService, OrganisationUnitService organisationUnitService, CategoryService categoryService,
        DataElementService dataElementService, ProgramStageInstanceService programStageInstanceService,
        IdentifiableObjectManager identifiableObjectManager )
    {
        super( incomingSmsService, smsSender, userService, trackedEntityTypeService, trackedEntityAttributeService,
            programService, organisationUnitService, categoryService, dataElementService, programStageInstanceService,
            identifiableObjectManager );
    }

    @Override
    protected SmsResponse postProcess( IncomingSms sms, SmsSubmission submission )
        throws SMSProcessingException
    {
        DeleteSmsSubmission subm = (DeleteSmsSubmission) submission;

        Uid eventid = subm.getEvent();
        ProgramStageInstance psi = programStageInstanceService.getProgramStageInstance( eventid.getUid() );

        if ( psi == null )
        {
            throw new SMSProcessingException( SmsResponse.INVALID_EVENT.set( eventid ) );
        }

        programStageInstanceService.deleteProgramStageInstance( psi );

        return SmsResponse.SUCCESS;
    }

    @Override
    protected boolean handlesType( SubmissionType type )
    {
        return (type == SubmissionType.DELETE);
    }

}