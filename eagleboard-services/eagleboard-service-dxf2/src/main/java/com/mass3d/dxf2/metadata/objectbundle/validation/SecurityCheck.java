package com.mass3d.dxf2.metadata.objectbundle.validation;

import static com.mass3d.dxf2.metadata.objectbundle.validation.ValidationUtils.addObjectReports;

import java.util.Iterator;
import java.util.List;
import com.mass3d.common.IdentifiableObject;
import com.mass3d.dxf2.metadata.objectbundle.ObjectBundle;
import com.mass3d.feedback.ErrorCode;
import com.mass3d.feedback.ErrorReport;
import com.mass3d.feedback.TypeReport;
import com.mass3d.importexport.ImportStrategy;
import com.mass3d.preheat.PreheatIdentifier;
import com.mass3d.user.User;

public class SecurityCheck
    implements
    ValidationCheck
{
    @Override
    public TypeReport check( ObjectBundle bundle, Class<? extends IdentifiableObject> klass,
        List<IdentifiableObject> persistedObjects, List<IdentifiableObject> nonPersistedObjects,
        ImportStrategy importStrategy, ValidationContext context )
    {

        if ( importStrategy.isCreateAndUpdate() )
        {
            TypeReport report = runValidationCheck( bundle, klass, persistedObjects, ImportStrategy.UPDATE, context );
            report.merge( runValidationCheck( bundle, klass, nonPersistedObjects, ImportStrategy.CREATE, context ) );
            return report;
        }
        else if ( importStrategy.isCreate() )
        {
            return runValidationCheck( bundle, klass, nonPersistedObjects, ImportStrategy.CREATE, context );
        }
        else if ( importStrategy.isUpdate() )
        {
            return runValidationCheck( bundle, klass, persistedObjects, ImportStrategy.UPDATE, context );
        }
        else
        {
            return new TypeReport( klass );
        }

    }

    private TypeReport runValidationCheck( ObjectBundle bundle, Class<? extends IdentifiableObject> klass,
        List<IdentifiableObject> objects, ImportStrategy importMode, ValidationContext ctx )
    {

        TypeReport typeReport = new TypeReport( klass );

        if ( objects == null || objects.isEmpty() )
        {
            return typeReport;
        }

        Iterator<IdentifiableObject> iterator = objects.iterator();
        PreheatIdentifier identifier = bundle.getPreheatIdentifier();

        while ( iterator.hasNext() )
        {
            IdentifiableObject object = iterator.next();

            if ( importMode.isCreate() )
            {
                if ( !ctx.getAclService().canCreate( bundle.getUser(), klass ) )
                {
                    ErrorReport errorReport = new ErrorReport( klass, ErrorCode.E3000,
                        identifier.getIdentifiersWithName( bundle.getUser() ),
                        identifier.getIdentifiersWithName( object ) );

                    ValidationUtils.addObjectReport( errorReport, typeReport, object, bundle);
                    ctx.markForRemoval( object );
                    continue;
                }
            }
            else
            {
                IdentifiableObject persistedObject = bundle.getPreheat().get( bundle.getPreheatIdentifier(), object );

                if ( importMode.isUpdate() )
                {
                    if ( !ctx.getAclService().canUpdate( bundle.getUser(), persistedObject ) )
                    {
                        ErrorReport errorReport = new ErrorReport( klass, ErrorCode.E3001,
                            identifier.getIdentifiersWithName( bundle.getUser() ),
                            identifier.getIdentifiersWithName( object ) );

                        ValidationUtils.addObjectReport( errorReport, typeReport, object, bundle);
                        ctx.markForRemoval( object );
                        continue;
                    }
                }
                else if ( importMode.isDelete() )
                {
                    if ( !ctx.getAclService().canDelete( bundle.getUser(), persistedObject ) )
                    {
                        ErrorReport errorReport = new ErrorReport( klass, ErrorCode.E3002,
                            identifier.getIdentifiersWithName( bundle.getUser() ),
                            identifier.getIdentifiersWithName( object ) );

                        ValidationUtils.addObjectReport( errorReport, typeReport, object, bundle);

                        ctx.markForRemoval( object );
                        continue;
                    }
                }
            }

            if ( object instanceof User )
            {
                User user = (User) object;
                List<ErrorReport> errorReports = ctx.getUserService().validateUser( user, bundle.getUser() );

                if ( !errorReports.isEmpty() ) {

                    addObjectReports( errorReports, typeReport, object, bundle );
                    ctx.markForRemoval( object );
                }
            }

            List<ErrorReport> sharingErrorReports = ctx.getAclService().verifySharing( object, bundle.getUser() );
            if ( !sharingErrorReports.isEmpty() )
            {
                addObjectReports( sharingErrorReports, typeReport, object, bundle );
                ctx.markForRemoval( object );

            }
        }

        return typeReport;
    }


}
