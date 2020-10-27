package com.mass3d.feedback;

import java.util.IdentityHashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import com.mass3d.common.IdentifiableObject;

/**
 * Container with objects where the index of the object can be retrieved by the
 * object itself.
 *
 */
public class IndexedObjectContainer implements ObjectIndexProvider
{
    private final Map<IdentifiableObject, Integer> objectsIndexMap = new IdentityHashMap<>();

    @Nonnull
    @Override
    public Integer mergeObjectIndex( @Nonnull IdentifiableObject object )
    {
        return add( object );
    }

    /**
     * @param object the identifiable object that should be checked.
     * @return <code>true</code> if the object is included in the container, <code>false</code> otherwise.
     */
    public boolean containsObject( @Nonnull IdentifiableObject object )
    {
        return objectsIndexMap.containsKey( object );
    }

    /**
     * Adds an object to the container of indexed objects. If the object has
     * already an index assigned, that will not be changed.
     *
     * @param object the object to which an index should be assigned.
     * @return the resulting zero based index of the added object in the container.
     */
    @Nonnull
    protected Integer add( @Nonnull IdentifiableObject object )
    {
        final Integer newIndex = objectsIndexMap.size();
        final Integer existingIndex = objectsIndexMap.putIfAbsent( object, newIndex );
        return ( existingIndex == null ) ? newIndex : existingIndex;
    }
}
