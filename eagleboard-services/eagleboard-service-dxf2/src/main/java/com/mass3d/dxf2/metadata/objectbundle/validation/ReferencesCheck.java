package com.mass3d.dxf2.metadata.objectbundle.validation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import com.mass3d.common.EmbeddedObject;
import com.mass3d.common.IdentifiableObject;
import com.mass3d.dxf2.metadata.AtomicMode;
import com.mass3d.dxf2.metadata.objectbundle.ObjectBundle;
import com.mass3d.feedback.ErrorCode;
import com.mass3d.feedback.ObjectReport;
import com.mass3d.feedback.TypeReport;
import com.mass3d.importexport.ImportStrategy;
import com.mass3d.period.Period;
import com.mass3d.period.PeriodType;
import com.mass3d.preheat.Preheat;
import com.mass3d.preheat.PreheatErrorReport;
import com.mass3d.preheat.PreheatIdentifier;
import com.mass3d.schema.PropertyType;
import com.mass3d.schema.Schema;
import com.mass3d.system.util.ReflectionUtils;
import com.mass3d.user.User;
import com.mass3d.user.UserCredentials;

public class ReferencesCheck
    implements
    ValidationCheck
{
    @Override
    public TypeReport check( ObjectBundle bundle, Class<? extends IdentifiableObject> klass,
        List<IdentifiableObject> persistedObjects, List<IdentifiableObject> nonPersistedObjects,
        ImportStrategy importStrategy, ValidationContext ctx )
    {
        TypeReport typeReport = new TypeReport( klass );

        List<IdentifiableObject> objects = ValidationUtils.joinObjects( persistedObjects, nonPersistedObjects );

        if ( objects.isEmpty() )
        {
            return typeReport;
        }

        for ( IdentifiableObject object : objects )
        {
            List<PreheatErrorReport> errorReports = checkReferences( object, bundle.getPreheat(),
                bundle.getPreheatIdentifier(), bundle.isSkipSharing(), ctx );

            if ( errorReports.isEmpty() )
                continue;
            
            if ( object != null )
            {
                ObjectReport objectReport = new ObjectReport( object, bundle );
                objectReport.addErrorReports( errorReports );
                typeReport.addObjectReport( objectReport );
            }
        }

        if ( !typeReport.getErrorReports().isEmpty() && AtomicMode.ALL == bundle.getAtomicMode() )
        {
            typeReport.getStats().incIgnored();
        }

        return typeReport;
    }

    private List<PreheatErrorReport> checkReferences( IdentifiableObject object, Preheat preheat,
        PreheatIdentifier identifier, boolean skipSharing, ValidationContext ctx )
    {
        List<PreheatErrorReport> preheatErrorReports = new ArrayList<>();

        if ( object == null )
        {
            return preheatErrorReports;
        }

        Schema schema = ctx.getSchemaService().getDynamicSchema( object.getClass() );
        schema.getProperties().stream().filter( p -> p.isPersisted() && p.isOwner()
            && ( PropertyType.REFERENCE == p.getPropertyType() || PropertyType.REFERENCE == p.getItemPropertyType() ) )
            .forEach( p -> {
                if ( skipCheck( p.getKlass() ) || skipCheck( p.getItemKlass() ) )
                {
                    return;
                }

                if ( !p.isCollection() )
                {
                    IdentifiableObject refObject = ReflectionUtils.invokeMethod( object, p.getGetterMethod() );
                    IdentifiableObject ref = preheat.get( identifier, refObject );

                    if ( ref == null && refObject != null && !preheat.isDefault( refObject ) )
                    {
                        if ( !("user".equals( p.getName() ) && User.class.isAssignableFrom( p.getKlass() )
                            && skipSharing) )
                        {
                            preheatErrorReports.add( new PreheatErrorReport( identifier, object.getClass(),
                                ErrorCode.E5002, identifier.getIdentifiersWithName( refObject ),
                                identifier.getIdentifiersWithName( object ), p.getName() ) );
                        }
                    }
                }
                else
                {
                    Collection<IdentifiableObject> objects = ReflectionUtils.newCollectionInstance( p.getKlass() );
                    Collection<IdentifiableObject> refObjects = ReflectionUtils.invokeMethod( object,
                        p.getGetterMethod() );

                    for ( IdentifiableObject refObject : refObjects )
                    {
                        if ( preheat.isDefault( refObject ) )
                            continue;

                        IdentifiableObject ref = preheat.get( identifier, refObject );

                        if ( ref == null && refObject != null )
                        {
                            preheatErrorReports.add( new PreheatErrorReport( identifier, object.getClass(),
                                ErrorCode.E5002, identifier.getIdentifiersWithName( refObject ),
                                identifier.getIdentifiersWithName( object ), p.getCollectionName() ) );
                        }
                        else
                        {
                            objects.add( refObject );
                        }
                    }

                    ReflectionUtils.invokeMethod( object, p.getSetterMethod(), objects );
                }
            } );

//        if ( schema.havePersistedProperty( "attributeValues" ) )
//        {
//            object.getAttributeValues().stream()
//                .filter( attributeValue -> attributeValue.getAttribute() != null
//                    && preheat.get( identifier, attributeValue.getAttribute() ) == null )
//                .forEach(
//                    attributeValue -> preheatErrorReports.add( new PreheatErrorReport( identifier, object.getClass(),
//                        ErrorCode.E5002, identifier.getIdentifiersWithName( attributeValue.getAttribute() ),
//                        identifier.getIdentifiersWithName( object ), "attributeValues" ) ) );
//        }

        if ( schema.havePersistedProperty( "userGroupAccesses" ) )
        {
            object.getUserGroupAccesses().stream()
                .filter( userGroupAccess -> !skipSharing && userGroupAccess.getUserGroup() != null
                    && preheat.get( identifier, userGroupAccess.getUserGroup() ) == null )
                .forEach(
                    userGroupAccesses -> preheatErrorReports.add( new PreheatErrorReport( identifier, object.getClass(),
                        ErrorCode.E5002, identifier.getIdentifiersWithName( userGroupAccesses.getUserGroup() ),
                        identifier.getIdentifiersWithName( object ), "userGroupAccesses" ) ) );
        }

        if ( schema.havePersistedProperty( "userAccesses" ) )
        {
            object.getUserAccesses().stream()
                .filter( userGroupAccess -> !skipSharing && userGroupAccess.getUser() != null
                    && preheat.get( identifier, userGroupAccess.getUser() ) == null )
                .forEach( userAccesses -> preheatErrorReports.add( new PreheatErrorReport( identifier,
                    object.getClass(), ErrorCode.E5002, identifier.getIdentifiersWithName( userAccesses.getUser() ),
                    identifier.getIdentifiersWithName( object ), "userAccesses" ) ) );
        }


        return preheatErrorReports;
    }

    private boolean skipCheck( Class<?> klass )
    {
        return klass != null
            && (UserCredentials.class.isAssignableFrom( klass ) || EmbeddedObject.class.isAssignableFrom( klass )
                || Period.class.isAssignableFrom( klass ) || PeriodType.class.isAssignableFrom( klass ));
    }

}
