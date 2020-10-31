package com.mass3d.config;

import com.mass3d.category.CategoryService;
import com.mass3d.configuration.ConfigurationService;
import com.mass3d.dataelement.DataElementDefaultDimensionPopulator;
import com.mass3d.dataelement.DataElementService;
import com.mass3d.external.conf.ConfigurationKey;
import com.mass3d.external.conf.DhisConfigurationProvider;
import com.mass3d.i18n.I18nLocaleService;
import com.mass3d.message.MessageService;
import com.mass3d.period.PeriodStore;
import com.mass3d.period.PeriodTypePopulator;
import com.mass3d.scheduling.JobConfigurationService;
import com.mass3d.scheduling.SchedulingManager;
import com.mass3d.setting.SystemSettingManager;
import com.mass3d.startup.ConfigurationPopulator;
import com.mass3d.startup.DefaultAdminUserPopulator;
import com.mass3d.startup.I18nLocalePopulator;
import com.mass3d.startup.SchedulerStart;
import com.mass3d.startup.SettingUpgrader;
import com.mass3d.startup.TestDataElementPopulator;
import com.mass3d.startup.TwoFAPopulator;
import com.mass3d.user.CurrentUserService;
import com.mass3d.user.UserService;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author Luciano Fiandesio
 */
@Configuration
//@ComponentScan(basePackages = {"com.mass3d"})
//@ImportResource({"classpath*:META-INF/mass3d/beans.xml"})
//@EnableTransactionManagement
public class StartupConfig
{
//    @Bean( "com.mass3d.period.PeriodTypePopulator" )
//    public PeriodTypePopulator periodTypePopulator( PeriodStore periodStore, SessionFactory sessionFactory )
//    {
//        PeriodTypePopulator populator = new PeriodTypePopulator( periodStore, sessionFactory );
//        populator.setName( "PeriodTypePopulator" );
//        populator.setRunlevel( 3 );
//        return populator;
//    }

    @Bean
    public TwoFAPopulator twoFAPopulator( UserService userService, CurrentUserService currentUserService )
    {
        TwoFAPopulator populator = new TwoFAPopulator( userService, currentUserService );
        populator.setName( "PeriodTypePopulator" );
        populator.setRunlevel( 3 );
        populator.setSkipInTests( true );
        return populator;
    }

    @Bean( "com.mass3d.dataelement.DataElementDefaultDimensionPopulator" )
    public DataElementDefaultDimensionPopulator dataElementDefaultDimensionPopulator(
        DataElementService dataElementService, CategoryService categoryService )
    {
        DataElementDefaultDimensionPopulator populator = new DataElementDefaultDimensionPopulator( dataElementService,
            categoryService );
        populator.setName( "DataElementDefaultDimensionPopulator" );
        populator.setRunlevel( 4 );
        return populator;
    }

    @Bean( "com.mass3d.startup.ConfigurationPopulator" )
    public ConfigurationPopulator configurationPopulator( ConfigurationService configurationService,
        DhisConfigurationProvider dhisConfigurationProvider )
    {
        ConfigurationPopulator populator = new ConfigurationPopulator( configurationService,
            dhisConfigurationProvider );
        populator.setName( "ConfigurationPopulator" );
        populator.setRunlevel( 12 );
        populator.setSkipInTests( true );
        return populator;
    }

    @Bean( "com.mass3d.startup.I18nLocalePopulator" )
    public I18nLocalePopulator i18nLocalePopulator( I18nLocaleService i18nLocaleService )
    {
        I18nLocalePopulator populator = new I18nLocalePopulator( i18nLocaleService );
        populator.setName( "I18nLocalePopulator" );
        populator.setRunlevel( 13 );
        populator.setSkipInTests( true );
        return populator;
    }

//    @Bean( "com.mass3d.startup.ModelUpgrader" )
//    public ModelUpgrader modelUpgrader( OrganisationUnitService organisationUnitService,
//        CategoryService categoryService )
//    {
//        ModelUpgrader upgrader = new ModelUpgrader( organisationUnitService, categoryService );
//        upgrader.setName( "ModelUpgrader" );
//        upgrader.setRunlevel( 7 );
//        upgrader.setSkipInTests( true );
//        return upgrader;
//    }

//    @Bean( "com.mass3d.startup.ExpressionUpgrader" )
//    public ExpressionUpgrader expressionUpgrader( DataEntryFormService dataEntryFormService,
//        DataElementService dataElementService, CategoryService categoryService, IndicatorService indicatorService,
//        ConstantService constantService, ExpressionService expressionService )
//    {
//        ExpressionUpgrader upgrader = new ExpressionUpgrader( dataEntryFormService, dataElementService, categoryService,
//            indicatorService, constantService, expressionService );
//        upgrader.setRunlevel( 11 );
//        upgrader.setSkipInTests( true );
//        return upgrader;
//    }

    @Bean( "com.mass3d.startup.SettingUpgrader" )
    public SettingUpgrader settingUpgrader( SystemSettingManager systemSettingManager )
    {
        SettingUpgrader upgrader = new SettingUpgrader( systemSettingManager );
        upgrader.setRunlevel( 14 );
        upgrader.setName( "SettingUpgrader" );
        upgrader.setSkipInTests( true );
        return upgrader;
    }

    @Bean( "com.mass3d.startup.DefaultAdminUserPopulator" )
    public DefaultAdminUserPopulator defaultAdminUserPopulator( UserService userService )
    {
        DefaultAdminUserPopulator upgrader = new DefaultAdminUserPopulator( userService );
        upgrader.setName( "defaultAdminUserPopulator" );
        upgrader.setRunlevel( 2 );
        upgrader.setSkipInTests( true );
        return upgrader;
    }

    // temporary for test
    @Bean( "com.mass3d.startup.TestDataElementPopulator" )
    public TestDataElementPopulator testDataElementPopulator( DataElementService dataElementService, CategoryService categoryService )
    {
        TestDataElementPopulator upgrader = new TestDataElementPopulator( dataElementService, categoryService );
        upgrader.setName( "testDataElementPopulator" );
        upgrader.setRunlevel( 8 );
        upgrader.setSkipInTests( true );
        return upgrader;
    }

    @Bean
    public SchedulerStart schedulerStart( SystemSettingManager systemSettingManager,
        JobConfigurationService jobConfigurationService, SchedulingManager schedulingManager,
        MessageService messageService, DhisConfigurationProvider configurationProvider )
    {
        SchedulerStart schedulerStart = new SchedulerStart( systemSettingManager,
            configurationProvider.getProperty( ConfigurationKey.REDIS_ENABLED ),
            configurationProvider.getProperty( ConfigurationKey.LEADER_TIME_TO_LIVE ), jobConfigurationService,
            schedulingManager, messageService );
        schedulerStart.setRunlevel( 14 );
        schedulerStart.setSkipInTests( true );
        return schedulerStart;
    }
}
