package com.mass3d.render;

import static com.google.common.base.Preconditions.checkNotNull;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.util.JSONPObject;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import com.mass3d.common.IdentifiableObject;
import com.mass3d.metadata.version.MetadataVersion;
import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Default implementation that uses Jackson to serialize/deserialize
 *
 */
@Slf4j
@Service( "com.mass3d.render.RenderService" )
public class DefaultRenderService
    implements RenderService
{
    private final ObjectMapper jsonMapper;

    private final ObjectMapper xmlMapper;

    private SchemaService schemaService;

    public DefaultRenderService(
        @Qualifier( "jsonMapper" ) ObjectMapper jsonMapper,
        @Qualifier( "xmlMapper" ) ObjectMapper xmlMapper,
        SchemaService schemaService )
    {
        checkNotNull( jsonMapper );
        checkNotNull( xmlMapper );
        checkNotNull( schemaService );

        this.jsonMapper = jsonMapper;
        this.xmlMapper = xmlMapper;
        this.schemaService = schemaService;
    }

    //--------------------------------------------------------------------------
    // RenderService
    //--------------------------------------------------------------------------

    @Override
    public void toJson( OutputStream output, Object value )
        throws IOException
    {
        jsonMapper.writeValue( output, value );
    }

    @Override
    public String toJsonAsString( Object value )
    {
        try
        {
            return jsonMapper.writeValueAsString( value );
        }
        catch ( JsonProcessingException ignored )
        {
            ignored.printStackTrace();
        }

        return null;
    }

    @Override
    public void toJsonP( OutputStream output, Object value, String callback )
        throws IOException
    {
        if ( StringUtils.isEmpty( callback ) )
        {
            callback = "callback";
        }

        jsonMapper.writeValue( output, new JSONPObject( callback, value ) );
    }

    @Override
    public <T> T fromJson( InputStream input, Class<T> klass )
        throws IOException
    {
        return jsonMapper.readValue( input, klass );
    }

    @Override
    public <T> T fromJson( String input, Class<T> klass )
        throws IOException
    {
        return jsonMapper.readValue( input, klass );
    }

    @Override
    public <T> void toXml( OutputStream output, T value )
        throws IOException
    {
        xmlMapper.writeValue( output, value );
    }

    @Override
    public <T> T fromXml( InputStream input, Class<T> klass )
        throws IOException
    {
        return xmlMapper.readValue( input, klass );
    }

    @Override
    public <T> T fromXml( String input, Class<T> klass )
        throws IOException
    {
        return xmlMapper.readValue( input, klass );
    }

    @Override
    public boolean isValidJson( String json )
        throws IOException
    {
        try
        {
            jsonMapper.readValue( json, Object.class );
        }
        catch ( JsonParseException | JsonMappingException e )
        {
            return false;
        }

        return true;
    }

    @Override
    public JsonNode getSystemObject( InputStream inputStream, RenderFormat format ) throws IOException
    {
        ObjectMapper mapper;

        if ( RenderFormat.JSON == format )
        {
            mapper = jsonMapper;
        }
        else if ( RenderFormat.XML == format )
        {
            throw new IllegalArgumentException( "XML format is not supported." );
        }
        else
        {
            return null;
        }

        JsonNode rootNode = mapper.readTree( inputStream );

        return rootNode.get( "system" );
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public Map<Class<? extends IdentifiableObject>, List<IdentifiableObject>> fromMetadata( InputStream inputStream, RenderFormat format ) throws IOException
    {
        Map<Class<? extends IdentifiableObject>, List<IdentifiableObject>> map = new HashMap<>();

        ObjectMapper mapper;

        if ( RenderFormat.JSON == format )
        {
            mapper = jsonMapper;
        }
        else if ( RenderFormat.XML == format )
        {
            throw new IllegalArgumentException( "XML format is not supported." );
        }
        else
        {
            return map;
        }

        JsonNode rootNode = mapper.readTree( inputStream );
        Iterator<String> fieldNames = rootNode.fieldNames();

        while ( fieldNames.hasNext() )
        {
            String fieldName = fieldNames.next();
            JsonNode node = rootNode.get( fieldName );
            Schema schema = schemaService.getSchemaByPluralName( fieldName );

            if ( schema == null || !schema.isIdentifiableObject() )
            {
                log.info( "Skipping unknown property '" + fieldName + "'." );
                continue;
            }

            if ( !schema.isMetadata() )
            {
                log.debug( "Skipping non-metadata property `" + fieldName + "`." );
                continue;
            }

            List<IdentifiableObject> collection = new ArrayList<>();

            for ( JsonNode item : node )
            {
                IdentifiableObject value = mapper.treeToValue( item, (Class<? extends IdentifiableObject>) schema.getKlass() );
                if ( value != null ) collection.add( value );
            }

            map.put( (Class<? extends IdentifiableObject>) schema.getKlass(), collection );
        }

        return map;
    }

    @Override
    public List<MetadataVersion> fromMetadataVersion( InputStream versions, RenderFormat format ) throws IOException
    {
        List<MetadataVersion> metadataVersions = new ArrayList<>();

        if ( RenderFormat.JSON == format )
        {
            JsonNode rootNode = jsonMapper.readTree( versions );

            if ( rootNode != null )
            {
                JsonNode versionsNode = rootNode.get( "metadataversions" );

                if ( versionsNode instanceof ArrayNode )
                {
                    ArrayNode arrayVersionsNode = (ArrayNode) versionsNode;
                    metadataVersions = jsonMapper.readValue( arrayVersionsNode.toString().getBytes(), new TypeReference<List<MetadataVersion>>()
                    {
                    } );
                }
            }
        }

        return metadataVersions;
    }
}
