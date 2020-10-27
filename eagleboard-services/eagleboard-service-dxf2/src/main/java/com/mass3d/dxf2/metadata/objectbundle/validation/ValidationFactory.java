package com.mass3d.dxf2.metadata.objectbundle.validation;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import com.mass3d.common.IdentifiableObject;
import com.mass3d.dxf2.metadata.objectbundle.ObjectBundle;
import com.mass3d.dxf2.metadata.objectbundle.ObjectBundleHook;
import com.mass3d.feedback.TypeReport;
import com.mass3d.importexport.ImportStrategy;
import com.mass3d.schema.SchemaService;
import com.mass3d.schema.validation.SchemaValidator;
import com.mass3d.security.acl.AclService;
import com.mass3d.user.UserService;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ValidationFactory
{
    private final SchemaValidator schemaValidator;

    private final SchemaService schemaService;

    private final AclService aclService;

    private final UserService userService;

    private final Map<ImportStrategy, List<Class<? extends ValidationCheck>>> validatorMap;

    private List<ObjectBundleHook> objectBundleHooks;

    public ValidationFactory( SchemaValidator schemaValidator, SchemaService schemaService, AclService aclService,
        UserService userService, List<ObjectBundleHook> objectBundleHooks,
        Map<ImportStrategy, List<Class<? extends ValidationCheck>>> validatorMap )
    {
        this.schemaValidator = schemaValidator;
        this.schemaService = schemaService;
        this.aclService = aclService;
        this.userService = userService;
        this.validatorMap = validatorMap;
        this.objectBundleHooks = objectBundleHooks == null ? Collections.emptyList() : objectBundleHooks;
    }

    /**
     * Run the validation checks against the bundle
     *
     * @param bundle an {@see ObjectBundle}
     * @param klass the Class type that is getting validated
     * @param persistedObjects a List of IdentifiableObject
     * @param nonPersistedObjects a List of IdentifiableObject
     *
     * @return a {@see TypeReport} containing the outcome of the validation
     */
    public TypeReport validateBundle( ObjectBundle bundle, Class<? extends IdentifiableObject> klass,
        List<IdentifiableObject> persistedObjects, List<IdentifiableObject> nonPersistedObjects )
    {
        ValidationContext ctx = getContext();
        TypeReport typeReport = new ValidationRunner( validatorMap.get( bundle.getImportMode() ) )
            .executeValidationChain( bundle, klass, persistedObjects, nonPersistedObjects, ctx );

        // Remove invalid objects from the bundle
        removeFromBundle( klass, ctx, bundle );

        return addStatistics( typeReport, bundle, persistedObjects, nonPersistedObjects );
    }

    private TypeReport addStatistics( TypeReport typeReport, ObjectBundle bundle,
        List<IdentifiableObject> persistedObjects, List<IdentifiableObject> nonPersistedObjects )
    {
        if ( bundle.getImportMode().isCreateAndUpdate() )
        {
            typeReport.getStats().incCreated( nonPersistedObjects.size() );
            typeReport.getStats().incUpdated( persistedObjects.size() );
        }
        else if ( bundle.getImportMode().isCreate() )
        {
            typeReport.getStats().incCreated( nonPersistedObjects.size() );

        }
        else if ( bundle.getImportMode().isUpdate() )
        {
            typeReport.getStats().incUpdated( persistedObjects.size() );

        }
        else if ( bundle.getImportMode().isDelete() )
        {
            typeReport.getStats().incDeleted( persistedObjects.size() );
        }

        return typeReport;
    }

    /**
     *
     * @param klass the class of the objects to remove from bundle
     * @param ctx the {@see ValidationContext} containing the list of objects to
     *        remove
     * @param bundle the {@see ObjectBundle}
     */
    private void removeFromBundle( Class<? extends IdentifiableObject> klass, ValidationContext ctx,
        ObjectBundle bundle )
    {
        List<IdentifiableObject> persisted = bundle.getObjects( klass, true );
        persisted.removeAll( ctx.getMarkedForRemoval() );

        List<IdentifiableObject> nonPersisted = bundle.getObjects( klass, false );
        nonPersisted.removeAll( ctx.getMarkedForRemoval() );
    }

    private ValidationContext getContext()
    {
        return new ValidationContext( this.objectBundleHooks, this.schemaValidator, this.aclService, this.userService,
            this.schemaService );
    }

    static class ValidationRunner
    {
        private List<Class<? extends ValidationCheck>> validators;

        public ValidationRunner( List<Class<? extends ValidationCheck>> validators )
        {
            this.validators = validators;
        }

        public TypeReport executeValidationChain( ObjectBundle bundle, Class<? extends IdentifiableObject> klass,
            List<IdentifiableObject> persistedObjects, List<IdentifiableObject> nonPersistedObjects,
            ValidationContext ctx )
        {
            TypeReport typeReport = new TypeReport( klass );
            for ( Class<? extends ValidationCheck> validator : validators )
            {
                try
                {
                    ValidationCheck validationCheck = validator.newInstance();
                    typeReport.merge( validationCheck.check( bundle, klass, persistedObjects, nonPersistedObjects,
                        bundle.getImportMode(), ctx ) );
                }
                catch ( InstantiationException | IllegalAccessException e )
                {
                    log.error( "An error occurred during metadata import validation", e );
                }
            }
            return typeReport;
        }
    }
}
