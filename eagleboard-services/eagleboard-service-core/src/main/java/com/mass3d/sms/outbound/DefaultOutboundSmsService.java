package com.mass3d.sms.outbound;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Simple {@link OutboundSmsService sms service} storing the sms in a store and
 * forwards the request to a {@link com.mass3d.sms.config.SmsMessageSender sms transport
 * service} for sending.
 */

@Service( "com.mass3d.sms.outbound.OutboundSmsService" )
@Transactional
public class DefaultOutboundSmsService
    implements OutboundSmsService
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private final OutboundSmsStore outboundSmsStore;

    public DefaultOutboundSmsService( OutboundSmsStore outboundSmsStore )
    {
        checkNotNull( outboundSmsStore );

        this.outboundSmsStore = outboundSmsStore;
    }

    // -------------------------------------------------------------------------
    // Implementation
    // -------------------------------------------------------------------------

    @Override
    public List<OutboundSms> getAll()
    {
        return outboundSmsStore.getAll();
    }

    @Override
    public List<OutboundSms> get( OutboundSmsStatus status )
    {
        return outboundSmsStore.get( status );
    }

    @Override
    public long save( OutboundSms sms )
    {
        outboundSmsStore.saveOutboundSms( sms );
        return sms.getId();
    }

    @Override
    public void delete( long outboundSmsId )
    {
        OutboundSms sms = get( outboundSmsId );

        if ( sms != null )
        {
            outboundSmsStore.delete( sms );
        }
    }

    @Override
    public void delete( String uid )
    {
        OutboundSms sms = outboundSmsStore.getByUid( uid );

        if ( sms != null )
        {
            outboundSmsStore.delete( sms );
        }
    }

    @Override
    public OutboundSms get( long id )
    {
        return outboundSmsStore.get( id );
    }

    @Override
    public OutboundSms get( String uid )
    {
        return outboundSmsStore.getByUid( uid );
    }

    @Override
    public List<OutboundSms> get( OutboundSmsStatus status, Integer min, Integer max, boolean hasPagination )
    {
        return outboundSmsStore.get( status, min, max, hasPagination );
    }

    @Override
    public List<OutboundSms> getAll( Integer min, Integer max, boolean hasPagination )
    {
        return outboundSmsStore.getAllOutboundSms( min, max, hasPagination );
    }
}