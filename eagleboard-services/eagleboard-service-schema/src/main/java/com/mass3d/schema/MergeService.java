package com.mass3d.schema;

public interface MergeService
{
    /**
     * Merges source object into target object, requires a "schema friendly" class.
     *
     * @param mergeParams MergeParams instance containing source and target object
     */
    <T> T merge(MergeParams<T> mergeParams);

    /**
     * Creates a clone of given object and returns it.
     *
     * @param source Object to clone
     */
    <T> T clone(T source);
}
