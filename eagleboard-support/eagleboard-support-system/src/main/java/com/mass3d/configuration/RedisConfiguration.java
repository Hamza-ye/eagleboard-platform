package com.mass3d.configuration;

import com.mass3d.condition.RedisEnabledCondition;
import com.mass3d.external.conf.ConfigurationKey;
import com.mass3d.external.conf.ConfigurationPropertyFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration.LettuceClientConfigurationBuilder;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Configuration registered if {@link RedisEnabledCondition} matches to true.
 * This class deals with the configuration properties for establishing
 * connection to a redis server.
 *
 *
 */
@Configuration
@DependsOn( "dhisConfigurationProvider" )
@Conditional( RedisEnabledCondition.class )
public class RedisConfiguration
{
    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory()
    {
        LettuceClientConfigurationBuilder builder = LettuceClientConfiguration.builder();
        if(Boolean.parseBoolean( (String) redisSslEnabled().getObject()))
        {
            builder.useSsl();
        }
        LettuceClientConfiguration clientConfiguration = builder.build();
        RedisStandaloneConfiguration standaloneConfig = new RedisStandaloneConfiguration();
        standaloneConfig.setHostName( (String) redisHost().getObject() );
        standaloneConfig.setPassword( (String) redisPassword().getObject() );
        standaloneConfig.setPort( Integer.parseInt( (String) redisPort().getObject() ) );
        return new LettuceConnectionFactory( standaloneConfig, clientConfiguration );
    }

    @Bean
    public ConfigurationPropertyFactoryBean redisHost()
    {
        return new ConfigurationPropertyFactoryBean( ConfigurationKey.REDIS_HOST );
    }

    @Bean
    public ConfigurationPropertyFactoryBean redisPort()
    {
        return new ConfigurationPropertyFactoryBean( ConfigurationKey.REDIS_PORT );
    }

    @Bean
    public ConfigurationPropertyFactoryBean redisPassword()
    {
        return new ConfigurationPropertyFactoryBean( ConfigurationKey.REDIS_PASSWORD );
    }

    @Bean
    public ConfigurationPropertyFactoryBean redisSslEnabled()
    {
        return new ConfigurationPropertyFactoryBean( ConfigurationKey.REDIS_USE_SSL );
    }

    @Bean
    public RedisTemplate<?, ?> redisTemplate()
    {
        RedisTemplate<?, ?> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory( lettuceConnectionFactory() );
        redisTemplate.setKeySerializer( new StringRedisSerializer() );
        return redisTemplate;
    }

}