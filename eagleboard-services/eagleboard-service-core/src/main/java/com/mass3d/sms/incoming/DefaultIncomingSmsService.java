package com.mass3d.sms.incoming;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import com.mass3d.sms.MessageQueue;
import com.mass3d.user.User;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service( "com.mass3d.sms.incoming.IncomingSmsService" )
public class DefaultIncomingSmsService
    implements IncomingSmsService
{
    private static final String DEFAULT_GATEWAY = "default";

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private final IncomingSmsStore incomingSmsStore;

    private final MessageQueue incomingSmsQueue;

    public DefaultIncomingSmsService(IncomingSmsStore incomingSmsStore, @Lazy MessageQueue incomingSmsQueue) {

        checkNotNull( incomingSmsQueue );
        checkNotNull( incomingSmsStore );

        this.incomingSmsStore = incomingSmsStore;
        this.incomingSmsQueue = incomingSmsQueue;
    }

    // -------------------------------------------------------------------------
    // Implementation
    // -------------------------------------------------------------------------

    @Override
    @Transactional( readOnly = true )
    public List<IncomingSms> getAll()
    {
        return incomingSmsStore.getAll();
    }

    @Override
    @Transactional( readOnly = true )
    public List<IncomingSms> getAll( Integer min, Integer max, boolean hasPagination )
    {
        return incomingSmsStore.getAll( min, max, hasPagination );
    }

    @Override
    @Transactional
    public long save( IncomingSms sms )
    {
        if ( sms.getReceivedDate() != null )
        {
            sms.setSentDate( sms.getReceivedDate() );
        }
        else
        {
            sms.setSentDate( new Date() );
        }

        sms.setReceivedDate( new Date() );
        sms.setGatewayId( StringUtils.defaultIfBlank( sms.getGatewayId(), DEFAULT_GATEWAY ) );

        incomingSmsStore.save( sms );
        incomingSmsQueue.put( sms );
        return sms.getId();
    }

    @Override
    @Transactional
    public long save( String message, String originator, String gateway, Date receivedTime, User user )
    {
        IncomingSms sms = new IncomingSms();
        sms.setText( message );
        sms.setOriginator( originator );
        sms.setGatewayId( gateway );
        sms.setUser( user );

        if ( receivedTime != null )
        {
            sms.setSentDate( receivedTime );
        }
        else
        {
            sms.setSentDate( new Date() );
        }
        
        sms.setReceivedDate( new Date() );
        
        return save( sms );
    }

    @Override
    @Transactional
    public void delete( long id )
    {
        IncomingSms incomingSms = incomingSmsStore.get( id );

        if ( incomingSms != null )
        {
            incomingSmsStore.delete( incomingSms );
        }
    }

    @Override
    @Transactional
    public void delete( String uid )
    {
        IncomingSms incomingSms = incomingSmsStore.getByUid( uid );

        if ( incomingSms != null )
        {
            incomingSmsStore.delete( incomingSms );
        }
    }

    @Override
    @Transactional( readOnly = true )
    public IncomingSms get( long id )
    {
        return incomingSmsStore.get( id );
    }

    @Override
    @Transactional( readOnly = true )
    public IncomingSms get( String id )
    {
        return incomingSmsStore.getByUid( id );
    }

    @Override
    @Transactional
    public void update( IncomingSms incomingSms )
    {
        incomingSmsStore.update( incomingSms );
    }

    @Override
    @Transactional( readOnly = true )
    public List<IncomingSms> getSmsByStatus( SmsMessageStatus status, String originator )
    {
        return incomingSmsStore.getSmsByStatus( status, originator );
    }

    @Override
    @Transactional( readOnly = true )
    public List<IncomingSms> getSmsByStatus( SmsMessageStatus status, String keyword, Integer min, Integer max, boolean hasPagination )
    {
        return incomingSmsStore.getSmsByStatus( status, keyword, min, max, hasPagination );
    }

    @Override
    @Transactional( readOnly = true )
    public List<IncomingSms> getAllUnparsedMessages()
    {
        return incomingSmsStore.getAllUnparsedMessages();
    }
}
