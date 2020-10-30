package com.mass3d;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import org.apache.struts2.dispatcher.filter.StrutsPrepareAndExecuteFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.support.OpenSessionInViewFilter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
//@ImportResource({"classpath*:META-INF/mass3d/servlet.xml"})
//@EntityScan(basePackages = "com.mass3d")
//@ComponentScan(basePackages = {"com.mass3d"})
//@ComponentScan(basePackages = { "com.mass3d.webapi" }, excludeFilters = { @Filter(type = FilterType.ANNOTATION, value = Configuration.class) })
//@ImportResource({"classpath*:META-INF/mass3d/beans.xml"})
//@EnableTransactionManagement
@EnableTransactionManagement//(proxyTargetClass = true)
public class WebApiConfiguration2 {

//  @Bean
//  public FilterRegistrationBean registerOpenSessionInViewFilterBean() {
//    FilterRegistrationBean registrationBean = new FilterRegistrationBean();
//    OpenSessionInViewFilter filter = new OpenSessionInViewFilter();
//    registrationBean.setFilter(filter);
//    registrationBean.addUrlPatterns("/*");
//    registrationBean.setOrder(5);
//    return registrationBean;
//  }
//  @Bean
//  public FilterRegistrationBean filterRegistrationBean() {
//    FilterRegistrationBean registrationBean = new FilterRegistrationBean();
//    StrutsPrepareAndExecuteFilter struts = new StrutsPrepareAndExecuteFilter();
//    registrationBean.setFilter(struts);
//    registrationBean.addUrlPatterns("*.action");
//    registrationBean.setOrder(1);
//    return registrationBean;
//  }
//
//  @Bean
//  public FilterRegistrationBean filter1() {
//
//    FilterRegistrationBean registration = new FilterRegistrationBean();
//    registration.setFilter(openSessionInViewFilter());
//    registration.addUrlPatterns("*.action");
////    registration.addInitParameter("paramName", "paramValue");
//    registration.setName("OpenSessionInViewFilter");
////    registration.setOrder(1);
//    registration.setAsyncSupported(true);
//    return registration;
//  }
//
//  @Bean
//  public FilterRegistrationBean filter2() {
//
//    FilterRegistrationBean registration = new FilterRegistrationBean();
//    registration.setFilter(openSessionInViewFilter());
//    registration.addUrlPatterns("/api/*");
////    registration.addInitParameter("paramName", "paramValue");
//    registration.setName("OpenSessionInViewFilter");
////    registration.setOrder(1);
//    registration.setAsyncSupported(true);
//    return registration;
//  }
//
//  @Bean(name = "OpenSessionInViewFilter")
//  public Filter openSessionInViewFilter() {
////    OpenSessionInViewFilter openSessionInViewFilter = new OpenSessionInViewFilter();
//    return new OpenSessionInViewFilter();
//  }
}
