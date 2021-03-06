package com.mass3d.security.config;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mass3d.external.conf.ConfigurationKey;
import com.mass3d.external.conf.DhisConfigurationProvider;
import com.mass3d.i18n.I18nManager;
import com.mass3d.security.MappedRedirectStrategy;
import com.mass3d.security.spring2fa.TwoFactorWebAuthenticationDetailsSource;
import com.mass3d.security.vote.ActionAccessVoter;
import com.mass3d.security.vote.LogicalOrAccessDecisionManager;
import com.mass3d.security.vote.ModuleAccessVoter;
import com.mass3d.security.vote.SimpleAccessVoter;
import com.mass3d.webapi.filter.CorsFilter;
import com.mass3d.webapi.filter.CustomAuthenticationFilter;
import com.mass3d.webapi.handler.CustomExceptionMappingAuthenticationFailureHandler;
import com.mass3d.webapi.handler.DefaultAuthenticationSuccessHandler;
import com.mass3d.webapi.security.Http401LoginUrlAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.annotation.Order;
import org.springframework.mobile.device.DeviceResolver;
import org.springframework.mobile.device.LiteDeviceResolver;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.Arrays;
import java.util.List;

import static com.mass3d.webapi.security.config.DhisWebApiWebSecurityConfig.setHttpHeaders;

@Configuration
@Order( 2000 )
@ImportResource( locations = { "classpath*:/META-INF/mass3d/beans.xml"
//    , "classpath*:/META-INF/mass3d/beans-dataentry.xml",
//    "classpath*:/META-INF/mass3d/beans-maintenance-mobile.xml", "classpath*:/META-INF/mass3d/beans-approval.xml"
} )
public class DhisWebCommonsWebSecurityConfig
{
    /**
     * This configuration class is responsible for setting up the session management.
     */
    @Configuration
    @Order( 3300 )
    public static class SessionWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter
    {
        @Bean
        public static SessionRegistryImpl sessionRegistry()
        {
            return new SessionRegistryImpl();
        }

        @Override
        protected void configure( HttpSecurity http )
            throws Exception
        {
            http
                .sessionManagement()
                .sessionCreationPolicy( SessionCreationPolicy.ALWAYS )
                .enableSessionUrlRewriting( false )
                .maximumSessions( 10 )
                .expiredUrl( "/dhis-web-commons-security/logout.action" )
                .sessionRegistry( sessionRegistry() );
        }
    }

    /**
     * This configuration class is responsible for setting up the form login and everything related to the web pages.
     */
    @Configuration
    @Order( 2200 )
    public static class FormLoginWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter
    {
        @Autowired
        private TwoFactorWebAuthenticationDetailsSource twoFactorWebAuthenticationDetailsSource;

        @Autowired
        private I18nManager i18nManager;

        @Autowired
        private DhisConfigurationProvider configurationProvider;

//        @Autowired
//        private ExternalAccessVoter externalAccessVoter;

        @Override
        public void configure( WebSecurity web )
            throws Exception
        {
            super.configure( web );
            web
                .ignoring()
                .antMatchers( "/dhis-web-commons/javascripts/**" )
                .antMatchers( "/dhis-web-commons/css/**" )
                .antMatchers( "/dhis-web-commons/flags/**" )
                .antMatchers( "/dhis-web-commons/fonts/**" )
                .antMatchers( "/dhis-web-commons/i18nJavaScript.action" )
                .antMatchers( "/api/files/style/external" )
                .antMatchers( "/external-static/**" )
                .antMatchers( "/favicon.ico" );
        }

