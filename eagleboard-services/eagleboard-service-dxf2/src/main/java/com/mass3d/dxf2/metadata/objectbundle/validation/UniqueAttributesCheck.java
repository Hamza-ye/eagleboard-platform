package com.mass3d.dxf2.metadata.objectbundle.validation;

import static com.mass3d.dxf2.metadata.objectbundle.validation.ValidationUtils.addObjectReports;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import com.mass3d.attribute.Attribute;
import com.mass3d.attribute.AttributeValue;
import com.mass3d.common.IdentifiableObject;
import com.mass3d.common.IdentifiableObjectUtils;
import com.mass3d.dxf2.metadata.objectbundle.ObjectBundle;
import com.mass3d.feedback.ErrorCode;
import com.mass3d.feedback.ErrorReport;
import com.mass3d.feedback.TypeReport;
import com.mass3d.importexport.ImportStrategy;
import com.mass3d.preheat.Preheat;
import com.mass3d.preheat.PreheatIdentifier;
import com.mass3d.schema.Schema;

public class UniqueAttributesCheck
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
            List<ErrorReport> errorReports = checkUniqueAttributes( klass, object, bundle.getPreheat(),
                bundle.getPreheatIdentifier() );

            if ( !errorReports.isEmpty() )
            {

                addObjectReports( errorReports, typeReport, object, bundle );
                ctx.markForRemoval( object );
            }
        }

        return typeReport;
    }

    private List<ErrorReport> checkUniqueAttributes( Class<? extends IdentifiableObject> klass,
        IdentifiableObject object, Preheat preheat, PreheatIdentifier identifier )
    {
        List<ErrorReport> errorReports = new ArrayList<>();

        if ( object == null || preheat.isDefault( object ) || !preheat.getUniqueAttributes().containsKey( klass ) )
        {
            return errorReports;
        }

        Set<AttributeValue> attributeValues = object.getAttributeValues();
        List<String> uniqueAttributes = new ArrayList<>( preheat.getUniqueAttributes().get( klass ) ); // make copy for
                                                                                                       // modification

        if ( !preheat.getUniqueAttributeValues().containsKey( klass ) )
        {
            preheat.getUniqueAttributeValues().put( klass, new HashMap<>() );
        }

        Map<String, Map<String, String>> uniqueAttributeValues = preheat.getUniqueAttributeValues().get( klass );

        if ( uniqueAttributes.isEmpty() )
        {
            return errorReports;
        }

        attributeValues.forEach( attributeValue -> {
            Attribute attribute = preheat.get( identifier, attributeValue.getAttribute() );

            if ( attribute == null || !attribute.isUnique() || StringUtils.isEmpty( attributeValue.getValue() ) )
            {
                return;
            }

            if ( uniqueAttributeValues.containsKey( attribute.getUid() ) )
            {
                Map<String, String> values = uniqueAttributeValues.get( attribute.getUid() );

                if ( values.containsKey( attributeValue.getValue() )
                    && !values.get( attributeValue.getValue() ).equals( object.getUid() ) )
                {
                    errorReports.add( new ErrorReport( Attribute.class, ErrorCode.E4009,
                        IdentifiableObjectUtils.getDisplayName( attribute ), attributeValue.getValue() )
                            .setMainId( attribute.getUid() ).setErrorProperty( "value" ) );
                }
                else
                {
                    uniqueAttributeValues.get( attribute.getUid() ).put( attributeValue.getValue(), object.getUid() );
                }
            }
            else
            {
                uniqueAttributeValues.put( attribute.getUid(), new HashMap<>() );
                uniqueAttributeValues.get( attribute.getUid() ).put( attributeValue.getValue(), object.getUid() );
            }
        } );

        return errorReports;
    }
}
