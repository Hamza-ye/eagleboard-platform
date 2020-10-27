package com.mass3d.commons.collection;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Utility methods for operations on various collections.
 *
 */
public class CollectionUtils
{
    public static final String[] STRING_ARR = new String[0];

    /**
     * Returns the intersection of the given Collections.
     *
     * @param c1  the first Collection.
     * @param c2  the second Collection.
     * @param <T> the type.
     * @return the intersection of the Collections.
     */
    public static <T> Collection<T> intersection( Collection<T> c1, Collection<T> c2 )
    {
        Set<T> set1 = new HashSet<>( c1 );
        set1.retainAll( new HashSet<>( c2 ) );
        return set1;
    }

    /**
     * Searches for and returns the first string which starts with the given
     * prefix. Removes the match from the collection.
     *
     * @param collection the collection.
     * @param prefix     the string prefix.
     * @return a string, or null if no matches.
     */
    public static String popStartsWith( Collection<String> collection, String prefix )
    {
        Iterator<String> iterator = collection.iterator();

        while ( iterator.hasNext() )
        {
            String element = iterator.next();

            if ( element != null && element.startsWith( prefix ) )
            {
                iterator.remove();
                return element;
            }
        }

        return null;
    }

    /**
     * Applies the given consumer to each item in the given collection after filtering
     * out null items.
     *
     * @param collection the collection.
     * @param consumer the consumer.
     */
    public static <E> void nullSafeForEach( Collection<E> collection, Consumer<E> consumer )
    {
        collection.stream()
            .filter( Objects::nonNull )
            .forEach( consumer );
    }

    /**
     * Returns an empty set if the given set is null, if not returns the set.
     *
     * @param set the set.
     * @return a non-null set.
     */
    public static <T> Set<T> emptyIfNull( Set<T> set )
    {
        return set != null ? set : new HashSet<>();
    }

    /**
     * Adds all items not already present in the target collection
     *
     * @param collection collection to add items to.
     * @param items collection of items to add.
     */
    public static <E> void addAllUnique( Collection<E> collection, Collection<E> items )
    {
        items.stream()
            .filter( item -> !collection.contains( item ) )
            .forEach( item -> collection.add( item ) );
    }
}
