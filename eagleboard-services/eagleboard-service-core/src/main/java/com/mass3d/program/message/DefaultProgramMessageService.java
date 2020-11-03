package com.mass3d.program.message;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import com.mass3d.common.DeliveryChannel;
import com.mass3d.common.IdentifiableObject;
import com.mass3d.common.IdentifiableObjectManager;
import com.mass3d.common.IllegalQueryException;
import com.mass3d.message.MessageSender;
import com.mass3d.organisationunit.OrganisationUnitService;
import com.mass3d.outboundmessage.BatchResponseStatus;
import com.mass3d.outboundmessage.OutboundMessageBatch;
import com.mass3d.outboundmessage.OutboundMessageBatchService;
import com.mass3d.outboundmessage.OutboundMessageResponseSummary;
import com.mass3d.program.Program;
import com.mass3d.program.ProgramInstance;
import com.mass3d.program.ProgramService;
import com.mass3d.program.ProgramStageInstance;
import com.mass3d.security.acl.AclService;
import com.mass3d.sms.config.MessageSendingCallback;
import com.mass3d.trackedentity.TrackedEntityInstanceService;
import com.mass3d.user.CurrentUserService;
import com.mass3d.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFuture;

@Slf4j
@Service( "com.mass3d.program.message.ProgramMessageService" )
public class DefaultProgramMessageService
    implements ProgramMessageService
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    protected final IdentifiableObjectManager manager;

    private final ProgramMessageStore programMessageStore;

    private final OrganisationUnitService organisationUnitService;

    private final TrackedEntityInstanceService trackedEntityInstanceService;

    private final ProgramService programService;

    private final OutboundMessageBatchService messageBatchService;

    private final CurrentUserService currentUserService;

    private final List<DeliveryChannelStrategy> strategies;

    private final List<MessageBatchCreatorService> batchCreators;

    private final AclService aclService;

    public DefaultProgramMessageService( IdentifiableObjectManager manager, ProgramMessageStore programMessageStore,
        OrganisationUnitService organisationUnitService, TrackedEntityInstanceService trackedEntityInstanceService,
        ProgramService programService, OutboundMessageBatchService messageBatchService,
        CurrentUserService currentUserService, List<DeliveryChannelStrategy> strategies,
        List<MessageBatchCreatorService> batchCreators, AclService aclService )
    {
        checkNotNull( manager );
        checkNotNull( programMessageStore );
        checkNotNull( organisationUnitService );
        checkNotNull( trackedEntityInstanceService );
        checkNotNull( programService );
        checkNotNull( messageBatchService );
        checkNotNull( currentUserService );
        checkNotNull( strategies );
        checkNotNull( batchCreators );
        checkNotNull( aclService );

        this.manager = manager;
        this.programMessageStore = programMessageStore;
        this.organisationUnitService = organisationUnitService;
        this.trackedEntityInstanceService = trackedEntityInstanceService;
        this.programService = programService;
        this.messageBatchService = messageBatchService;
        this.currentUserService = currentUserService;
        this.strategies = strategies;
        this.batchCreators = batchCreators;
        this.aclService = aclService;
    }

    @Autowired
    @Qualifier( "smsMessageSender" )
    private MessageSender smsSender;

    @Autowired
    private MessageSendingCallback sendingCallback;

    // -------------------------------------------------------------------------
    // Implementation methods
    // -------------------------------------------------------------------------

    @Override
    @Transactional( readOnly = true )
    public ProgramMessageQueryParams getFromUrl( Set<String> ou, String piUid, String psiUid,
        ProgramMessageStatus messageStatus, Integer page, Integer pageSize, Date afterDate, Date beforeDate )
    {
        ProgramMessageQueryParams params = new ProgramMessageQueryParams();

        if ( piUid != null )
        {
            if ( manager.exists( ProgramInstance.class, piUid ) )
            {
                params.setProgramInstance( manager.get( ProgramInstance.class, piUid ) );
            }
            else
            {
                throw new IllegalQueryException( "ProgramInstance does not exist." );
            }
        }

        if ( psiUid != null )
        {
            if ( manager.exists( ProgramStageInstance.class, psiUid ) )
            {
                params.setProgramStageInstance( manager.get( ProgramStageInstance.class, psiUid ) );
            }
            else
            {
                throw new IllegalQueryException( "ProgramStageInstance does not exist." );
            }
        }

        params.setOrganisationUnit( ou );
        params.setMessageStatus( messageStatus );
        params.setPage( page );
        params.setPageSize( pageSize );
        params.setAfterDate( afterDate );
        params.setBeforeDate( beforeDate );

        return params;
    }

    @Override
    @Transactional( readOnly = true )
    public boolean exists( String uid )
    {
        return programMessageStore.exists( uid );
    }

    @Override
    @Transactional( readOnly = true )
    public ProgramMessage getProgramMessage( long id )
    {
        return programMessageStore.get( id );
    }

    @Override
    @Transactional( readOnly = true )
    public ProgramMessage getProgramMessage( String uid )
    {
        return programMessageStore.getByUid( uid );
    }

    @Override
    @Transactional( readOnly = true )
    public List<ProgramMessage> getAllProgramMessages()
    {
        return programMessageStore.getAll();
    }

    @Override
    @Transactional( readOnly = true )
    public List<ProgramMessage> getProgramMessages( ProgramMessageQueryParams params )
    {
        hasAccess( params, currentUserService.getCurrentUser() );
        validateQueryParameters( params );

        return programMessageStore.getProgramMessages( params );
    }

    @Override
    @Transactional
    public long saveProgramMessage( ProgramMessage programMessage )
    {
        programMessageStore.save( programMessage );
        return programMessage.getId();
    }

    @Override
    @Transactional
    public void updateProgramMessage( ProgramMessage programMessage )
    {
        programMessageStore.update( programMessage );
    }

    @Override
    public void deleteProgramMessage( ProgramMessage programMessage )
    {
        programMessageStore.delete( programMessage );
    }

    @Override
    @Transactional
    public BatchResponseStatus sendMessages( List<ProgramMessage> programMessages )
    {
        List<ProgramMessage> populatedProgramMessages = programMessages.stream()
            .filter( this::hasDataWriteAccess )
            .map( this::setAttributesBasedOnStrategy )
            .collect( Collectors.toList() );

        List<OutboundMessageBatch> batches = createBatches( populatedProgramMessages );

        BatchResponseStatus status = new BatchResponseStatus( messageBatchService.sendBatches( batches ) );

        saveProgramMessages( programMessages, status );

        return status;
    }

    @Override
    @Transactional
    public void sendMessagesAsync( List<ProgramMessage> programMessages )
    {
        List<ProgramMessage> populatedProgramMessages = programMessages.stream()
            .filter( this::hasDataWriteAccess )
            .map( this::setAttributesBasedOnStrategy )
            .collect( Collectors.toList() );

        List<OutboundMessageBatch> batches = createBatches( populatedProgramMessages );

        for ( OutboundMessageBatch batch : batches )
        {
            ListenableFuture<OutboundMessageResponseSummary> future = smsSender.sendMessageBatchAsync( batch );
            future.addCallback( sendingCallback.getBatchCallBack() );
        }
    }

    @Override
    @Transactional( readOnly = true )
    public void hasAccess( ProgramMessageQueryParams params, User user )
    {
        ProgramInstance programInstance = null;

        Set<Program> programs;

        if ( params.hasProgramInstance() )
        {
            programInstance = params.getProgramInstance();
        }

        if ( params.hasProgramStageInstance() )
        {
            programInstance = params.getProgramStageInstance().getProgramInstance();
        }

        if ( programInstance == null )
        {
            throw new IllegalQueryException( "ProgramInstance or ProgramStageInstance has to be provided" );
        }

        programs = new HashSet<>( programService.getUserPrograms( user ) );

        if ( user != null && !programs.contains( programInstance.getProgram() ) )
        {
            throw new IllegalQueryException( "User does not have access to the required program" );
        }
    }

    @Override
    @Transactional( readOnly = true )
    public void validateQueryParameters( ProgramMessageQueryParams params )
    {
        String violation = null;

        if ( !params.hasProgramInstance() && !params.hasProgramStageInstance() )
        {
            violation = "Program instance or program stage instance must be provided";
        }

        if ( violation != null )
        {
            log.warn( "Parameter validation failed: " + violation );

            throw new IllegalQueryException( violation );
        }
    }

    @Override
    @Transactional( readOnly = true )
    public void validatePayload( ProgramMessage message )
    {
        String violation = null;

        ProgramMessageRecipients recipients = message.getRecipients();

        if ( message.getText() == null )
        {
            violation = "Message content must be provided";
        }

        if ( message.getDeliveryChannels() == null || message.getDeliveryChannels().isEmpty() )
        {
            violation = "Delivery channel must be specified";
        }

        if ( message.getProgramInstance() == null && message.getProgramStageInstance() == null )
        {
            violation = "Program instance or program stage instance must be specified";
        }

        if ( recipients.getTrackedEntityInstance() != null && trackedEntityInstanceService
            .getTrackedEntityInstance( recipients.getTrackedEntityInstance().getUid() ) == null )
        {
            violation = "Tracked entity does not exist";
        }

        if ( recipients.getOrganisationUnit() != null
            && organisationUnitService.getOrganisationUnit( recipients.getOrganisationUnit().getUid() ) == null )
        {
            violation = "Organisation unit does not exist";
        }

        if ( violation != null )
        {
            log.info( "Message validation failed: " + violation );

            throw new IllegalQueryException( violation );

        }
    }

    // ---------------------------------------------------------------------
    // Supportive Methods
    // ---------------------------------------------------------------------

    private boolean hasDataWriteAccess( ProgramMessage message )
    {
        IdentifiableObject object = null;

        boolean isAuthorized;

        if ( message.hasProgramInstance() )
        {
            object = message.getProgramInstance().getProgram();
        }
        else if ( message.hasProgramStageInstance() )
        {
            object = message.getProgramStageInstance().getProgramStage();
        }

        if ( object != null )
        {
            isAuthorized = aclService.canDataWrite( currentUserService.getCurrentUser(), object );

            if ( !isAuthorized )
            {
                log.error( String.format( "Sending message failed. User does not have write access for %s.", object.getName() ) );

                return false;
            }
        }

        return true;
    }

    private void saveProgramMessages( List<ProgramMessage> messageBatch, BatchResponseStatus status )
    {
        messageBatch.parallelStream()
            .map( pm -> setParameters( pm, status ) )
            .forEach( this::saveProgramMessage );
    }

    private ProgramMessage setParameters( ProgramMessage message, BatchResponseStatus status )
    {
        message.setProgramInstance( getProgramInstance( message ) );
        message.setProgramStageInstance( getProgramStageInstance( message ) );
        message.setProcessedDate( new Date() );
        message.setMessageStatus( status.isOk() ? ProgramMessageStatus.SENT : ProgramMessageStatus.FAILED );

        return message;
    }

    private List<OutboundMessageBatch> createBatches( List<ProgramMessage> programMessages )
    {
        return batchCreators.stream()
            .map( bc -> bc.getMessageBatch( programMessages ) )
            .filter( bc -> !bc.getMessages().isEmpty() )
            .collect( Collectors.toList() );
    }

    private ProgramInstance getProgramInstance( ProgramMessage programMessage )
    {
        if ( programMessage.getProgramInstance() != null )
        {
            return manager.get( ProgramInstance.class, programMessage.getProgramInstance().getUid() );
        }

        return null;
    }

    private ProgramStageInstance getProgramStageInstance( ProgramMessage programMessage )
    {
        if ( programMessage.getProgramStageInstance() != null )
        {
            return manager.get( ProgramStageInstance.class, programMessage.getProgramStageInstance().getUid() );
        }

        return null;
    }

    private ProgramMessage setAttributesBasedOnStrategy( ProgramMessage message )
    {
        Set<DeliveryChannel> channels = message.getDeliveryChannels();

        for ( DeliveryChannel channel : channels )
        {
            for ( DeliveryChannelStrategy strategy : strategies )
            {
                if ( strategy.getDeliveryChannel().equals( channel ) )
                {
                    strategy.setAttributes( message );
                }
            }
        }

        return message;
    }
}