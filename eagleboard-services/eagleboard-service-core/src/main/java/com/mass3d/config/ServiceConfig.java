package com.mass3d.config;

import com.mass3d.i18n.ui.resourcebundle.DefaultResourceBundleManager;
import com.mass3d.i18n.ui.resourcebundle.ResourceBundleManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration( "coreServiceConfig" )
//@ComponentScan(basePackages = {"com.mass3d"})
//@ImportResource({"classpath*:META-INF/mass3d/beans.xml"})
//@EnableTransactionManagement
public class ServiceConfig
{
    @Bean( "taskScheduler" )
    public ThreadPoolTaskScheduler threadPoolTaskScheduler()
    {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize( 25 );
        return threadPoolTaskScheduler;
    }

//    @Bean( "com.mass3d.setting.StyleManager" )
//    public StyleManager styleManager( SystemSettingManager systemSettingManager, UserSettingService userSettingService,
//        I18nManager i18nManager )
//    {
//        SortedMap<String, String> styles = new TreeMap<>();
//        styles.put( "light_blue", "light_blue/light_blue.css" );
//        styles.put( "green", "green/green.css" );
//        styles.put( "myanmar", "myanmar/myanmar.css" );
//        styles.put( "vietnam", "vietnam/vietnam.css" );
//        styles.put( "india", "india/india.css" );
//
//        return new DefaultStyleManager( systemSettingManager, userSettingService, styles, i18nManager );
//    }

//    @Bean( "com.mass3d.outboundmessage.OutboundMessageService" )
//    public DefaultOutboundMessageBatchService defaultOutboundMessageBatchService( SmsMessageSender smsMessageSender,
//        EmailMessageSender emailMessageSender )
//    {
//        Map<DeliveryChannel, MessageSender> channels = new HashMap<>();
//        channels.put( DeliveryChannel.SMS, smsMessageSender );
//        channels.put( DeliveryChannel.EMAIL, emailMessageSender );
//
//        DefaultOutboundMessageBatchService service = new DefaultOutboundMessageBatchService();
//
//        service.setMessageSenders( channels );
//
//        return service;
//    }

    @Bean( "com.mass3d.i18n.ui.resourcebundle.ResourceBundleManager" )
    public ResourceBundleManager resourceBundleManager()
    {
        return new DefaultResourceBundleManager( "i18n_global", "i18n_module" );
    }
}
