package com.mass3d.dxf2.metadata.objectbundle;

import java.util.List;
import com.mass3d.common.IdentifiableObject;
import com.mass3d.feedback.ErrorReport;

/**
 * Contains hooks for object bundle commit phase.
 *
 */
public interface ObjectBundleHook
{
    /**
     * Hook to run custom validation code. Run before any other validation.
     *
     * @param object Object to validate
     * @param bundle Current validation phase bundle
     * @return Empty list if not errors, if errors then populated with one or more ErrorReports
     */
    <T extends IdentifiableObject> List<ErrorReport> validate(T object, ObjectBundle bundle);

    /**
     * Run before commit phase has started.
     *
     * @param bundle Current commit phase bundle
     */
    void preCommit(ObjectBundle bundle);

    /**
     * Run after commit phase has finished.
     *
     * @param bundle Current commit phase bundle
     */
    void postCommit(ObjectBundle bundle);

    /**
     * Run before a type import has started. I.e. run before importing orgUnits, dataElements, etc.
     *
     * @param bundle Current commit phase bundle
     */
    <T extends IdentifiableObject> void preTypeImport(Class<? extends IdentifiableObject> klass,
        List<T> objects, ObjectBundle bundle);

    /**
     * Run after a type import has finished. I.e. run before importing orgUnits, dataElements, etc.
     *
     * @param bundle Current commit phase bundle
     */
    <T extends IdentifiableObject> void postTypeImport(Class<? extends IdentifiableObject> klass,
        List<T> objects, ObjectBundle bundle);

    /**
     * Run before object has been created.
     *
     * @param bundle Current commit phase bundle
     */
    <T extends IdentifiableObject> void preCreate(T object, ObjectBundle bundle);

    /**
     * Run after object has been created.
     *
     * @param bundle Current commit phase bundle
     */
    <T extends IdentifiableObject> void postCreate(T persistedObject, ObjectBundle bundle);

    /**
     * Run before object has been updated.
     *
     * @param bundle Current commit phase bundle
     */
    <T extends IdentifiableObject> void preUpdate(T object, T persistedObject, ObjectBundle bundle);

    /**
     * Run after object has been updated.
     *
     * @param bundle Current commit phase bundle
     */
    <T extends IdentifiableObject> void postUpdate(T persistedObject, ObjectBundle bundle);

    /**
     * Run before object has been deleted.
     *
     * @param bundle Current commit phase bundle
     */
    <T extends IdentifiableObject> void preDelete(T persistedObject, ObjectBundle bundle);
}
