package com.mass3d.common.adapter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Set;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import com.mass3d.render.DeviceRenderTypeMap;
import com.mass3d.render.RenderDevice;
import com.mass3d.render.type.RenderingObject;

public class DeviceRenderTypeMapSerializer
    extends JsonSerializer<DeviceRenderTypeMap<RenderingObject<?>>>
{
    @Override
    @SuppressWarnings("unchecked")
    public void serialize( DeviceRenderTypeMap<RenderingObject<?>> value, JsonGenerator gen, SerializerProvider serializers )
        throws IOException
    {
        Set<RenderDevice> keys = value.keySet();
        if ( ToXmlGenerator.class.isAssignableFrom( gen.getClass() ) )
        {
            ToXmlGenerator xmlGenerator = (ToXmlGenerator) gen;
            try
            {
                XMLStreamWriter staxWriter = xmlGenerator.getStaxWriter();
                for ( RenderDevice key : keys )
                {
                    RenderingObject<?> val = value.get( key );
                    staxWriter.writeStartElement(  key.name() );
                    staxWriter.writeAttribute( RenderingObject._TYPE, val.getType().name() );
                    staxWriter.writeEndElement();
                }
            }
            catch ( XMLStreamException e )
            {
                throw new RuntimeException( e );
            }
        }
        else
        {
            gen.writeStartObject();

            for ( RenderDevice key : keys )
            {
                Object val =  value.get( key );
                String fieldValue = "";

                if ( LinkedHashMap.class.isAssignableFrom( val.getClass() ) )
                {
                    LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) val;
                    fieldValue = map.get( RenderingObject._TYPE );
                }
                else if ( RenderingObject.class.isAssignableFrom( val.getClass()) )
                {
                    RenderingObject<?> renderingObject = ( RenderingObject<?> ) val;
                    fieldValue = renderingObject.getType().name();
                }
                else
                {
                    fieldValue = val.toString();
                }

                gen.writeObjectFieldStart( key.name() );
                gen.writeStringField( RenderingObject._TYPE,  fieldValue );
                gen.writeEndObject();
            }
            gen.writeEndObject();
        }
    }
}