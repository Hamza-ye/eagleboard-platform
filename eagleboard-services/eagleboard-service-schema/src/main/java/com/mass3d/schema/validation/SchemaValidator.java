package com.mass3d.schema.validation;

import java.util.List;
import com.mass3d.feedback.ErrorReport;

public interface SchemaValidator
{
    /**
     * Validate embedded object against its schema, the object is required to be non-null and have a schema associated with it.
     *
     * @param object      Object to validate
     * @param parentClass Only include persisted properties
     * @return WebMessage   containing validation response
     */
    List<ErrorReport> validateEmbeddedObject(Object object, Class<?> parentClass);

    /**
     * Validate object against its schema, the object is required to be non-null and have a schema associated with it.
     *
     * @param object    Object to validate
     * @param persisted Only include persisted properties
     * @return WebMessage containing validation response
     */
    List<ErrorReport> validate(Object object, boolean persisted);

    /**
     * Validate object against its schema, the object is required to be non-null and have a schema associated with it.
     * <p>
     * Only persisted values will be checked.
     *
     * @param object Object to validate
     * @return WebMessage containing validation response
     */
    List<ErrorReport> validate(Object object);
}
