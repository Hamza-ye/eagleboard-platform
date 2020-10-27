package com.mass3d.dxf2.metadata.objectbundle.validation;

import java.util.Collections;
import java.util.List;
import com.mass3d.common.IdentifiableObject;
import com.mass3d.dxf2.metadata.objectbundle.ObjectBundle;
import com.mass3d.feedback.TypeReport;
import com.mass3d.importexport.ImportStrategy;

/**
 * This interface is implemented by classes that can validate an {@see ObjectBundle}
 *
 */
public interface ValidationCheck
{
    /**
     * Execute a validation check against the {@link ObjectBundle}
     *
     * @param bundle an {@link ObjectBundle} to validate
     * @param klass the class of Object to validate, within the bundle
     * @param persistedObjects a List of IdentifiableObject
     * @param nonPersistedObjects a List of IdentifiableObject
     * @param importStrategy the {@link ImportStrategy}
     * @param context a {@link ValidationContext} containing the services required
     *         for validation
     *
     * @return a {@link TypeReport}
     */
    TypeReport check(ObjectBundle bundle, Class<? extends IdentifiableObject> klass,
        List<IdentifiableObject> persistedObjects, List<IdentifiableObject> nonPersistedObjects,
        ImportStrategy importStrategy, ValidationContext context);

    default List<IdentifiableObject> selectObjects(List<IdentifiableObject> persistedObjects,
        List<IdentifiableObject> nonPersistedObjects, ImportStrategy importStrategy)
    {

        if ( importStrategy.isCreateAndUpdate() )
        {
            return ValidationUtils.joinObjects( persistedObjects, nonPersistedObjects );
        }
        else if ( importStrategy.isCreate() )
        {
            return nonPersistedObjects;
        }
        else if ( importStrategy.isUpdate() )
        {
            return persistedObjects;
        }
        else
        {
            return Collections.emptyList();
        }
    }
}
