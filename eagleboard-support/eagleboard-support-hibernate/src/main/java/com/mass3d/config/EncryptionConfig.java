package com.mass3d.config;

import com.google.common.collect.ImmutableMap;
import javax.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import com.mass3d.hibernate.HibernateConfigurationProvider;
import com.mass3d.hibernate.encryption.HibernateEncryptorRegistry;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.salt.RandomSaltGenerator;
import org.jasypt.salt.StringFixedSaltGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EncryptionConfig
{
    @Autowired
    private HibernateConfigurationProvider hibernateConfigurationProvider;

    private String password;

    @PostConstruct
    public void init()
    {
        password = (String) getConnectionProperty( "encryption.password", "J7GhAs287hsSQlKd9g5" );
    }

    // Used only for SystemSettings (due to bug with JCE policy restrictions in Jasypt)
    @Bean( "tripleDesStringEncryptor" )
    public PooledPBEStringEncryptor tripleDesStringEncryptor()
    {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        encryptor.setAlgorithm( "PBEWithSHA1AndDESede" );
        encryptor.setPassword( password );
        encryptor.setPoolSize( 4 );
        encryptor.setSaltGenerator( new StringFixedSaltGenerator( "H7g0oLkEw3wf52fs52g3hbG" ) );

        return encryptor;
    }

    // AES string encryptor, requires BouncyCastle and JCE extended policy (due to
    // issue mentioned above)
    @Bean( "aes128StringEncryptor" )
    public PooledPBEStringEncryptor aes128StringEncryptor()
    {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        encryptor.setAlgorithm( "PBEWITHSHA256AND128BITAES-CBC-BC" );
        encryptor.setPassword( password );
        encryptor.setPoolSize( 4 );
        encryptor.setSaltGenerator( new RandomSaltGenerator() );

        return encryptor;
    }

    @Bean( "hibernateEncryptors" )
    public HibernateEncryptorRegistry hibernateEncryptors()
    {
        HibernateEncryptorRegistry registry = HibernateEncryptorRegistry.getInstance();
        registry.setEncryptors( ImmutableMap.of( "aes128StringEncryptor", aes128StringEncryptor() ) );
        return registry;
    }

    @Bean
    public MethodInvokingFactoryBean addProvider()
    {
        MethodInvokingFactoryBean methodInvokingFactoryBean = new MethodInvokingFactoryBean();
        methodInvokingFactoryBean.setStaticMethod( "java.security.Security.addProvider" );
        methodInvokingFactoryBean.setArguments( new BouncyCastleProvider() );
        return methodInvokingFactoryBean;
    }

    private Object getConnectionProperty( String key, String defaultValue )
    {
        String value = hibernateConfigurationProvider.getConfiguration().getProperty( key );

        return StringUtils.defaultIfEmpty( value, defaultValue );
    }
}