        @Override
        protected void configure( HttpSecurity http )
            throws Exception
        {
            http
                .authorizeRequests()

                .accessDecisionManager( accessDecisionManager() )

                .requestMatchers( analyticsPluginResources() ).permitAll()

                .antMatchers( "/dhis-web-commons/security/*" ).permitAll()
                .antMatchers( "/oauth2/**" ).permitAll()
                .antMatchers( "/dhis-web-dashboard/**" ).hasAnyAuthority( "ALL", "M_dhis-web-dashboard" )
                .antMatchers( "/dhis-web-pivot/**" ).hasAnyAuthority( "ALL", "M_dhis-web-pivot" )
                .antMatchers( "/dhis-web-visualizer/**" ).hasAnyAuthority( "ALL", "M_dhis-web-visualizer" )
                .antMatchers( "/dhis-web-data-visualizer/**" ).hasAnyAuthority( "ALL", "M_dhis-web-data-visualizer" )
                .antMatchers( "/dhis-web-mapping/**" ).hasAnyAuthority( "ALL", "M_dhis-web-mapping" )
                .antMatchers( "/dhis-web-maps/**" ).hasAnyAuthority( "ALL", "M_dhis-web-maps" )
                .antMatchers( "/dhis-web-event-reports/**" ).hasAnyAuthority( "ALL", "M_dhis-web-event-reports" )
                .antMatchers( "/dhis-web-event-visualizer/**" ).hasAnyAuthority( "ALL", "M_dhis-web-event-visualizer" )
                .antMatchers( "/dhis-web-interpretation/**" ).hasAnyAuthority( "ALL", "M_dhis-web-interpretation" )
                .antMatchers( "/dhis-web-settings/**" ).hasAnyAuthority( "ALL", "M_dhis-web-settings" )
                .antMatchers( "/dhis-web-maintenance/**" ).hasAnyAuthority( "ALL", "M_dhis-web-maintenance" )
                .antMatchers( "/dhis-web-app-management/**" ).hasAnyAuthority( "ALL", "M_dhis-web-app-management" )
                .antMatchers( "/dhis-web-usage-analytics/**" ).hasAnyAuthority( "ALL", "M_dhis-web-usage-analytics" )
                .antMatchers( "/dhis-web-event-capture/**" ).hasAnyAuthority( "ALL", "M_dhis-web-event-capture" )
                .antMatchers( "/dhis-web-tracker-capture/**" ).hasAnyAuthority( "ALL", "M_dhis-web-tracker-capture" )
                .antMatchers( "/dhis-web-cache-cleaner/**" ).hasAnyAuthority( "ALL", "M_dhis-web-cache-cleaner" )
                .antMatchers( "/dhis-web-data-administration/**" )
                .hasAnyAuthority( "ALL", "M_dhis-web-data-administration" )
                .antMatchers( "/dhis-web-data-quality/**" ).hasAnyAuthority( "ALL", "M_dhis-web-data-quality" )
                .antMatchers( "/dhis-web-messaging/**" ).hasAnyAuthority( "ALL", "M_dhis-web-messaging" )
                .antMatchers( "/dhis-web-datastore/**" ).hasAnyAuthority( "ALL", "M_dhis-web-datastore" )
                .antMatchers( "/dhis-web-scheduler/**" ).hasAnyAuthority( "ALL", "M_dhis-web-scheduler" )
                .antMatchers( "/dhis-web-sms-configuration/**" ).hasAnyAuthority( "ALL", "M_dhis-web-sms-configuration" )
                .antMatchers( "/dhis-web-user/**" ).hasAnyAuthority( "ALL", "M_dhis-web-user" )

                .antMatchers( "/**" ).authenticated()
                .and()

                .formLogin()
                .authenticationDetailsSource( twoFactorWebAuthenticationDetailsSource )
                .loginPage( "/dhis-web-commons/security/login.action" )
                .usernameParameter( "j_username" ).passwordParameter( "j_password" )
                .loginProcessingUrl( "/dhis-web-commons-security/login.action" )
                .failureHandler( authenticationFailureHandler() )
                .successHandler( authenticationSuccessHandler() )
                .permitAll()
                .and()

                .logout()
                .logoutUrl( "/dhis-web-commons-security/logout.action" )
                .logoutSuccessUrl( "/" )
                .deleteCookies( "JSESSIONID" )
                .permitAll()
                .and()

                .exceptionHandling()
                .authenticationEntryPoint( entryPoint() )

                .and()

                .csrf()
                .disable()

                .addFilterBefore( CorsFilter.get(), BasicAuthenticationFilter.class )
                .addFilterBefore( CustomAuthenticationFilter.get(), UsernamePasswordAuthenticationFilter.class );

            setHttpHeaders( http );
        }

        @Bean
        public Http401LoginUrlAuthenticationEntryPoint entryPoint()
        {
            // Converts to a HTTP basic login if  "XMLHttpRequest".equals( request.getHeader( "X-Requested-With" ) )
            return new Http401LoginUrlAuthenticationEntryPoint( "/dhis-web-commons/security/login.action" );
        }

