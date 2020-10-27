package com.mass3d.dxf2.metadata.objectbundle.validation;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.mass3d.common.IdentifiableObject;
import com.mass3d.dxf2.metadata.objectbundle.ObjectBundle;
import com.mass3d.feedback.ErrorCode;
import com.mass3d.feedback.ErrorReport;
import com.mass3d.feedback.TypeReport;
import com.mass3d.importexport.ImportStrategy;

public class DuplicateIdsCheck
    implements
    ValidationCheck
{

    @Override
    public TypeReport check( ObjectBundle bundle, Class<? extends IdentifiableObject> klass,
        List<IdentifiableObject> persistedObjects, List<IdentifiableObject> nonPersistedObjects,
        ImportStrategy importStrategy, ValidationContext ctx )
    {
        TypeReport typeReport = new TypeReport( klass );

        if ( persistedObjects.isEmpty() && nonPersistedObjects.isEmpty() )
        {
            return typeReport;
        }

        Map<Class<?>, String> idMap = new HashMap<>();

        typeReport.merge( run( typeReport, bundle, persistedObjects.iterator(), idMap, ctx ) );
        typeReport.merge( run( typeReport, bundle, nonPersistedObjects.iterator(), idMap, ctx ) );

        return typeReport;
    }

    private TypeReport run( TypeReport typeReport, ObjectBundle bundle, Iterator<IdentifiableObject> iterator,
        Map<Class<?>, String> idMap, ValidationContext context )
    {
        while ( iterator.hasNext() )
        {
            IdentifiableObject object = iterator.next();

            if ( idMap.containsKey( object.getClass() ) && idMap.get( object.getClass() ).equals( object.getUid() ) )
            {
                ErrorReport errorReport = new ErrorReport( object.getClass(), ErrorCode.E5004, object.getUid(),
                    object.getClass() ).setMainId( object.getUid() ).setErrorProperty( "id" );

                ValidationUtils.addObjectReport( errorReport, typeReport, object, bundle );
                context.markForRemoval( object );
            }
            else
            {
                idMap.put( object.getClass(), object.getUid() );
            }
        }

        return typeReport;
    }
}
