package com.mass3d.security.config;

import com.google.common.collect.ImmutableSet;
import com.mass3d.schema.SchemaService;
import com.mass3d.security.SecurityService;
import com.mass3d.security.SpringSecurityActionAccessResolver;
import com.mass3d.security.authority.CachingSystemAuthoritiesProvider;
import com.mass3d.security.authority.CompositeSystemAuthoritiesProvider;
import com.mass3d.security.authority.DefaultRequiredAuthoritiesProvider;
import com.mass3d.security.authority.DetectingSystemAuthoritiesProvider;
import com.mass3d.security.authority.RequiredAuthoritiesProvider;
import com.mass3d.security.authority.SchemaAuthoritiesProvider;
import com.mass3d.security.authority.SimpleSystemAuthoritiesProvider;
import com.mass3d.security.authority.SystemAuthoritiesProvider;
import com.mass3d.security.intercept.XWorkSecurityInterceptor;
import com.mass3d.security.spring2fa.TwoFactorAuthenticationProvider;
import com.mass3d.startup.DefaultAdminUserPopulator;
import com.mass3d.user.CurrentUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDecisionManager;

@Configuration
@Order( 3200 )
public class AuthoritiesProviderConfig
{

    @Autowired
    private SecurityService securityService;

//    @Autowired
//    private ModuleManager moduleManager;

    @Autowired
    private SchemaService schemaService;

//    @Autowired
//    private AppManager appManager;

    @Autowired
    @Qualifier( "com.mass3d.user.CurrentUserService" )
    public CurrentUserService currentUserService;

    @Autowired
    @Qualifier( "accessDecisionManager" )
    public AccessDecisionManager accessDecisionManager;

    @Autowired
    public TwoFactorAuthenticationProvider twoFactorAuthenticationProvider;

//    @Autowired
//    @Qualifier( "com.mass3d.organisationunit.OrganisationUnitService" )
//    public OrganisationUnitService organisationUnitService;

    @Bean( "com.mass3d.security.authority.SystemAuthoritiesProvider" )
    public SystemAuthoritiesProvider systemAuthoritiesProvider()
    {
        SchemaAuthoritiesProvider schemaAuthoritiesProvider = new SchemaAuthoritiesProvider( schemaService );
//        AppsSystemAuthoritiesProvider appsSystemAuthoritiesProvider = new AppsSystemAuthoritiesProvider( appManager );

        DetectingSystemAuthoritiesProvider detectingSystemAuthoritiesProvider = new DetectingSystemAuthoritiesProvider();
        detectingSystemAuthoritiesProvider.setRequiredAuthoritiesProvider( requiredAuthoritiesProvider() );

        CompositeSystemAuthoritiesProvider provider = new CompositeSystemAuthoritiesProvider();
        provider.setSources( ImmutableSet.of(
            new CachingSystemAuthoritiesProvider( detectingSystemAuthoritiesProvider ),
//            new CachingSystemAuthoritiesProvider( moduleSystemAuthoritiesProvider() ),
            new CachingSystemAuthoritiesProvider( simpleSystemAuthoritiesProvider() ),
            schemaAuthoritiesProvider
//            , appsSystemAuthoritiesProvider
        ) );
        return provider;
    }

    private SystemAuthoritiesProvider simpleSystemAuthoritiesProvider()
    {
        SimpleSystemAuthoritiesProvider provider = new SimpleSystemAuthoritiesProvider();
        provider.setAuthorities( DefaultAdminUserPopulator.ALL_AUTHORITIES );
        return provider;
    }

    @Bean
    public RequiredAuthoritiesProvider requiredAuthoritiesProvider()
    {
        DefaultRequiredAuthoritiesProvider provider = new DefaultRequiredAuthoritiesProvider();
        provider.setRequiredAuthoritiesKey( "requiredAuthorities" );
        provider.setAnyAuthoritiesKey( "anyAuthorities" );
        provider.setGlobalAttributes( ImmutableSet.of( "M_MODULE_ACCESS_VOTER_ENABLED" ) );
        return provider;
    }

//    private ModuleSystemAuthoritiesProvider moduleSystemAuthoritiesProvider()
//    {
//        ModuleSystemAuthoritiesProvider provider = new ModuleSystemAuthoritiesProvider();
//        provider.setAuthorityPrefix( "M_" );
//        provider.setModuleManager( moduleManager );
//        provider.setExcludes( ImmutableSet.of(
//            "dhis-web-commons-menu",
//            "dhis-web-commons-menu-management",
//            "dhis-web-commons-oust",
//            "dhis-web-commons-ouwt",
//            "dhis-web-commons-security",
//            "dhis-web-commons-i18n",
//            "dhis-web-commons-ajax",
//            "dhis-web-commons-ajax-json",
//            "dhis-web-commons-ajax-html",
//            "dhis-web-commons-stream",
//            "dhis-web-commons-help",
//            "dhis-web-commons-about",
//            "dhis-web-apps",
//            "dhis-web-api-mobile",
//            "dhis-web-portal"
//        ) );
//        return provider;
//    }

    @Bean( "com.mass3d.security.intercept.XWorkSecurityInterceptor" )
    public XWorkSecurityInterceptor xWorkSecurityInterceptor()
        throws Exception
    {
        DefaultRequiredAuthoritiesProvider provider = new DefaultRequiredAuthoritiesProvider();
        provider.setRequiredAuthoritiesKey( "requiredAuthorities" );
        provider.setAnyAuthoritiesKey( "anyAuthorities" );
        provider.setGlobalAttributes( ImmutableSet.of( "M_MODULE_ACCESS_VOTER_ENABLED" ) );

        SpringSecurityActionAccessResolver resolver = new SpringSecurityActionAccessResolver();
        resolver.setRequiredAuthoritiesProvider( provider );
        resolver.setAccessDecisionManager( accessDecisionManager );

        XWorkSecurityInterceptor interceptor = new XWorkSecurityInterceptor();
        interceptor.setAccessDecisionManager( accessDecisionManager );
        interceptor.setValidateConfigAttributes( false );
        interceptor.setRequiredAuthoritiesProvider( provider );
        interceptor.setActionAccessResolver( resolver );
        interceptor.setSecurityService( securityService );

        return interceptor;
    }

//    @Bean( "com.mass3d.security.intercept.LoginInterceptor" )
//    public LoginInterceptor loginInterceptor()
//    {
//        RestrictOrganisationUnitsAction unitsAction = new RestrictOrganisationUnitsAction();
//        unitsAction.setCurrentUserService( currentUserService );
//        DefaultOrganisationUnitSelectionManager selectionManager = new DefaultOrganisationUnitSelectionManager();
//        selectionManager.setOrganisationUnitService( organisationUnitService );
//        unitsAction.setSelectionManager( selectionManager );
//        DefaultSelectionTreeManager selectionTreeManager = new DefaultSelectionTreeManager();
//        selectionTreeManager.setOrganisationUnitService( organisationUnitService );
//        unitsAction.setSelectionTreeManager( selectionTreeManager );
//
//        LoginInterceptor interceptor = new LoginInterceptor();
//        interceptor.setActions( ImmutableList.of( unitsAction ) );
//
//        return interceptor;
//    }
}
