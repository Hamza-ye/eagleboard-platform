package com.mass3d.cache;

import lombok.extern.slf4j.Slf4j;
import com.mass3d.external.conf.ConfigurationKey;
import com.mass3d.external.conf.DhisConfigurationProvider;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * A Builder class that helps in building Cache instances. Sensible defaults are
 * in place which can be modified with a fluent builder api.
 * 
 *
 * @param <V> The Value type to be stored in cache
 */
@Slf4j
public class ExtendedCacheBuilder<V> extends SimpleCacheBuilder<V>
{
    private DhisConfigurationProvider configurationProvider;

    private RedisTemplate<String, ?> redisTemplate;

    private boolean forceInMemory;
    
    public ExtendedCacheBuilder( RedisTemplate<String, ?> redisTemplate, DhisConfigurationProvider configurationProvider )
    {
        super();
        this.configurationProvider = configurationProvider;
        this.redisTemplate = redisTemplate;
        this.forceInMemory = false;
    }


    /**
     * Configure the cache instance to use local inmemory storage even in clustered or standalone environment.
     * Ideally used in scenarios where stale data is not critical and faster lookup is preferred.
     * 
     * @return The builder instance.
     */
    @Override
    public CacheBuilder<V> forceInMemory()
    {
        this.forceInMemory = true;
        return this;
    }
    
    /**
     * Creates and returns a cacheInstance based on the system configuration and
     * the cache builder parameters. If {@code maximumSize} is 0 then a
     * NoOpCache instance will be returned which does not cache anything. This
     * can be used during system testings where cache has to be disabled. If
     * {@code maximumSize} is greater than 0 than based on {@code redis.enabled}
     * property in dhis.conf, either Redis backed implementation
     * {@link RedisCache} will be returned or a Local Caffeine backed cache
     * implementation {@link LocalCache} will be returned. For Local cache,
     * every instance created using this method will be logically separate and
     * will not share any state. However, when using Redis Cache, every instance
     * created using this method will use the same redis store.
     * 
     * @return A cache instance based on the system configuration and input
     *         parameters. Returns one of {@link RedisCache}, {@link LocalCache}
     *         or {@link NoOpCache}
     */
    @Override
    public Cache<V> build()
    {
        if ( getMaximumSize() == 0 || isDisabled() )
        {
            log.info( String.format( "NoOp Cache instance created for region:'%s'", getRegion() ) );
            return new NoOpCache<V>( this );
        }
        else if ( forceInMemory )
        {
            log.info( String
                .format( "Local Cache (forced) instance created for region:'%s'", getRegion() ) );
            return new LocalCache<V>( this );
        }
        else if ( configurationProvider.getProperty( ConfigurationKey.REDIS_ENABLED ).equalsIgnoreCase( "true" ) )
        {
            log.info( String.format( "Redis Cache instance created for region:'%s'", getRegion() ) );
            return new RedisCache<V>( this );
        }
        else
        {
            log.info( String.format( "Local Cache instance created for region:'%s'", getRegion() ) );
            return new LocalCache<V>( this );
        }
    }

    public RedisTemplate<String, ?> getRedisTemplate()
    {
        return redisTemplate;
    }
}
