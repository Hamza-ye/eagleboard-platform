package com.mass3d.commons.collection;

import java.util.Map;

/**
 * Class that wraps a map with Enum as the value type. Offers a getValue-method
 * that retrieves a value from the map by providing a key of type String. This
 * is convenient when rendering such a map in a view which cannot handle Java 5
 * enums.
 *
 */
public class EnumMapWrapper<E extends Enum<E>, T>
{
    private Class<E> enumType;

    private Map<E, T> map;

    public EnumMapWrapper( Class<E> enumType, Map<E, T> map )
    {
        this.enumType = enumType;
        this.map = map;
    }

    /**
     * Gets the value with the given key.
     *
     * @param key the key.
     * @return the value.
     */
    public T getValue( String key )
    {
        return map.get( Enum.valueOf( enumType, key ) );
    }
}
