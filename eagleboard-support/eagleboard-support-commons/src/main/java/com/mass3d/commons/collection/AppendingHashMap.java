package com.mass3d.commons.collection;

import java.util.HashMap;

/**
 * HashMap that appends the content of the value the existing content when
 * inserted, or inserts as usual if no entries match the key.
 *
 */
public class AppendingHashMap<K, V>
    extends HashMap<K, String>
{
    /**
     * Determines if a de-serialized file is compatible with this class.
     */
    private static final long serialVersionUID = 2579976271277075017L;

    /**
     * Appends the content of the value the existing content when
     * inserted, or inserts as usual if no entries match the key.
     *
     * @param key the key.
     * @param value the value.
     * @return the new value.
     */
    @Override
    public String put( K key, String value )
    {
        final String existing = super.get( key );

        value = existing != null ? existing + value : value;

        super.put( key, value );

        return existing;
    }
}
