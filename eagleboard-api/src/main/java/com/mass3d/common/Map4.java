package com.mass3d.common;

import java.util.HashMap;

public class Map4<R, S, T, U, V>
    extends HashMap<R, MapMapMap<S, T, U, V>>
{
    private static final long serialVersionUID = -2806972962597162013L;

    public MapMapMap<S, T, U, V> putEntry( R key1, S key2, T key3, U key4, V value )
    {
        MapMapMap<S, T, U, V> map = this.get( key1 );
        map = map == null ? new MapMapMap<>() : map;
        map.putEntry( key2, key3, key4, value );
        return this.put( key1, map );
    }

    public void putEntries( R key1, MapMapMap<S, T, U, V> m )
    {
        MapMapMap<S, T, U, V> map = this.get( key1 );
        map = map == null ? new MapMapMap<>() : map;
        map.putMap( m );
        this.put( key1, map );
    }

    public void putMap( Map4<R, S, T, U, V> map )
    {
        for ( Entry<R, MapMapMap<S, T, U, V>> entry : map.entrySet() )
        {
            this.putEntries( entry.getKey(), entry.getValue() );
        }
    }

    public V getValue( R key1, S key2, T key3, U key4 )
    {
        return this.get( key1 ) == null ? null : this.get( key1 ).getValue( key2, key3, key4 );
    }

    @SafeVarargs
    public static <R, S, T, U, V> Map4<R, S, T, U, V> ofEntries( Entry<R, MapMapMap<S, T, U, V>>... entries )
    {
        Map4<R, S, T, U, V> map = new Map4<>();

        for ( Entry<R, MapMapMap<S, T, U, V>> entry : entries )
        {
            map.put( entry.getKey(), entry.getValue() );
        }

        return map;
    }
}
