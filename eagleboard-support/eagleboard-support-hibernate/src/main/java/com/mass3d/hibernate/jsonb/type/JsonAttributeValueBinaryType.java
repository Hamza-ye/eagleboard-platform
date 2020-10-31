package com.mass3d.hibernate.jsonb.type;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.hibernate.HibernateException;
import com.mass3d.attribute.AttributeValue;

public class JsonAttributeValueBinaryType
    extends JsonBinaryType
{
    static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    protected JavaType getResultingJavaType( Class<?> returnedClass )
    {
        return MAPPER.getTypeFactory().constructMapLikeType( Map.class, String.class, returnedClass );
    }

    @Override
    @SuppressWarnings("unchecked")
    public String convertObjectToJson( Object object )
    {
        try
        {
            Set<AttributeValue> attributeValues = object == null ? Collections.emptySet()
                : (Set<AttributeValue>) object;

            Map<String, AttributeValue> attrValueMap = new HashMap<>();

            for ( AttributeValue attributeValue : attributeValues )
            {
                if ( attributeValue.getAttribute() != null )
                {
                    attrValueMap.put( attributeValue.getAttribute().getUid(), attributeValue );
                }
            }

            return writer.writeValueAsString( attrValueMap );

        }
        catch ( IOException e )
        {
            throw new RuntimeException( e );
        }
    }

    @Override
    public Object deepCopy( Object value ) throws HibernateException
    {
        String json = convertObjectToJson( value );
        return convertJsonToObject( json );
    }

    @Override
    public Object convertJsonToObject( String content )
    {
        try
        {
            Map<String, AttributeValue> data = reader.readValue( content );

            return convertAttributeValueMapIntoSet( data );
        }
        catch ( IOException e )
        {
            throw new RuntimeException( e );
        }
    }

    public static List<AttributeValue> convertListJsonToListObject( List<String> content )
    {
        return content.stream().map( json -> {
            try
            {
                return MAPPER.readValue( json, AttributeValue.class );
            }
            catch ( IOException e )
            {
                throw new RuntimeException( e );
            }
        } ).collect( Collectors.toList() );
    }

    private static Set<AttributeValue> convertAttributeValueMapIntoSet( Map<String, AttributeValue> data )
    {
        Set<AttributeValue> attributeValues = new HashSet<>();

        for ( Map.Entry<String, AttributeValue> entry : data.entrySet() )
        {
            AttributeValue attributeValue = entry.getValue();
            attributeValues.add( attributeValue );
        }

        return attributeValues;
    }
}
