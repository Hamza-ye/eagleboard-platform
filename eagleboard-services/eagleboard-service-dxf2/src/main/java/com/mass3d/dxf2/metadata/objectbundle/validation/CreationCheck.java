package com.mass3d.dxf2.metadata.objectbundle.validation;

import java.util.List;
import com.mass3d.common.IdentifiableObject;
import com.mass3d.dxf2.metadata.objectbundle.ObjectBundle;
import com.mass3d.feedback.ErrorCode;
import com.mass3d.feedback.ErrorReport;
import com.mass3d.feedback.TypeReport;
import com.mass3d.importexport.ImportStrategy;

public class CreationCheck
    implements
    ValidationCheck
{
    @Override
    public TypeReport check( ObjectBundle bundle, Class<? extends IdentifiableObject> klass,
        List<IdentifiableObject> persistedObjects, List<IdentifiableObject> nonPersistedObjects,
        ImportStrategy importStrategy, ValidationContext ctx )
    {
        TypeReport typeReport = new TypeReport( klass );

        if ( persistedObjects == null || persistedObjects.isEmpty() )
        {
            return typeReport;
        }

        for ( IdentifiableObject identifiableObject : persistedObjects )
        {
            IdentifiableObject object = bundle.getPreheat().get( bundle.getPreheatIdentifier(), identifiableObject );

            if ( object != null && object.getId() > 0 )
            {
                ErrorReport errorReport = new ErrorReport( klass, ErrorCode.E5000, bundle.getPreheatIdentifier(),
                    bundle.getPreheatIdentifier().getIdentifiersWithName( identifiableObject ) )
                        .setMainId( identifiableObject.getUid() );

                ValidationUtils.addObjectReport( errorReport, typeReport, object, bundle );

                ctx.markForRemoval( object );
            }
        }

        return typeReport;
    }
}
