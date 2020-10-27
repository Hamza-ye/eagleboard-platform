package com.mass3d.artemis.config;

import static com.mass3d.commons.util.SystemUtils.isTestRun;

import java.util.HashMap;
import java.util.Map;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import org.apache.activemq.artemis.api.core.RoutingType;
import org.apache.activemq.artemis.api.core.SimpleString;
import org.apache.activemq.artemis.core.config.CoreAddressConfiguration;
import org.apache.activemq.artemis.core.config.CoreQueueConfiguration;
import org.apache.activemq.artemis.core.config.impl.ConfigurationImpl;
import org.apache.activemq.artemis.core.server.JournalType;
import org.apache.activemq.artemis.core.server.embedded.EmbeddedActiveMQ;
import org.apache.activemq.artemis.core.settings.impl.AddressSettings;
import org.apache.qpid.jms.JmsConnectionFactory;
import com.mass3d.artemis.AuditProducerConfiguration;
import com.mass3d.artemis.Topics;
import com.mass3d.audit.AuditScope;
import com.mass3d.external.conf.ConfigurationKey;
import com.mass3d.external.conf.DhisConfigurationProvider;
import com.mass3d.external.location.LocationManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.Environment;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.util.SocketUtils;

@Configuration
@EnableJms
@DependsOn( "artemisPortChecker" )
public class ArtemisConfig
{
    private final DhisConfigurationProvider dhisConfig;
    private final LocationManager locationManager;
    private final Environment environment;

    public ArtemisConfig(
        DhisConfigurationProvider dhisConfig,
        LocationManager locationManager,
        Environment environment )
    {
        this.dhisConfig = dhisConfig;
        this.locationManager = locationManager;
        this.environment = environment;
    }

    @Bean
    public ConnectionFactory jmsConnectionFactory( ArtemisConfigData artemisConfigData )
    {
        JmsConnectionFactory connectionFactory = new JmsConnectionFactory( String
            .format( "amqp://%s:%d", artemisConfigData.getHost(), artemisConfigData.getPort() ) );
        connectionFactory.setClientIDPrefix( "dhis2" );
        connectionFactory.setCloseLinksThatFailOnReconnect( false );
        connectionFactory.setForceAsyncAcks( true );

        return connectionFactory;
    }

    @Bean
    public JmsTemplate jmsTopicTemplate( ConnectionFactory connectionFactory, NameDestinationResolver nameDestinationResolver )
    {
        JmsTemplate template = new JmsTemplate( connectionFactory );
        template.setDeliveryMode( DeliveryMode.NON_PERSISTENT );
        template.setDestinationResolver( nameDestinationResolver );
        // set to true, since we only use topics and we want to resolve names to topic destination
        template.setPubSubDomain( true );

        return template;
    }

    @Bean
    public JmsTemplate jmsQueueTemplate( ConnectionFactory connectionFactory, NameDestinationResolver nameDestinationResolver )
    {
        JmsTemplate template = new JmsTemplate( connectionFactory );
        template.setDeliveryMode( DeliveryMode.PERSISTENT );
        template.setDestinationResolver( nameDestinationResolver );
        template.setPubSubDomain( false );

        return template;
    }

    @Bean // configured for topics
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory( ConnectionFactory connectionFactory, NameDestinationResolver nameDestinationResolver )
    {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory( connectionFactory );
        factory.setDestinationResolver( nameDestinationResolver );
        // set to true, since we only use topics and we want to resolve names to topic destination
        factory.setPubSubDomain( true );
        // 1 forces the listener to use only one consumer, to avoid duplicated messages
        factory.setConcurrency( "1" );

        return factory;
    }

    @Bean // configured for queues
    public DefaultJmsListenerContainerFactory jmsQueueListenerContainerFactory( ConnectionFactory connectionFactory, NameDestinationResolver nameDestinationResolver )
    {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory( connectionFactory );
        factory.setDestinationResolver( nameDestinationResolver );
        factory.setPubSubDomain( false );
        factory.setConcurrency( "5-10" );

        return factory;
    }

