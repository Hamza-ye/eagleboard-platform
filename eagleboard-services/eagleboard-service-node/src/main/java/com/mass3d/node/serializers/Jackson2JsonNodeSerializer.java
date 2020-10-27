package com.mass3d.node.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.vividsolutions.jts.geom.Geometry;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import com.mass3d.node.AbstractNodeSerializer;
import com.mass3d.node.types.CollectionNode;
import com.mass3d.node.types.ComplexNode;
import com.mass3d.node.types.RootNode;
import com.mass3d.node.types.SimpleNode;
import com.mass3d.util.DateUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope( value = "prototype", proxyMode = ScopedProxyMode.INTERFACES )
public class Jackson2JsonNodeSerializer extends AbstractNodeSerializer
{
    public static final String CONTENT_TYPE = "application/json";

    public static final String JSONP_CALLBACK = "com.mass3d.node.serializers.Jackson2JsonNodeSerializer.callback";

    private final ObjectMapper jsonMapper;

    private JsonGenerator generator = null;

    public Jackson2JsonNodeSerializer( ObjectMapper jsonMapper )
    {
        this.jsonMapper = jsonMapper;
    }

    @Override
    public List<String> contentTypes()
    {
        return Lists.newArrayList( CONTENT_TYPE );
    }

    @Override
    protected void flushStream() throws Exception
    {
        generator.flush();
    }

    @Override
    protected void startSerialize( RootNode rootNode, OutputStream outputStream ) throws Exception
    {
        generator = jsonMapper.getFactory().createGenerator( outputStream );
    }

    @Override
    protected void startWriteRootNode( RootNode rootNode ) throws Exception
    {
        if ( config.getProperties().containsKey( JSONP_CALLBACK ) )
        {
            generator.writeRaw( config.getProperties().get( JSONP_CALLBACK ) + "(" );
        }

        generator.writeStartObject();
    }

    @Override
    protected void endWriteRootNode( RootNode rootNode ) throws Exception
    {
        generator.writeEndObject();

        if ( config.getProperties().containsKey( JSONP_CALLBACK ) )
        {
            generator.writeRaw( ")" );
        }
    }

    @Override
    protected void startWriteSimpleNode( SimpleNode simpleNode ) throws Exception
    {
        Object value = simpleNode.getValue();

        if ( Date.class.isAssignableFrom( simpleNode.getValue().getClass() ) )
        {
            value = DateUtils.getIso8601NoTz( (Date) value );
        }

        if ( Geometry.class.isAssignableFrom( simpleNode.getValue().getClass() ) )
        {
            generator.writeFieldName( simpleNode.getName() );
            generator.writeRawValue( jsonMapper.writeValueAsString( value ) );
            return;
        }

        if ( simpleNode.getParent().isCollection() )
        {
            generator.writeObject( value );
        }
        else if ( value instanceof String)
        {
            generator.writeStringField( simpleNode.getName(), (String) value );
        }
        else
        {
            generator.writeObjectField( simpleNode.getName(), value );
        }
    }

    @Override
    protected void endWriteSimpleNode( SimpleNode simpleNode ) throws Exception
    {
    }

    @Override
    protected void startWriteComplexNode( ComplexNode complexNode ) throws Exception
    {
        if ( complexNode.getParent().isCollection() )
        {
            generator.writeStartObject();
        }
        else
        {
            generator.writeObjectFieldStart( complexNode.getName() );
        }
    }

    @Override
    protected void endWriteComplexNode( ComplexNode complexNode ) throws Exception
    {
        generator.writeEndObject();
    }

    @Override
    protected void startWriteCollectionNode( CollectionNode collectionNode ) throws Exception
    {
        if ( collectionNode.getParent().isCollection() )
        {
            generator.writeStartArray();
        }
        else
        {
            generator.writeArrayFieldStart( collectionNode.getName() );
        }
    }

    @Override
    protected void endWriteCollectionNode( CollectionNode collectionNode ) throws Exception
    {
        generator.writeEndArray();
    }
}
