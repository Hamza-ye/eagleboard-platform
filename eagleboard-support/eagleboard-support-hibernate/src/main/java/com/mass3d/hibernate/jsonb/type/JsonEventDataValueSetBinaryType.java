package com.mass3d.hibernate.jsonb.type;

import com.fasterxml.jackson.core.type.TypeReference;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.hibernate.HibernateException;
import com.mass3d.eventdatavalue.EventDataValue;

public class JsonEventDataValueSetBinaryType extends JsonBinaryType
{
    public JsonEventDataValueSetBinaryType()
    {
        super();
        writer = MAPPER.writerFor( new TypeReference<Map<String, EventDataValue>>() {} );
        reader = MAPPER.readerFor( new TypeReference<Map<String, EventDataValue>>() {} );
        returnedClass = EventDataValue.class;
    }

    @Override
    protected void init( Class<?> klass )
    {
        returnedClass = klass;
        reader = MAPPER.readerFor( new TypeReference<Map<String, EventDataValue>>() {} );
        writer = MAPPER.writerFor( new TypeReference<Map<String, EventDataValue>>() {} );
    }

    @Override
    public Object deepCopy( Object value ) throws HibernateException
    {
        String json = convertObjectToJson( value );
        return convertJsonToObject( json );
    }

    /**
     * Serializes an object to JSON.
     *
     * @param object the object to convert.
     * @return JSON content.
     */
    @SuppressWarnings( "unchecked" )
    @Override
    protected String convertObjectToJson( Object object )
    {
        try
        {
            Set<EventDataValue> eventDataValues = object == null ? Collections
                .emptySet() : (Set<EventDataValue>) object;

            Map<String, EventDataValue> tempMap = new HashMap<>();

            for ( EventDataValue eventDataValue : eventDataValues )
            {
                tempMap.put( eventDataValue.getDataElement(), eventDataValue );
            }

            return writer.writeValueAsString( tempMap );
        }
        catch ( IOException e )
        {
            throw new IllegalArgumentException( e );
        }
    }

    /**
     * Deserializes JSON content to an object.
     *
     * @param content the JSON content.
     * @return an object.
     */
    @Override
    public Object convertJsonToObject( String content )
    {
        try
        {
            Map<String, EventDataValue> data = reader.readValue( content );

            return convertEventDataValuesMapIntoSet( data );
        }
        catch ( IOException e )
        {
            throw new IllegalArgumentException( e );
        }
    }

    public static Set<EventDataValue> convertEventDataValuesMapIntoSet( Map<String, EventDataValue> data )
    {

        Set<EventDataValue> eventDataValues = new HashSet<>();

        for ( Map.Entry<String, EventDataValue> entry : data.entrySet() )
        {

            EventDataValue eventDataValue = entry.getValue();
            eventDataValue.setDataElement( entry.getKey() );
            eventDataValues.add( eventDataValue );
        }

        return eventDataValues;
    }

}
