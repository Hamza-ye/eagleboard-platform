package com.mass3d.cache;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.springframework.util.Assert.hasText;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * A redis backed implementation of {@link Cache}. This implementation uses a
 * shared redis cache server for any number of instances.
 * 
 */
public class RedisCache<V> implements Cache<V>
{
    private RedisTemplate<String, V> redisTemplate;

    private boolean refreshExpriryOnAccess;

    private long expiryInSeconds;

    private String cacheRegion;

    private V defaultValue;

    private boolean expiryEnabled;

    /**
     * Constructor for instantiating RedisCache.
     * 
     * @param cacheBuilder The cache builder instance
     */
    @SuppressWarnings( "unchecked" )
    public RedisCache( ExtendedCacheBuilder<V> cacheBuilder )
    {
        this.redisTemplate = (RedisTemplate<String, V>) cacheBuilder.getRedisTemplate();
        this.refreshExpriryOnAccess = cacheBuilder.isRefreshExpiryOnAccess();
        this.expiryInSeconds = cacheBuilder.getExpiryInSeconds();
        this.cacheRegion = cacheBuilder.getRegion();
        this.defaultValue = cacheBuilder.getDefaultValue();
        this.expiryEnabled = cacheBuilder.isExpiryEnabled();
    }

    @Override
    public Optional<V> getIfPresent( String key )
    {
        String redisKey = generateActualKey( key );
        if ( expiryEnabled && refreshExpriryOnAccess )
        {
            redisTemplate.expire( redisKey, expiryInSeconds, SECONDS );
        }
        return Optional.ofNullable( redisTemplate.boundValueOps( redisKey ).get() );
    }

    @Override
    public Optional<V> get( String key )
    {
        String redisKey = generateActualKey( key );
        if ( expiryEnabled && refreshExpriryOnAccess )
        {
            redisTemplate.expire( redisKey, expiryInSeconds, SECONDS );
        }
        return Optional
            .ofNullable( Optional.ofNullable( redisTemplate.boundValueOps( redisKey ).get() ).orElse( defaultValue ) );
    }

    @Override
    public Optional<V> get( String key, Function<String, V> mappingFunction )
    {
        if ( null == mappingFunction )
        {
            throw new IllegalArgumentException( "MappingFunction cannot be null" );
        }
        
        String redisKey = generateActualKey( key );
        
        if ( expiryEnabled && refreshExpriryOnAccess )
        {
            redisTemplate.expire( redisKey, expiryInSeconds, SECONDS );
        }
        
        V value = redisTemplate.boundValueOps( redisKey ).get();

        if ( null == value )
        {
            value = mappingFunction.apply( key );

            if ( null != value )
            {
                if ( expiryEnabled )
                {
                    redisTemplate.boundValueOps( redisKey ).set( value, expiryInSeconds, SECONDS );
                }
                else
                {
                    redisTemplate.boundValueOps( redisKey ).set( value );
                }
            }
        }

        return Optional.ofNullable( Optional.ofNullable( value ).orElse( defaultValue ) );
    }

    @Override
    public Collection<V> getAll()
    {
        Set<String> keySet = redisTemplate.keys( cacheRegion + "*" );
        return redisTemplate.opsForValue().multiGet( keySet );
    }

    @Override
    public void put( String key, V value )
    {
        if ( null == value )
        {
            throw new IllegalArgumentException( "Value cannot be null" );
        }
        
        String redisKey = generateActualKey( key );

        if ( expiryEnabled )
        {
            redisTemplate.boundValueOps( redisKey ).set( value, expiryInSeconds, SECONDS );
        }
        else
        {
            redisTemplate.boundValueOps( redisKey ).set( value );
        }
    }

    @Override
    public void put( String key, V value, long ttlInSeconds)
    {
        hasText( key, "Value cannot be null" );
        final String redisKey = generateActualKey( key );

        redisTemplate.boundValueOps( redisKey ).set( value, ttlInSeconds, SECONDS );
    }

    @Override
    public void invalidate( String key )
    {
        redisTemplate.delete( generateActualKey( key ) );
    }

    private String generateActualKey( String key )
    {
        return cacheRegion.concat( ":" ).concat( key );
    }

    @Override
    public void invalidateAll()
    {
        Set<String> keysToDelete = redisTemplate.keys( cacheRegion.concat( ":*" ) );
        redisTemplate.delete( keysToDelete );
    }

    @Override
    public CacheType getCacheType()
    {
        return CacheType.REDIS;
    }
}