        @Bean
        public DefaultAuthenticationSuccessHandler authenticationSuccessHandler()
        {
            DefaultAuthenticationSuccessHandler successHandler = new DefaultAuthenticationSuccessHandler();
            successHandler.setRedirectStrategy( mappedRedirectStrategy() );
            if ( configurationProvider.getProperty( ConfigurationKey.SYSTEM_SESSION_TIMEOUT ) != null )
            {
                successHandler.setSessionTimeout(
                    Integer.parseInt( configurationProvider.getProperty( ConfigurationKey.SYSTEM_SESSION_TIMEOUT ) ) );
            }

            return successHandler;
        }

        @Bean
        public MappedRedirectStrategy mappedRedirectStrategy()
        {
            MappedRedirectStrategy mappedRedirectStrategy = new MappedRedirectStrategy();
            mappedRedirectStrategy.setRedirectMap( ImmutableMap.of( "/dhis-web-commons-stream/ping.action", "/" ) );
            mappedRedirectStrategy.setDeviceResolver( deviceResolver() );

            return mappedRedirectStrategy;
        }

        @Bean
        public CustomExceptionMappingAuthenticationFailureHandler authenticationFailureHandler()
        {
            CustomExceptionMappingAuthenticationFailureHandler handler =
                new CustomExceptionMappingAuthenticationFailureHandler( i18nManager );

            // Handles the special case when a user failed to login because it has expired...
            handler.setExceptionMappings(
                ImmutableMap.of(
                    "org.springframework.security.authentication.CredentialsExpiredException",
                    "/dhis-web-commons/security/expired.action" ) );

            handler.setDefaultFailureUrl( "/dhis-web-commons/security/login.action?failed=true" );

            return handler;
        }

        @Bean
        public DeviceResolver deviceResolver()
        {
            return new LiteDeviceResolver();
        }

        @Bean
        public RequestMatcher analyticsPluginResources()
        {
            String pattern = ".*(dhis-web-mapping\\/map.js|dhis-web-visualizer\\/chart.js|dhis-web-maps\\" +
                "/map.js|dhis-web-event-reports\\/eventreport.js|dhis-web-event-visualizer\\/eventchart.js|dhis-web-pivot\\/reporttable.js)";

            return new org.springframework.security.web.util.matcher.RegexRequestMatcher( pattern, "GET" );
        }

        @Bean
        public ModuleAccessVoter moduleAccessVoter()
        {
            ModuleAccessVoter voter = new ModuleAccessVoter();
            voter.setAttributePrefix( "M_" );
            voter.setAlwaysAccessible( ImmutableSet.of(
                "dhis-web-commons-menu",
                "dhis-web-commons-oust",
                "dhis-web-commons-ouwt",
                "dhis-web-commons-security",
                "dhis-web-commons-i18n",
                "dhis-web-commons-ajax",
                "dhis-web-commons-ajax-json",
                "dhis-web-commons-ajax-html",
                "dhis-web-commons-stream",
                "dhis-web-commons-help",
                "dhis-web-commons-about",
                "dhis-web-menu-management",
                "dhis-web-apps",
                "dhis-web-api-mobile",
                "dhis-web-portal",
                "dhis-web-uaa"
            ) );
            return voter;
        }

        @Bean
        public ActionAccessVoter actionAccessVoter()
        {
            ActionAccessVoter voter = new ActionAccessVoter();
            voter.setAttributePrefix( "F_" );
            voter.setRequiredAuthoritiesKey( "requiredAuthorities" );
            voter.setAnyAuthoritiesKey( "anyAuthorities" );
            return voter;
        }

        @Bean
        public WebExpressionVoter webExpressionVoter()
        {
            DefaultWebSecurityExpressionHandler h = new DefaultWebSecurityExpressionHandler();
            h.setDefaultRolePrefix( "" );
            WebExpressionVoter voter = new WebExpressionVoter();
            voter.setExpressionHandler( h );
            return voter;
        }

        @Bean( "accessDecisionManager" )
        public LogicalOrAccessDecisionManager accessDecisionManager()
        {
            List<AccessDecisionManager> decisionVoters = Arrays.asList(
                new UnanimousBased( ImmutableList.of( new SimpleAccessVoter( "ALL" ) ) ),
                new UnanimousBased( ImmutableList.of( actionAccessVoter(), moduleAccessVoter() ) ),
                new UnanimousBased( ImmutableList.of( webExpressionVoter() ) ),
//                new UnanimousBased( ImmutableList.of( externalAccessVoter ) ),
                new UnanimousBased( ImmutableList.of( new AuthenticatedVoter() ) )
            );
            return new LogicalOrAccessDecisionManager( decisionVoters );
        }
    }
}