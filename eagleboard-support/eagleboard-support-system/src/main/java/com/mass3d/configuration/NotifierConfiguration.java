package com.mass3d.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mass3d.condition.RedisDisabledCondition;
import com.mass3d.condition.RedisEnabledCondition;
import com.mass3d.system.notification.InMemoryNotifier;
import com.mass3d.system.notification.Notifier;
import com.mass3d.system.notification.RedisNotifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * This class deals with the configuring an appropriate notifier depending on
 * whether redis is enabled or not.
 *
 */
@Configuration
public class NotifierConfiguration
{
    @Autowired( required = false )
    private RedisTemplate<?, ?> redisTemplate;

    @SuppressWarnings( "unchecked" )
    @Bean
    @Qualifier( "notifier" )
    @Conditional( RedisEnabledCondition.class )
    public Notifier redisNotifier( ObjectMapper objectMapper )
    {
        return new RedisNotifier( (RedisTemplate<String, String>) redisTemplate, objectMapper );
    }

    @Bean
    @Qualifier( "notifier" )
    @Conditional( RedisDisabledCondition.class )
    public Notifier inMemoryNotifier()
    {
        return new InMemoryNotifier();
    }
}