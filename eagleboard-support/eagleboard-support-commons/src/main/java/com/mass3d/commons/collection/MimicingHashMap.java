package com.mass3d.commons.collection;

import java.util.HashMap;

/**
 * HashMap that returns as value the given key.
 *
 */
public class MimicingHashMap<K, V>
    extends HashMap<K, V>
{
    /**
     * Returns the given key.
     *
     * @param key the key.
     * @return the given key.
     */
    @Override
    @SuppressWarnings( "unchecked" )
    public V get( Object key )
    {
        return (V) key;
    }
}
