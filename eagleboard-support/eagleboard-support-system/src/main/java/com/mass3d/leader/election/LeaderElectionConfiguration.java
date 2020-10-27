package com.mass3d.leader.election;

import com.mass3d.condition.RedisDisabledCondition;
import com.mass3d.condition.RedisEnabledCondition;
import com.mass3d.external.conf.ConfigurationKey;
import com.mass3d.external.conf.ConfigurationPropertyFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Configures leaderManager that takes care of node leader elections. 
 * 
 *
 */
@Configuration
public class LeaderElectionConfiguration
{
    @Autowired( required = false )
    private RedisTemplate<String, ?> redisTemplate;

    @Bean
    @Qualifier( "leaderTimeToLive" )
    public ConfigurationPropertyFactoryBean leaderTimeToLive()
    {
        return new ConfigurationPropertyFactoryBean( ConfigurationKey.LEADER_TIME_TO_LIVE );
    }

    @Bean
    @Qualifier( "leaderManager" )
    @Conditional( RedisEnabledCondition.class )
    public LeaderManager redisLeaderManager()
    {
        return new RedisLeaderManager( Long.parseLong( (String) leaderTimeToLive().getObject() ), redisTemplate );
    }

    @Bean
    @Qualifier( "leaderManager" )
    @Conditional( RedisDisabledCondition.class )
    public LeaderManager noOpLeaderManager()
    {
        return new NoOpLeaderManager();
    }

}