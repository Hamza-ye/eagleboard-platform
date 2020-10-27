package com.mass3d.commons.filter;

import java.util.Collection;
import java.util.Iterator;

/**
 * Utility class for collection filtering.
 *
 */
public class FilterUtils
{
    /**
     * Filters the given collection using the given {@link Filter}.
     *
     * @param <T> type.
     * @param collection the {@link Collection}.
     * @param filter the filter.
     * @param <V> the type of the collection members.
     * @return the filtered collection, null if any input parameter is null.
     */
    public static <T extends Collection<V>, V> T filter( T collection, Filter<V> filter )
    {
        if ( collection == null || filter == null )
        {
            return null;
        }

        final Iterator<V> iterator = collection.iterator();

        while ( iterator.hasNext() )
        {
            if ( !filter.retain( iterator.next() ) )
            {
                iterator.remove();
            }
        }

        return collection;
    }

    /**
     * Filters the given collection using the given {@link Filter} retaining only
     * items which does NOT pass the filter evaluation.
     *
     * @param <T> type.
     * @param collection the {@link Collection}.
     * @param filter the filter.
     * @param <V> the type of the collection members.
     * @return the inverse filtered collection, null if any input parameter is null.
     */
    public static <T extends Collection<V>, V> T inverseFilter( T collection, Filter<V> filter )
    {
        if ( collection == null || filter == null )
        {
            return null;
        }

        final Iterator<V> iterator = collection.iterator();

        while ( iterator.hasNext() )
        {
            if ( filter.retain( iterator.next() ) )
            {
                iterator.remove();
            }
        }

        return collection;
    }
}
