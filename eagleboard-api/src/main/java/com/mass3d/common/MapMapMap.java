package com.mass3d.common;

import java.util.HashMap;

public class MapMapMap<S, T, U, V>
    extends HashMap<S, MapMap<T, U, V>>
{
    /**
     * Determines if a de-serialized file is compatible with this class.
     */
    private static final long serialVersionUID = 4505153475282323148L;

    public MapMap<T, U, V> putEntry( S key1, T key2, U key3, V value )
    {
        MapMap<T, U, V> map = this.get( key1 );
        map = map == null ? new MapMap<>() : map;
        map.putEntry( key2, key3, value );
        return this.put( key1, map );
    }

    public void putEntries( S key1, MapMap<T, U, V> m )
    {
        MapMap<T, U, V> map = this.get( key1 );
        map = map == null ? new MapMap<>() : map;
        map.putMap( m );
        this.put( key1, map );
    }

    public void putMap( MapMapMap<S, T, U, V> map )
    {
        for ( Entry<S, MapMap<T, U, V>> entry : map.entrySet() )
        {
            this.putEntries( entry.getKey(), entry.getValue() );
        }
    }

    public V getValue( S key1, T key2, U key3 )
    {
        return this.get( key1 ) == null ? null : this.get( key1 ).getValue( key2, key3 );
    }

    @SafeVarargs
    public static <S, T, U, V> MapMapMap<S, T, U, V> ofEntries( Entry<S, MapMap<T, U, V>>... entries )
    {
        MapMapMap<S, T, U, V> map = new MapMapMap<>();

        for ( Entry<S, MapMap<T, U, V>> entry : entries )
        {
            map.put( entry.getKey(), entry.getValue() );
        }

        return map;
    }
}
