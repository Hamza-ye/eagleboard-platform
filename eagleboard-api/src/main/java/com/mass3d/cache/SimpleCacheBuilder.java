package com.mass3d.cache;

import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

/**
 * A Builder class that helps in building Cache instances. Sensible defaults are
 * in place which can be modified with a fluent builder api.
 * 
 *
 * @param <V> The Value type to be stored in cache
 */
@Slf4j
public class SimpleCacheBuilder<V> implements CacheBuilder<V>
{
    private long maximumSize;
    
    private int initialCapacity;

    private String region;

    private boolean refreshExpiryOnAccess;

    private long expiryInSeconds;

    private V defaultValue;

    private boolean expiryEnabled;

    private boolean disabled;

    public SimpleCacheBuilder()
    {
        // Applying sensible defaults explicitly
        this.maximumSize = -1;
        this.region = "default";
        this.refreshExpiryOnAccess = false;
        this.expiryInSeconds = 0;
        this.defaultValue = null;
        this.expiryEnabled = false;
        this.disabled = false;
        this.initialCapacity = 16;
    }
    
    public CacheBuilder<V> withMaximumSize( long maximumSize )
    {
        if ( maximumSize < 0 )
        {
            throw new IllegalArgumentException( "MaximumSize cannot be negative" );
        }
        this.maximumSize = maximumSize;
        return this;
    }
    
    public CacheBuilder<V> withInitialCapacity( int initialCapacity )
    {
        if ( initialCapacity < 0 )
        {
            throw new IllegalArgumentException( "InitialCapacity cannot be negative" );
        }
        this.initialCapacity = initialCapacity;
        return this;
    }

    public CacheBuilder<V> forRegion( String region )
    {
        if ( region == null )
        {
            throw new IllegalArgumentException( "Region cannot be null" );
        }
        this.region = region;
        return this;
    }

    public CacheBuilder<V> expireAfterAccess( long duration, TimeUnit timeUnit )
    {
        if ( timeUnit == null )
        {
            throw new IllegalArgumentException( "TimeUnit cannot be null" );
        }
        this.expiryInSeconds = timeUnit.toSeconds( duration );
        this.refreshExpiryOnAccess = true;
        this.expiryEnabled = true;
        return this;
    }

    public CacheBuilder<V> expireAfterWrite( long duration, TimeUnit timeUnit )
    {
        if ( timeUnit == null )
        {
            throw new IllegalArgumentException( "TimeUnit cannot be null" );
        }
        this.expiryInSeconds = timeUnit.toSeconds( duration );
        this.refreshExpiryOnAccess = false;
        this.expiryEnabled = true;
        return this;
    }

    public CacheBuilder<V> withDefaultValue( V defaultValue )
    {
        this.defaultValue = defaultValue;
        return this;
    }

    public CacheBuilder<V> disabled()
    {
        this.disabled = true;
        return this;
    }

    /**
     * Creates and returns a {@link LocalCache}. If {@code maximumSize} is 0 or {@code disabled} is true then a
     * NoOpCache instance will be returned which does not cache anything.
     * 
     * @return A cache instance based on the input
     *         parameters. Returns one of {@link LocalCache}
     *         or {@link NoOpCache}
     */
    public Cache<V> build()
    {
        if ( maximumSize == 0 || disabled )
        {
            log.info( String.format( "NoOp Cache instance created for region:'%s'", region ) );
            return new NoOpCache<V>( this );
        }
        else
        {
            log.info( String.format( "Simple Local Cache instance created for region:'%s'", region ) );
            return new LocalCache<V>( this );
        }
    }

    public long getMaximumSize()
    {
        return maximumSize;
    }
    
    public int getInitialCapacity()
    {
        return initialCapacity;
    }

    public String getRegion()
    {
        return region;
    }

    public boolean isRefreshExpiryOnAccess()
    {
        return refreshExpiryOnAccess;
    }

    public boolean isExpiryEnabled()
    {
        return expiryEnabled;
    }
    
    public boolean isDisabled()
    {
        return disabled;
    }

    public long getExpiryInSeconds()
    {
        return expiryInSeconds;
    }

    public V getDefaultValue()
    {
        return defaultValue;
    }

    public CacheBuilder<V> forceInMemory()
    {
        return this;
    }
}
