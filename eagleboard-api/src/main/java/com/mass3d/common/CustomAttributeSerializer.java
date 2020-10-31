package com.mass3d.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import java.io.IOException;
import com.mass3d.attribute.Attribute;

public class CustomAttributeSerializer
    extends JsonSerializer<Attribute>
{
    @Override
    public void serialize( Attribute attribute, JsonGenerator jsonGenerator, SerializerProvider serializers )
        throws IOException
    {
        ToXmlGenerator xmlGenerator = null;

        if ( jsonGenerator instanceof ToXmlGenerator )
        {
            xmlGenerator = (ToXmlGenerator) jsonGenerator;
        }

        jsonGenerator.writeStartObject();

        if ( xmlGenerator != null )
        {
            xmlGenerator.setNextIsAttribute( true );
            xmlGenerator.setNextName( null );
        }

        jsonGenerator.writeStringField( "id", attribute.getUid() );
        jsonGenerator.writeEndObject();
    }
}