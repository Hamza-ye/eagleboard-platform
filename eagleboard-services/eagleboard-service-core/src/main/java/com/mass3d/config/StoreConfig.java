package com.mass3d.config;

import com.mass3d.common.hibernate.HibernateIdentifiableObjectStore;
import com.mass3d.constant.Constant;
import com.mass3d.expression.Expression;
import com.mass3d.hibernate.HibernateGenericStore;
import com.mass3d.indicator.IndicatorGroup;
import com.mass3d.indicator.IndicatorGroupSet;
import com.mass3d.indicator.IndicatorType;
import com.mass3d.option.OptionSet;
import com.mass3d.program.ProgramExpression;
import com.mass3d.program.ProgramIndicatorGroup;
import com.mass3d.program.notification.ProgramNotificationInstance;
import com.mass3d.program.notification.ProgramNotificationTemplate;
import com.mass3d.scheduling.JobConfiguration;
import com.mass3d.security.acl.AclService;
import com.mass3d.user.CurrentUserService;
import com.mass3d.user.UserAccess;
import com.mass3d.user.UserGroup;
import com.mass3d.user.UserGroupAccess;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration("coreStoreConfig")
//@ComponentScan(basePackages = {"com.mass3d"})
//@ImportResource({"classpath*:META-INF/mass3d/beans.xml"})
//@EnableTransactionManagement
public class StoreConfig {

  @Autowired
  private SessionFactory sessionFactory;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Autowired
  private ApplicationEventPublisher publisher;

  @Autowired
  private CurrentUserService currentUserService;

  @Autowired
  private AclService aclService;

  @Bean("com.mass3d.indicator.IndicatorTypeStore")
  public HibernateIdentifiableObjectStore<IndicatorType> indicatorTypeStore() {
    return new HibernateIdentifiableObjectStore<>(sessionFactory, jdbcTemplate,
        publisher,
        IndicatorType.class, currentUserService, aclService, true);
  }

  @Bean("com.mass3d.indicator.IndicatorGroupStore")
  public HibernateIdentifiableObjectStore<IndicatorGroup> indicatorGroupStore() {
    return new HibernateIdentifiableObjectStore<>(sessionFactory,
        jdbcTemplate, publisher, IndicatorGroup.class, currentUserService, aclService,
        true);
  }

  @Bean("com.mass3d.indicator.IndicatorGroupSetStore")
  public HibernateIdentifiableObjectStore<IndicatorGroupSet> indicatorGroupSetStore() {
    return new HibernateIdentifiableObjectStore<>(sessionFactory, jdbcTemplate,
        publisher, IndicatorGroupSet.class, currentUserService, aclService, true);
  }

  //    @Bean( "com.mass3d.predictor.PredictorGroupStore" )
//    public HibernateIdentifiableObjectStore<PredictorGroup> predictorGroupStore()
//    {
//        return new HibernateIdentifiableObjectStore<>( sessionFactory,
//            jdbcTemplate, publisher, PredictorGroup.class, currentUserService, aclService, true );
//    }

  @Bean("com.mass3d.expression.ExpressionStore")
  public HibernateGenericStore<Expression> expressionStore() {
    return new HibernateGenericStore<>( sessionFactory, jdbcTemplate, publisher,
        Expression.class, true );
  }

  @Bean("com.mass3d.user.UserGroupStore")
  public HibernateIdentifiableObjectStore<UserGroup> userGroupStore() {
    return new HibernateIdentifiableObjectStore<>(sessionFactory,
        jdbcTemplate, publisher, UserGroup.class, currentUserService, aclService, true);
  }

  @Bean("com.mass3d.user.UserGroupAccessStore")
  public HibernateGenericStore<UserGroupAccess> userGroupAccessStore() {
    return new HibernateGenericStore<>( sessionFactory, jdbcTemplate, publisher,
        UserGroupAccess.class, true );
  }

  @Bean("com.mass3d.user.UserAccessStore")
  public HibernateGenericStore<UserAccess> userAccessStore() {
    return new HibernateGenericStore<>( sessionFactory, jdbcTemplate, publisher,
        UserAccess.class, true );
  }

  @Bean("com.mass3d.configuration.ConfigurationStore")
  public HibernateGenericStore<com.mass3d.configuration.Configuration> configurationStore() {
    return new HibernateGenericStore<>(
        sessionFactory, jdbcTemplate, publisher, com.mass3d.configuration.Configuration.class, true );
  }

  @Bean("com.mass3d.constant.ConstantStore")
  public HibernateIdentifiableObjectStore<Constant> constantStore() {
    return new HibernateIdentifiableObjectStore<>(sessionFactory, jdbcTemplate,
        publisher, Constant.class, currentUserService, aclService, true);
  }

