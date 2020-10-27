package com.mass3d.dxf2.metadata.objectbundle.validation;

import java.util.List;
import com.mass3d.common.IdentifiableObject;
import com.mass3d.dxf2.metadata.objectbundle.ObjectBundle;
import com.mass3d.feedback.ErrorCode;
import com.mass3d.feedback.ErrorReport;
import com.mass3d.feedback.TypeReport;
import com.mass3d.importexport.ImportStrategy;

public class DummyCheck
    implements
    ValidationCheck
{

    @Override
    public TypeReport check( ObjectBundle bundle, Class<? extends IdentifiableObject> klass,
        List<IdentifiableObject> persistedObjects, List<IdentifiableObject> nonPersistedObjects,
        ImportStrategy importStrategy, ValidationContext context )
    {

        TypeReport typeReport = new TypeReport( klass );

        for ( IdentifiableObject nonPersistedObject : nonPersistedObjects )
        {
            if ( nonPersistedObject.getUid().startsWith( "u" ) )
            {

                ErrorReport errorReport = new ErrorReport( klass, ErrorCode.E5000, bundle.getPreheatIdentifier(),
                    bundle.getPreheatIdentifier().getIdentifiersWithName( nonPersistedObject ) )
                        .setMainId( nonPersistedObject.getUid() );
                ValidationUtils.addObjectReport( errorReport, typeReport, nonPersistedObject, bundle );

                context.markForRemoval( nonPersistedObject );
            }
        }

        return typeReport;
    }
}
