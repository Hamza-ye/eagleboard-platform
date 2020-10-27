package com.mass3d.common.adapter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.util.List;

/**
 * TODO switch to <code>jgen.writeObject( field )</code>
 * 
 */
public class JacksonRowDataSerializer
    extends JsonSerializer<List<List<Object>>>
{
    private static final String EMPTY = "";
    
    @Override
    public void serialize( List<List<Object>> values, JsonGenerator jgen, SerializerProvider provider ) throws IOException
    {
        jgen.writeStartArray();
        
        for ( List<Object> row : values )
        {
            jgen.writeStartArray();
            
            for ( Object field : row )
            {
                jgen.writeString( field != null ? String.valueOf( field ) : EMPTY );
            }
            
            jgen.writeEndArray();
        }
        
        jgen.writeEndArray();
    }
}
