package com.mass3d.commons.filter;

/**
 * Filter interface.
 *
 */
public interface Filter<T>
{
    /**
     * Indicates whether the given object to should be retained in the list.
     *
     * @param object the object.
     * @return true if object should be retained.
     */
    boolean retain(T object);
}
