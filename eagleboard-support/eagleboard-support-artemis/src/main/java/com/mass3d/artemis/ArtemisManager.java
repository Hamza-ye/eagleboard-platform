package com.mass3d.artemis;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.artemis.core.server.embedded.EmbeddedActiveMQ;
import com.mass3d.artemis.config.ArtemisConfigData;
import com.mass3d.artemis.config.ArtemisMode;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ArtemisManager
{
    private final EmbeddedActiveMQ embeddedActiveMQ;
    private final ArtemisConfigData artemisConfigData;

    public ArtemisManager(
        EmbeddedActiveMQ embeddedActiveMQ,
        ArtemisConfigData artemisConfigData )
    {
        this.embeddedActiveMQ = embeddedActiveMQ;
        this.artemisConfigData = artemisConfigData;
    }

    @PostConstruct
    public void startAmqp() throws Exception
    {
        if ( ArtemisMode.EMBEDDED == artemisConfigData.getMode() )
        {
            log.info( "Starting embedded Artemis ActiveMQ server." );
            embeddedActiveMQ.start();
        }
    }

    @PreDestroy
    public void stopAmqp() throws Exception
    {
        if ( embeddedActiveMQ == null )
        {
            return;
        }

        embeddedActiveMQ.stop();
    }
}
