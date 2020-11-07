package com.mass3d.dxf2.metadata.objectbundle.validation;

import static com.mass3d.dxf2.metadata.objectbundle.validation.ValidationUtils.addObjectReports;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.mass3d.attribute.Attribute;
import com.mass3d.attribute.AttributeValue;
import com.mass3d.common.IdentifiableObject;
import com.mass3d.dxf2.metadata.objectbundle.ObjectBundle;
import com.mass3d.feedback.ErrorCode;
import com.mass3d.feedback.ErrorReport;
import com.mass3d.feedback.TypeReport;
import com.mass3d.importexport.ImportStrategy;
import com.mass3d.preheat.Preheat;
import com.mass3d.schema.Schema;

public class MandatoryAttributesCheck
    implements
    ValidationCheck
{
    @Override
    public TypeReport check( ObjectBundle bundle, Class<? extends IdentifiableObject> klass,
        List<IdentifiableObject> persistedObjects, List<IdentifiableObject> nonPersistedObjects,
        ImportStrategy importStrategy, ValidationContext ctx )
    {
        TypeReport typeReport = new TypeReport( klass );
        Schema schema = ctx.getSchemaService().getDynamicSchema( klass );
        List<IdentifiableObject> objects = selectObjects( persistedObjects, nonPersistedObjects, importStrategy );

        if ( objects.isEmpty() || !schema.havePersistedProperty( "attributeValues" ) )
        {
            return typeReport;
        }

        for ( IdentifiableObject object : objects )
        {
            List<ErrorReport> errorReports = checkMandatoryAttributes( klass, object, bundle.getPreheat() );

            if ( !errorReports.isEmpty() )
            {
                addObjectReports( errorReports, typeReport, object, bundle );

                ctx.markForRemoval( object );
            }
        }

        return typeReport;
    }

    private List<ErrorReport> checkMandatoryAttributes( Class<? extends IdentifiableObject> klass,
        IdentifiableObject object, Preheat preheat )
    {
        List<ErrorReport> errorReports = new ArrayList<>();

        if ( object == null || preheat.isDefault( object ) || !preheat.getMandatoryAttributes().containsKey( klass ) )
        {
            return errorReports;
        }

        Set<AttributeValue> attributeValues = object.getAttributeValues();
        Set<String> mandatoryAttributes = new HashSet<>( preheat.getMandatoryAttributes().get( klass ) ); // make copy
                                                                                                          // for
        if ( mandatoryAttributes.isEmpty() )
        {
            return errorReports;
        }

        attributeValues
            .forEach( attributeValue -> mandatoryAttributes.remove( attributeValue.getAttribute().getUid() ) );
        mandatoryAttributes.forEach( att -> errorReports.add(
            new ErrorReport( Attribute.class, ErrorCode.E4011, att ).setMainId( att ).setErrorProperty( "value" ) ) );

        return errorReports;
    }
}