  @Bean("com.mass3d.scheduling.JobConfigurationStore")
  public HibernateIdentifiableObjectStore<JobConfiguration> jobConfigurationStore() {
    return new HibernateIdentifiableObjectStore<>(
        sessionFactory, jdbcTemplate, publisher, JobConfiguration.class,
        currentUserService, aclService, true);
  }

  @Bean("com.mass3d.option.OptionSetStore")
  public HibernateIdentifiableObjectStore<OptionSet> optionSetStore() {
    return new HibernateIdentifiableObjectStore<>(sessionFactory, jdbcTemplate,
        publisher, OptionSet.class, currentUserService, aclService, true);
  }

//    @Bean( "com.mass3d.legend.LegendSetStore" )
//    public HibernateIdentifiableObjectStore<LegendSet> legendSetStore()
//    {
//        return new HibernateIdentifiableObjectStore<>( sessionFactory, jdbcTemplate, publisher,
//            LegendSet.class, currentUserService, aclService, true );
//    }

    @Bean( "com.mass3d.program.ProgramIndicatorGroupStore" )
    public HibernateIdentifiableObjectStore<ProgramIndicatorGroup> programIndicatorGroupStore()
    {
        return new HibernateIdentifiableObjectStore<>( sessionFactory, jdbcTemplate, publisher,
            ProgramIndicatorGroup.class, currentUserService, aclService, true );
    }

//    @Bean( "com.mass3d.report.ReportStore" )
//    public HibernateIdentifiableObjectStore<Report> reportStore()
//    {
//        return new HibernateIdentifiableObjectStore<>( sessionFactory,
//            jdbcTemplate, publisher, Report.class, currentUserService, aclService, true );
//    }

//    @Bean( "com.mass3d.chart.ChartStore" )
//    public HibernateAnalyticalObjectStore<Chart> chartStore()
//    {
//        return new HibernateAnalyticalObjectStore<>( sessionFactory,
//            jdbcTemplate, publisher, Chart.class, currentUserService, aclService, true );
//    }

//    @Bean( "com.mass3d.reporttable.ReportTableStore" )
//    public HibernateAnalyticalObjectStore<ReportTable> reportTableStore()
//    {
//        return new HibernateAnalyticalObjectStore<>( sessionFactory,
//            jdbcTemplate, publisher, ReportTable.class, currentUserService, aclService, true );
//    }
//
//    @Bean( "com.mass3d.visualization.generic.VisualizationStore" )
//    public HibernateAnalyticalObjectStore<Visualization> visuzliationStore()
//    {
//        return new HibernateAnalyticalObjectStore<>( sessionFactory,
//            jdbcTemplate, publisher, Visualization.class, currentUserService, aclService, true );
//    }
//
//    @Bean( "com.mass3d.dashboard.DashboardStore" )
//    public HibernateIdentifiableObjectStore<Dashboard> dashboardStore()
//    {
//        return new HibernateIdentifiableObjectStore<>( sessionFactory,
//            jdbcTemplate, publisher, Dashboard.class, currentUserService, aclService, true );
//    }
//
    @Bean( "com.mass3d.program.ProgramExpressionStore" )
    public HibernateGenericStore<ProgramExpression> programExpressionStore()
    {
        return new HibernateGenericStore<>( sessionFactory, jdbcTemplate, publisher,
            ProgramExpression.class, true );
    }
//
//    @Bean( "com.mass3d.eventreport.EventReportStore" )
//    public HibernateAnalyticalObjectStore<EventReport> eventReportStore()
//    {
//        return new HibernateAnalyticalObjectStore<>( sessionFactory,
//            jdbcTemplate, publisher, EventReport.class, currentUserService, aclService, true );
//    }
//
//    @Bean( "com.mass3d.eventchart.EventChartStore" )
//    public HibernateAnalyticalObjectStore<EventChart> eventChartStore()
//    {
//        return new HibernateAnalyticalObjectStore<>( sessionFactory,
//            jdbcTemplate, publisher, EventChart.class, currentUserService, aclService, true );
//    }

    @Bean( "com.mass3d.program.notification.ProgramNotificationStore" )
    public HibernateIdentifiableObjectStore<ProgramNotificationTemplate> programNotificationStore()
    {
        return new HibernateIdentifiableObjectStore<>( sessionFactory, jdbcTemplate, publisher,
            ProgramNotificationTemplate.class, currentUserService, aclService, true );
    }

    @Bean( "com.mass3d.program.notification.ProgramNotificationInstanceStore" )
    public HibernateIdentifiableObjectStore<ProgramNotificationInstance> programNotificationInstanceStore()
    {
        return new HibernateIdentifiableObjectStore<>( sessionFactory, jdbcTemplate, publisher,
            ProgramNotificationInstance.class, currentUserService, aclService, true );
    }
}
