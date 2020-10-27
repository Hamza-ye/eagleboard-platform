package com.mass3d.configuration;

import com.mass3d.condition.RedisEnabledCondition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.session.data.redis.config.ConfigureRedisAction;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * Configuration registered if {@link RedisEnabledCondition} matches to true.
 * Redis backed Spring Session will be configured due to the
 * {@link EnableRedisHttpSession} annotation.
 *
 */
@Configuration
@DependsOn( "dhisConfigurationProvider" )
@Conditional( RedisEnabledCondition.class )
@EnableRedisHttpSession
public class RedisSpringSessionConfiguration
{
    @Bean
    public static ConfigureRedisAction configureRedisAction()
    {
        return ConfigureRedisAction.NO_OP;
    }
}