    @Bean
    public EmbeddedActiveMQ createEmbeddedServer( ArtemisConfigData artemisConfigData ) throws Exception
    {
        EmbeddedActiveMQ server = new EmbeddedActiveMQ();

        org.apache.activemq.artemis.core.config.Configuration config = new ConfigurationImpl();

        ArtemisEmbeddedConfig embeddedConfig = artemisConfigData.getEmbedded();

        config.addAcceptorConfiguration( "tcp",
            String.format( "tcp://%s:%d?protocols=AMQP&jms.useAsyncSend=%s&nioRemotingThreads=%d",
                artemisConfigData.getHost(),
                artemisConfigData.getPort(),
                artemisConfigData.isSendAsync(),
                embeddedConfig.getNioRemotingThreads() ) );

        config.setSecurityEnabled( embeddedConfig.isSecurity() );
        config.setPersistenceEnabled( embeddedConfig.isPersistence() );

        if ( locationManager.externalDirectorySet() && embeddedConfig.isPersistence() )
        {
            String dataDir = locationManager.getExternalDirectoryPath();
            config.setJournalDirectory( dataDir + "/artemis/journal" );

            config.setJournalType( JournalType.NIO );
            config.setLargeMessagesDirectory( dataDir + "/artemis/largemessages" );
            config.setBindingsDirectory( dataDir + "/artemis/bindings" );
            config.setPagingDirectory( dataDir + "/artemis/paging" );
        }

        config.addAddressesSetting( "#",
            new AddressSettings()
                .setDeadLetterAddress( SimpleString.toSimpleString( "DLQ" ) )
                .setExpiryAddress( SimpleString.toSimpleString( "ExpiryQueue" ) ) );

        config.addAddressConfiguration(
            new CoreAddressConfiguration()
                .setName( "DLQ" )
                .addRoutingType( RoutingType.ANYCAST )
                .addQueueConfiguration(
                    new CoreQueueConfiguration()
                        .setName( "DLQ" )
                        .setRoutingType( RoutingType.ANYCAST ) ) );

        config.addAddressConfiguration(
            new CoreAddressConfiguration()
                .setName( "ExpiryQueue" )
                .addRoutingType( RoutingType.ANYCAST )
                .addQueueConfiguration(
                    new CoreQueueConfiguration()
                        .setName( "ExpiryQueue" )
                        .setRoutingType( RoutingType.ANYCAST ) ) );

        server.setConfiguration( config );

        return server;
    }

    @Bean
    public ArtemisConfigData getArtemisConfig()
    {
        ArtemisConfigData artemisConfigData = new ArtemisConfigData();
        artemisConfigData
            .setMode( ArtemisMode.valueOf( (dhisConfig.getProperty( ConfigurationKey.ARTEMIS_MODE )).toUpperCase() ) );
        artemisConfigData.setHost( dhisConfig.getProperty( ConfigurationKey.ARTEMIS_HOST ) );

        artemisConfigData.setPort( Integer.parseInt( dhisConfig.getProperty( ConfigurationKey.ARTEMIS_PORT ) ) );

        if ( isTestRun( this.environment.getActiveProfiles() ) )
        {
            artemisConfigData.setPort( SocketUtils.findAvailableTcpPort( 3000 ) );
        }

        artemisConfigData.setUsername( dhisConfig.getProperty( ConfigurationKey.ARTEMIS_USERNAME ) );
        artemisConfigData.setPassword( dhisConfig.getProperty( ConfigurationKey.ARTEMIS_PASSWORD ) );

        ArtemisEmbeddedConfig artemisEmbeddedConfig = new ArtemisEmbeddedConfig();
        artemisEmbeddedConfig.setSecurity(
            Boolean.parseBoolean( dhisConfig.getProperty( ConfigurationKey.ARTEMIS_EMBEDDED_SECURITY ) ) );
        artemisEmbeddedConfig.setPersistence(
            Boolean.parseBoolean( dhisConfig.getProperty( ConfigurationKey.ARTEMIS_EMBEDDED_PERSISTENCE ) ) );

        artemisConfigData.setEmbedded( artemisEmbeddedConfig );

        return artemisConfigData;
    }

    /**
     * Holds a Map of AuditScope -> Topic Name so that a Producer can resolve the topic name from the scope
     */
    @Bean
    public Map<AuditScope, String> scopeToDestinationMap()
    {
        Map<AuditScope, String> scopeDestinationMap = new HashMap<>();

        scopeDestinationMap.put( AuditScope.METADATA, Topics.METADATA_TOPIC_NAME );
        scopeDestinationMap.put( AuditScope.AGGREGATE, Topics.AGGREGATE_TOPIC_NAME );
        scopeDestinationMap.put( AuditScope.TRACKER, Topics.TRACKER_TOPIC_NAME );

        return scopeDestinationMap;
    }

    @Bean
    public AuditProducerConfiguration producerConfiguration()
    {
        return AuditProducerConfiguration.builder()
            .useQueue( dhisConfig.isEnabled( ConfigurationKey.AUDIT_USE_INMEMORY_QUEUE_ENABLED ) )
            .build();
    }
}
