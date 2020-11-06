package com.mass3d.commons.config.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import org.springframework.util.StringUtils;

public class EmptyStringToNullStdDeserializer extends JsonDeserializer<String>
{
    @Override
    public String deserialize( JsonParser parser, DeserializationContext context ) throws IOException
    {
        String result = parser.getValueAsString();

        if ( StringUtils.isEmpty( result ) )
        {
            return null;
        }

        return result;
    }
}
