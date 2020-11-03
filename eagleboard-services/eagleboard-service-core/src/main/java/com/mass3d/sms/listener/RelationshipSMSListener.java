package com.mass3d.sms.listener;

import java.util.Date;
import com.mass3d.category.CategoryService;
import com.mass3d.common.IdentifiableObjectManager;
import com.mass3d.dataelement.DataElementService;
import com.mass3d.message.MessageSender;
import com.mass3d.organisationunit.OrganisationUnitService;
import com.mass3d.program.ProgramInstance;
import com.mass3d.program.ProgramInstanceService;
import com.mass3d.program.ProgramService;
import com.mass3d.program.ProgramStageInstance;
import com.mass3d.program.ProgramStageInstanceService;
import com.mass3d.relationship.Relationship;
import com.mass3d.relationship.RelationshipEntity;
import com.mass3d.relationship.RelationshipItem;
import com.mass3d.relationship.RelationshipService;
import com.mass3d.relationship.RelationshipType;
import com.mass3d.relationship.RelationshipTypeService;
import com.mass3d.sms.incoming.IncomingSms;
import com.mass3d.sms.incoming.IncomingSmsService;
import org.hisp.dhis.smscompression.SmsConsts.SubmissionType;
import org.hisp.dhis.smscompression.SmsResponse;
import org.hisp.dhis.smscompression.models.RelationshipSmsSubmission;
import org.hisp.dhis.smscompression.models.SmsSubmission;
import org.hisp.dhis.smscompression.models.Uid;
import com.mass3d.trackedentity.TrackedEntityAttributeService;
import com.mass3d.trackedentity.TrackedEntityInstance;
import com.mass3d.trackedentity.TrackedEntityInstanceService;
import com.mass3d.trackedentity.TrackedEntityTypeService;
import com.mass3d.user.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component( "com.mass3d.sms.listener.RelationshipSMSListener" )
@Transactional
public class RelationshipSMSListener
    extends
    CompressionSMSListener
{
    private enum RelationshipDir
    {
        FROM, TO;
    }

    private final RelationshipService relationshipService;

    private final RelationshipTypeService relationshipTypeService;

    private final TrackedEntityInstanceService trackedEntityInstanceService;

    private final ProgramInstanceService programInstanceService;

    public RelationshipSMSListener( IncomingSmsService incomingSmsService,
        @Qualifier( "smsMessageSender" ) MessageSender smsSender, UserService userService,
        TrackedEntityTypeService trackedEntityTypeService, TrackedEntityAttributeService trackedEntityAttributeService,
        ProgramService programService, OrganisationUnitService organisationUnitService, CategoryService categoryService,
        DataElementService dataElementService, ProgramStageInstanceService programStageInstanceService,
        RelationshipService relationshipService, RelationshipTypeService relationshipTypeService,
        TrackedEntityInstanceService trackedEntityInstanceService, ProgramInstanceService programInstanceService,
        IdentifiableObjectManager identifiableObjectManager )
    {
        super( incomingSmsService, smsSender, userService, trackedEntityTypeService, trackedEntityAttributeService,
            programService, organisationUnitService, categoryService, dataElementService, programStageInstanceService,
            identifiableObjectManager );

        this.relationshipService = relationshipService;
        this.relationshipTypeService = relationshipTypeService;
        this.trackedEntityInstanceService = trackedEntityInstanceService;
        this.programInstanceService = programInstanceService;
    }

    @Override
    protected SmsResponse postProcess( IncomingSms sms, SmsSubmission submission )
        throws SMSProcessingException
    {
        RelationshipSmsSubmission subm = (RelationshipSmsSubmission) submission;

        Uid fromid = subm.getFrom();
        Uid toid = subm.getTo();
        Uid typeid = subm.getRelationshipType();

        RelationshipType relType = relationshipTypeService.getRelationshipType( typeid.getUid() );

        if ( relType == null )
        {
            throw new SMSProcessingException( SmsResponse.INVALID_RELTYPE.set( typeid ) );
        }

        RelationshipItem fromItem = createRelationshipItem( relType, RelationshipDir.FROM, fromid );
        RelationshipItem toItem = createRelationshipItem( relType, RelationshipDir.TO, toid );

        Relationship rel = new Relationship();

        // If we aren't given a Uid for the relationship, it will be
        // auto-generated
        if ( subm.getRelationship() != null )
        {
            rel.setUid( subm.getRelationship().getUid() );
        }

        rel.setRelationshipType( relType );
        rel.setFrom( fromItem );
        rel.setTo( toItem );
        rel.setCreated( new Date() );
        rel.setLastUpdated( new Date() );

        // TODO: Are there values we need to account for in relationships?

        relationshipService.addRelationship( rel );

        return SmsResponse.SUCCESS;
    }

    private RelationshipItem createRelationshipItem( RelationshipType relType, RelationshipDir dir, Uid objId )
    {
        RelationshipItem relItem = new RelationshipItem();
        RelationshipEntity fromEnt = relType.getFromConstraint().getRelationshipEntity();
        RelationshipEntity toEnt = relType.getFromConstraint().getRelationshipEntity();
        RelationshipEntity relEnt = dir == RelationshipDir.FROM ? fromEnt : toEnt;

        switch ( relEnt )
        {
        case TRACKED_ENTITY_INSTANCE:
            TrackedEntityInstance tei = trackedEntityInstanceService.getTrackedEntityInstance( objId.getUid() );
            if ( tei == null )
            {
                throw new SMSProcessingException( SmsResponse.INVALID_TEI.set( objId ) );
            }
            relItem.setTrackedEntityInstance( tei );
            break;

        case PROGRAM_INSTANCE:
            ProgramInstance progInst = programInstanceService.getProgramInstance( objId.getUid() );
            if ( progInst == null )
            {
                throw new SMSProcessingException( SmsResponse.INVALID_ENROLL.set( objId ) );
            }
            relItem.setProgramInstance( progInst );
            break;

        case PROGRAM_STAGE_INSTANCE:
            ProgramStageInstance stageInst = programStageInstanceService.getProgramStageInstance( objId.getUid() );
            if ( stageInst == null )
            {
                throw new SMSProcessingException( SmsResponse.INVALID_EVENT.set( objId ) );
            }
            relItem.setProgramStageInstance( stageInst );
            break;

        }

        return relItem;
    }

    @Override
    protected boolean handlesType( SubmissionType type )
    {
        return (type == SubmissionType.RELATIONSHIP);
    }

}