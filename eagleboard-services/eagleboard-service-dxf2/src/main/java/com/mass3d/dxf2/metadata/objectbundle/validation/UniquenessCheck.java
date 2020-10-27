package com.mass3d.dxf2.metadata.objectbundle.validation;

import static com.mass3d.dxf2.metadata.objectbundle.validation.ValidationUtils.addObjectReports;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.mass3d.common.IdentifiableObject;
import com.mass3d.dxf2.metadata.objectbundle.ObjectBundle;
import com.mass3d.feedback.ErrorCode;
import com.mass3d.feedback.ErrorReport;
import com.mass3d.feedback.TypeReport;
import com.mass3d.importexport.ImportStrategy;
import com.mass3d.preheat.Preheat;
import com.mass3d.preheat.PreheatIdentifier;
import com.mass3d.schema.Property;
import com.mass3d.schema.Schema;
import com.mass3d.system.util.ReflectionUtils;
import com.mass3d.user.User;

public class UniquenessCheck
    implements
    ValidationCheck
{

    @Override
    public TypeReport check( ObjectBundle bundle, Class<? extends IdentifiableObject> klass,
        List<IdentifiableObject> persistedObjects, List<IdentifiableObject> nonPersistedObjects,
        ImportStrategy importStrategy, ValidationContext ctx )
    {
        TypeReport typeReport = new TypeReport( klass );

        List<IdentifiableObject> objects = selectObjects( persistedObjects, nonPersistedObjects, importStrategy );

        if ( objects.isEmpty() )
        {
            return typeReport;
        }

        for ( IdentifiableObject object : objects )
        {
            List<ErrorReport> errorReports = new ArrayList<>();

            if ( object instanceof User )
            {
                User user = (User) object;
                errorReports.addAll( checkUniqueness( user, bundle.getPreheat(), bundle.getPreheatIdentifier(), ctx ) );
                errorReports.addAll( checkUniqueness( user.getUserCredentials(), bundle.getPreheat(),
                    bundle.getPreheatIdentifier(), ctx ) );
            }
            else
            {
                errorReports = checkUniqueness( object, bundle.getPreheat(), bundle.getPreheatIdentifier(), ctx );
            }

            if ( !errorReports.isEmpty() )
            {
                addObjectReports( errorReports, typeReport, object, bundle );
                ctx.markForRemoval( object );
            }
        }

        return typeReport;

    }

    private List<ErrorReport> checkUniqueness( IdentifiableObject object, Preheat preheat, PreheatIdentifier identifier,
        ValidationContext ctx )
    {
        List<ErrorReport> errorReports = new ArrayList<>();

        if ( object == null || preheat.isDefault( object ) )
            return errorReports;

        if ( !preheat.getUniquenessMap().containsKey( object.getClass() ) )
        {
            preheat.getUniquenessMap().put( object.getClass(), new HashMap<>() );
        }

        Map<String, Map<Object, String>> uniquenessMap = preheat.getUniquenessMap().get( object.getClass() );

        Schema schema = ctx.getSchemaService().getDynamicSchema( object.getClass() );
        List<Property> uniqueProperties = schema.getProperties().stream()
            .filter( p -> p.isPersisted() && p.isOwner() && p.isUnique() && p.isSimple() )
            .collect( Collectors.toList() );

        uniqueProperties.forEach( property -> {
            if ( !uniquenessMap.containsKey( property.getName() ) )
            {
                uniquenessMap.put( property.getName(), new HashMap<>() );
            }

            Object value = ReflectionUtils.invokeMethod( object, property.getGetterMethod() );

            if ( value != null )
            {
                String objectIdentifier = uniquenessMap.get( property.getName() ).get( value );

                if ( objectIdentifier != null )
                {
                    if ( !identifier.getIdentifier( object ).equals( objectIdentifier ) )
                    {
                        errorReports.add( new ErrorReport( object.getClass(), ErrorCode.E5003, property.getName(),
                            value, identifier.getIdentifiersWithName( object ), objectIdentifier ).setMainId( objectIdentifier )
                            .setErrorProperty( property.getName() ) );
                    }
                }
                else
                {
                    uniquenessMap.get( property.getName() ).put( value, identifier.getIdentifier( object ) );
                }
            }
        } );

        return errorReports;
    }

}
