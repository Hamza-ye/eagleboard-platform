package com.mass3d.feedback;

import javax.annotation.Nonnull;
import com.mass3d.common.IdentifiableObject;

/**
 * Provides a zero based index for an object. The zero based unique index
 * may be unique based on a type.
 *
 */
@FunctionalInterface
public interface ObjectIndexProvider
{
    /**
     * Returns the object index for the specified object. If the
     * object has not yet an index, an index will be created.
     *
     * @param object the object for which an index should be returned.
     * @return the index of the specified object.
     */
    @Nonnull
    Integer mergeObjectIndex(@Nonnull IdentifiableObject object);
}
