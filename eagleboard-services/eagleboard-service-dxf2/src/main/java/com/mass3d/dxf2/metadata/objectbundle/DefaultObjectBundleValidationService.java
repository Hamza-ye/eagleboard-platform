package com.mass3d.dxf2.metadata.objectbundle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import com.mass3d.common.IdentifiableObject;
import com.mass3d.commons.timer.SystemTimer;
import com.mass3d.commons.timer.Timer;
import com.mass3d.dxf2.metadata.AtomicMode;
import com.mass3d.dxf2.metadata.objectbundle.feedback.ObjectBundleValidationReport;
import com.mass3d.dxf2.metadata.objectbundle.validation.ValidationFactory;
import com.mass3d.preheat.Preheat;
import com.mass3d.schema.SchemaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service( "com.mass3d.dxf2.metadata.objectbundle.ObjectBundleValidationService" )
@Transactional
public class DefaultObjectBundleValidationService
    implements
    ObjectBundleValidationService
{
    private final ValidationFactory validationFactory;
    private final SchemaService schemaService;

    public DefaultObjectBundleValidationService( ValidationFactory validationFactory, SchemaService schemaService )
    {
        this.schemaService = schemaService;
        this.validationFactory = validationFactory;
    }

    @Override
    public ObjectBundleValidationReport validate( ObjectBundle bundle )
    {
        Timer timer = new SystemTimer().start();

        ObjectBundleValidationReport validation = new ObjectBundleValidationReport();

        if ( ( bundle.getUser() == null || bundle.getUser().isSuper() ) && bundle.isSkipValidation() )
        {
            log.warn( "Skipping validation for metadata import by user '" +
                bundle.getUsername() + "'. Not recommended." );
            return validation;
        }

        List<Class<? extends IdentifiableObject>> klasses = getSortedClasses( bundle );

        for ( Class<? extends IdentifiableObject> klass : klasses )
        {
            List<IdentifiableObject> nonPersistedObjects = bundle.getObjects( klass, false );
            List<IdentifiableObject> persistedObjects = bundle.getObjects( klass, true );

            cleanDefaults( bundle.getPreheat(), nonPersistedObjects );
            cleanDefaults( bundle.getPreheat(), persistedObjects );

            // Validate the bundle by running the validation checks chain
            validation.addTypeReport( validationFactory.validateBundle( bundle, klass, persistedObjects,
                nonPersistedObjects ) );
        }

        validateAtomicity( bundle, validation );
        bundle.setObjectBundleStatus( ObjectBundleStatus.VALIDATED );

        log.info( "(" + bundle.getUsername() + ") Import:Validation took " + timer.toString() );

        return validation;
    }

    private void cleanDefaults( Preheat preheat, List<IdentifiableObject> objects )
    {
        objects.removeIf( preheat::isDefault );
    }

    // ----------------------------------------------------------------------------------------------------
    // Helpers
    // ----------------------------------------------------------------------------------------------------

    private void validateAtomicity( ObjectBundle bundle, ObjectBundleValidationReport validation )
    {
        if ( AtomicMode.NONE == bundle.getAtomicMode() )
        {
            return;
        }

        Map<Class<? extends IdentifiableObject>, List<IdentifiableObject>> nonPersistedObjects = bundle
            .getObjects( false );

        Map<Class<? extends IdentifiableObject>, List<IdentifiableObject>> persistedObjects = bundle.getObjects( true );

        if ( AtomicMode.ALL == bundle.getAtomicMode() )
        {
            if ( !validation.getErrorReports().isEmpty() )
            {
                nonPersistedObjects.clear();
                persistedObjects.clear();
            }
        }
    }

    @SuppressWarnings( "unchecked" )
    private List<Class<? extends IdentifiableObject>> getSortedClasses( ObjectBundle bundle )
    {
        List<Class<? extends IdentifiableObject>> klasses = new ArrayList<>();

        Map<Class<? extends IdentifiableObject>, List<IdentifiableObject>> objectMap = bundle.getObjectMap();

        schemaService.getMetadataSchemas().forEach( schema -> {
            Class<? extends IdentifiableObject> klass = (Class<? extends IdentifiableObject>) schema.getKlass();

            if ( objectMap.containsKey( klass ) )
            {
                klasses.add( klass );
            }
        } );

        return klasses;
    }
}
