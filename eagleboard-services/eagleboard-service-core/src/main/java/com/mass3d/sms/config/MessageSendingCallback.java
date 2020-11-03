package com.mass3d.sms.config;

import lombok.extern.slf4j.Slf4j;
import com.mass3d.outboundmessage.OutboundMessageResponse;
import com.mass3d.outboundmessage.OutboundMessageResponseSummary;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Slf4j
@Component( "com.mass3d.sms.config.SMSSendingCallback" )
public class MessageSendingCallback
{
    public ListenableFutureCallback<OutboundMessageResponse> getCallBack()
    {
        return new ListenableFutureCallback<OutboundMessageResponse>()
        {
            @Override
            public void onFailure( Throwable ex )
            {
                log.error( "Message sending failed", ex );
            }

            @Override
            public void onSuccess( OutboundMessageResponse result )
            {
                if ( result.isOk() )
                {
                    log.info( "Message sending successful: " + result.getDescription() );
                }
                else
                {
                    log.error( "Message sending failed: " + result.getDescription() );
                }
            }
        };
    }

    public ListenableFutureCallback<OutboundMessageResponseSummary> getBatchCallBack()
    {
        return new ListenableFutureCallback<OutboundMessageResponseSummary>()
        {
            @Override
            public void onFailure( Throwable ex )
            {
                log.error( "Message sending failed", ex );
            }

            @Override
            public void onSuccess( OutboundMessageResponseSummary result )
            {
                int successful = result.getSent();
                int failed = result.getFailed();

                log.info( String
                    .format( "%s Message sending status: Successful: %d Failed: %d", result.getChannel().name(), successful, failed ) );
            }
        };
    }
}
