package com.mass3d.cache;

import java.util.Map;
import com.mass3d.external.conf.DhisConfigurationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * Provides cache builder to build instances.
 *
 *
 */
@Component( "cacheProvider" )
public class DefaultCacheProvider implements CacheProvider
{
    private DhisConfigurationProvider configurationProvider;

    private RedisTemplate<String, ?> redisTemplate;

    @Override
    public <V> ExtendedCacheBuilder<V> newCacheBuilder( Class<V> valueType )
    {
        return new ExtendedCacheBuilder<V>( redisTemplate, configurationProvider );
    }

    @Override
    public  <K,V> ExtendedCacheBuilder<Map<K,V>> newCacheBuilder( Class<K> keyType, Class<V> valueType )
    {
        return new ExtendedCacheBuilder<Map<K,V>>( redisTemplate, configurationProvider );
    }

    @Autowired
    public void setConfigurationProvider( DhisConfigurationProvider configurationProvider )
    {
        this.configurationProvider = configurationProvider;
    }

    @Autowired( required = false )
    public void setRedisTemplate( RedisTemplate<String, ?> redisTemplate )
    {
        this.redisTemplate = redisTemplate;
    }

}