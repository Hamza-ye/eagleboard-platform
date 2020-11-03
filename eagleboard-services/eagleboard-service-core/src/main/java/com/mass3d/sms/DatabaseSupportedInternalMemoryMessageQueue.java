package com.mass3d.sms;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import com.mass3d.sms.incoming.IncomingSms;
import com.mass3d.sms.incoming.IncomingSmsService;
import org.springframework.stereotype.Component;

@Component( "com.mass3d.sms.MessageQueue" )
public class DatabaseSupportedInternalMemoryMessageQueue
    implements MessageQueue
{
    private List<IncomingSms> queue = new ArrayList<>();

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------
    
    private final IncomingSmsService incomingSmsService;

    public DatabaseSupportedInternalMemoryMessageQueue( IncomingSmsService incomingSmsService )
    {
        checkNotNull( incomingSmsService );
        this.incomingSmsService = incomingSmsService;
    }

    // -------------------------------------------------------------------------
    // Implementation
    // -------------------------------------------------------------------------

    @Override
    public void put( IncomingSms message )
    {
        queue.add( message );
    }

    @Override
    public IncomingSms get()
    {
        if ( queue != null && queue.size() > 0 )
        {
            return queue.get( 0 );
        }

        return null;
    }

    @Override
    public void remove( IncomingSms message )
    {
        queue.remove( message );
    }

    @Override
    public void initialize()
    {
        Collection<IncomingSms> messages = incomingSmsService.getAllUnparsedMessages();

        if ( messages != null )
        {
            queue.addAll( messages );
        }
    }
}
