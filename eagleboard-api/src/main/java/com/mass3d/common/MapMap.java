package com.mass3d.common;

import java.util.HashMap;
import java.util.Map;

public class MapMap<T, U, V>
    extends HashMap<T, Map<U, V>>
{
    public Map<U, V> putEntry( T key, U valueKey, V value )
    {
        Map<U, V> map = this.get( key );
        map = map == null ? new HashMap<>() : map;
        map.put( valueKey, value );
        return this.put( key, map );
    }

    public void putEntries( T key, Map<U, V> m )
    {
        Map<U, V> map = this.get( key );
        map = map == null ? new HashMap<>() : map;
        map.putAll( m );
        this.put( key, map );
    }

    public void putMap( MapMap<T, U, V> map )
    {
        for ( Entry<T, Map<U, V>> entry : map.entrySet() )
        {
            this.putEntries( entry.getKey(), entry.getValue() );
        }
    }

    public V getValue( T key, U valueKey )
    {
        return this.get( key ) == null ? null : this.get( key ).get( valueKey );
    }

    @SafeVarargs
    public static <T, U, V> MapMap<T, U, V> ofEntries( Entry<T, Map<U, V>>... entries )
    {
        MapMap<T, U, V> map = new MapMap<>();

        for ( Entry<T, Map<U, V>> entry : entries )
        {
            map.put( entry.getKey(), entry.getValue() );
        }

        return map;
    }
}
