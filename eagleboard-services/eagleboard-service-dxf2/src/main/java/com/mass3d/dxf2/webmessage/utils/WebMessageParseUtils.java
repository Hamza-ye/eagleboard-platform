package com.mass3d.dxf2.webmessage.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import org.apache.commons.io.IOUtils;
import com.mass3d.commons.config.JacksonObjectMapperConfig;
import com.mass3d.dxf2.webmessage.WebMessageParseException;

public class WebMessageParseUtils
{
    private final static ObjectMapper JSON_MAPPER = JacksonObjectMapperConfig.staticJsonMapper();

    public static <T> T fromWebMessageResponse( InputStream input, Class<T> klass ) throws WebMessageParseException
    {
        StringWriter writer = new StringWriter();
        try
        {
            IOUtils.copy( input, writer, "UTF-8" );
        }
        catch ( IOException e )
        {
            throw new WebMessageParseException( "Could not read the InputStream" + e.getMessage(), e );
        }
        return parseJson( writer.toString(), klass );
    }

    public static <T> T fromWebMessageResponse( String input, Class<T> klass ) throws WebMessageParseException
    {
        return parseJson( input, klass );
    }

    private static <T> T parseJson( String input, Class<T> klass ) throws WebMessageParseException
    {
        JsonNode objectNode = null;
        try
        {
            objectNode = JSON_MAPPER.readTree( input );
        }
        catch ( IOException e )
        {
            throw new WebMessageParseException( "Invalid JSON String. " + e.getMessage(), e );
        }

        JsonNode responseNode = null;

        if ( objectNode != null )
        {
            responseNode = objectNode.get( "response" );
        }
        else
        {
            throw new WebMessageParseException( "The object node is null. Could not parse the JSON." );
        }

        try
        {
            return JSON_MAPPER.readValue( responseNode.toString(), klass );
        }
        catch ( IOException e )
        {
            throw new WebMessageParseException( "Could not parse the JSON." + e.getMessage(), e );
        }
    }
}
