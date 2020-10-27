package com.mass3d.dxf2.metadata.objectbundle.validation;

import static com.mass3d.dxf2.metadata.objectbundle.validation.ValidationUtils.addObjectReports;

import java.util.ArrayList;
import java.util.List;
import com.mass3d.common.IdentifiableObject;
import com.mass3d.dxf2.metadata.objectbundle.ObjectBundle;
import com.mass3d.feedback.ErrorReport;
import com.mass3d.feedback.TypeReport;
import com.mass3d.importexport.ImportStrategy;

public class ValidationHooksCheck
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

        if ( objects == null || objects.isEmpty() )
        {
            return typeReport;
        }

        for ( IdentifiableObject object : objects )
        {
            List<ErrorReport> errorReports = new ArrayList<>();
            ctx.getObjectBundleHooks().forEach( hook -> errorReports.addAll( hook.validate( object, bundle ) ) );

            if ( !errorReports.isEmpty() )
            {
                addObjectReports( errorReports, typeReport, object, bundle );
                ctx.markForRemoval( object );
            }
        }

        return typeReport;
    }

}
