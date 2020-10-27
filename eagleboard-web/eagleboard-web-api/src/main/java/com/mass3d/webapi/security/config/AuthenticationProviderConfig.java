package com.mass3d.webapi.security.config;


import com.mass3d.external.conf.ConfigurationKey;
import com.mass3d.external.conf.DhisConfigurationProvider;
import com.mass3d.security.AuthenticationLoggerListener;
import com.mass3d.security.oauth2.DefaultClientDetailsUserDetailsService;
import com.mass3d.security.spring2fa.TwoFactorAuthenticationProvider;
import com.mass3d.security.spring2fa.TwoFactorWebAuthenticationDetailsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@Order( 910 )
@ComponentScan( basePackages = { "com.mass3d" } )
@EnableWebSecurity
public class AuthenticationProviderConfig
{
    @Autowired
    private DhisConfigurationProvider configurationProvider;

    @Autowired
    TwoFactorAuthenticationProvider twoFactorAuthenticationProvider;

    @Autowired
    DefaultClientDetailsUserDetailsService defaultClientDetailsUserDetailsService;

    @Autowired
    public void configureGlobal( @Lazy AuthenticationManagerBuilder auth )
        throws Exception
    {
        auth.authenticationProvider( twoFactorAuthenticationProvider );
//        auth.authenticationProvider( customLdapAuthenticationProvider() );
    }

//    @Bean
//    public TwoFactorWebAuthenticationDetailsSource twoFactorWebAuthenticationDetailsSource()
//    {
//        return new TwoFactorWebAuthenticationDetailsSource();
//    }


    // Ldap configs
//    @Bean
//    CustomLdapAuthenticationProvider customLdapAuthenticationProvider()
//    {
//        return new CustomLdapAuthenticationProvider( dhisBindAuthenticator(),
//            userDetailsServiceLdapAuthoritiesPopulator( defaultClientDetailsUserDetailsService ),
//            configurationProvider );
//    }
//
//    @Bean
//    public DefaultSpringSecurityContextSource defaultSpringSecurityContextSource()
//    {
//        DefaultSpringSecurityContextSource defaultSpringSecurityContextSource = new DefaultSpringSecurityContextSource(
//            configurationProvider.getProperty( ConfigurationKey.LDAP_URL ) );
//        defaultSpringSecurityContextSource
//            .setUserDn( configurationProvider.getProperty( ConfigurationKey.LDAP_MANAGER_DN ) );
//        defaultSpringSecurityContextSource
//            .setPassword( configurationProvider.getProperty( ConfigurationKey.LDAP_MANAGER_PASSWORD ) );
//
//        return defaultSpringSecurityContextSource;
//    }

//    @Bean
//    public FilterBasedLdapUserSearch filterBasedLdapUserSearch()
//    {
//        return new FilterBasedLdapUserSearch( configurationProvider.getProperty( ConfigurationKey.LDAP_SEARCH_BASE ),
//            configurationProvider.getProperty( ConfigurationKey.LDAP_SEARCH_FILTER ),
//            defaultSpringSecurityContextSource() );
//    }

//    @Bean
//    @DependsOn( "com.mass3d.user.UserService" )
//    public DhisBindAuthenticator dhisBindAuthenticator()
//    {
//        DhisBindAuthenticator dhisBindAuthenticator = new DhisBindAuthenticator( defaultSpringSecurityContextSource() );
//        dhisBindAuthenticator.setUserSearch( filterBasedLdapUserSearch() );
//        return dhisBindAuthenticator;
//    }

//    @Bean
//    public UserDetailsServiceLdapAuthoritiesPopulator userDetailsServiceLdapAuthoritiesPopulator(
//        UserDetailsService userDetailsService )
//    {
//        return new UserDetailsServiceLdapAuthoritiesPopulator( userDetailsService );
//    }

    @Bean
    public DefaultAuthenticationEventPublisher authenticationEventPublisher()
    {
        return new DefaultAuthenticationEventPublisher();
    }

    @Bean
    public AuthenticationLoggerListener authenticationLoggerListener()
    {
        return new AuthenticationLoggerListener();
    }

//    @Bean
//    public AuthenticationListener authenticationListener()
//    {
//        return new AuthenticationListener();
//    }
}
