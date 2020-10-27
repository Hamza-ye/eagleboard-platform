package com.mass3d.webapi;

import com.mass3d.H2DhisConfigurationProvider;
import com.mass3d.config.EncryptionConfig;
import com.mass3d.config.HibernateConfig;
import com.mass3d.config.ServiceConfig;
import com.mass3d.config.StartupConfig;
import com.mass3d.config.StoreConfig;
import com.mass3d.configuration.NotifierConfiguration;
import com.mass3d.external.conf.DhisConfigurationProvider;
import com.mass3d.jdbc.config.JdbcConfig;
import com.mass3d.leader.election.LeaderElectionConfiguration;
import com.mass3d.security.config.DhisWebCommonsWebSecurityConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Configuration
@ImportResource( locations = { "classpath*:/META-INF/dhis/beans.xml" } )
@ComponentScan( basePackages = { "com.mass3d" }, useDefaultFilters = false, includeFilters = {
    @Filter( type = FilterType.ANNOTATION, value = Service.class ),
    @Filter( type = FilterType.ANNOTATION, value = Component.class ),
    @Filter( type = FilterType.ANNOTATION, value = Repository.class )

}, excludeFilters = @Filter( Configuration.class ) )
@Import( {
    JdbcConfig.class,
    HibernateConfig.class,
//    FlywayConfig.class,
    EncryptionConfig.class,
    ServiceConfig.class,
    StoreConfig.class,
    LeaderElectionConfiguration.class,
    NotifierConfiguration.class,
//    HttpConfig.class,
    DhisWebCommonsWebSecurityConfig.class,
    com.mass3d.setting.config.ServiceConfig.class,
    com.mass3d.external.config.ServiceConfig.class,
    com.mass3d.dxf2.config.ServiceConfig.class,
    com.mass3d.support.config.ServiceConfig.class,
//    com.mass3d.validation.config.ServiceConfig.class,
//    com.mass3d.validation.config.StoreConfig.class,
//    com.mass3d.programrule.config.ProgramRuleConfig.class,
//    com.mass3d.reporting.config.StoreConfig.class,
//    com.mass3d.analytics.config.ServiceConfig.class,
    com.mass3d.commons.config.JacksonObjectMapperConfig.class,

    StartupConfig.class
} )
@Transactional
public class WebTestConfiguration
{
    @Bean( name = "dhisConfigurationProvider" )
    public DhisConfigurationProvider dhisConfigurationProvider()
    {
        return new H2DhisConfigurationProvider();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public LdapAuthenticator ldapAuthenticator()
//    {
//        return authentication -> null;
//    }
//
//    @Bean
//    public LdapAuthoritiesPopulator ldapAuthoritiesPopulator()
//    {
//        return ( dirContextOperations, s ) -> null;
//    }

    @Bean( "oAuth2AuthenticationManager" )
    public AuthenticationManager oAuth2AuthenticationManager()
    {
        return authentication -> null;
    }

    @Bean( "authenticationManager" )
    @Primary
    public AuthenticationManager authenticationManager()
    {
        return authentication -> null;
    }

}
