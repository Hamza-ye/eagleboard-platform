package com.mass3d.schema;

/**
 * Interface for converting from one object to another. The main use case being supporting
 * property transformers, these are transformers that are applied BEFORE any field filtering
 * takes place, and hides the original object completely.
 *
 * The main reason for using a property transformer is to either collapse an object (a deeply nested
 * object can be replaced with a more relevant one), or to smooth the transition when planning domain
 * object changes (this so the frontend can start working on the new payloads before all internals are
 * finished refactoring).
 *
 */
public interface PropertyTransformer extends Klass
{
    Object transform(Object o);
}
