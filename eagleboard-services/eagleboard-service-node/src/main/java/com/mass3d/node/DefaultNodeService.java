package com.mass3d.node;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import com.mass3d.fieldfilter.FieldFilterParams;
import com.mass3d.fieldfilter.FieldFilterService;
import com.mass3d.node.types.CollectionNode;
import com.mass3d.node.types.ComplexNode;
import com.mass3d.node.types.RootNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service( "com.mass3d.node.NodeService" )
public class DefaultNodeService implements NodeService
{
    @Autowired
    private FieldFilterService fieldFilterService;

    @Autowired( required = false )
    private List<NodeSerializer> nodeSerializers = Lists.newArrayList();

    @Autowired( required = false )
    private List<NodeDeserializer> nodeDeserializers = Lists.newArrayList();

    private Map<String, NodeSerializer> nodeSerializerMap = Maps.newHashMap();

    private Map<String, NodeDeserializer> nodeDeserializerMap = Maps.newHashMap();

    @PostConstruct
    private void init()
    {
        for ( NodeSerializer nodeSerializer : nodeSerializers )
        {
            for ( String contentType : nodeSerializer.contentTypes() )
            {
                nodeSerializerMap.put( contentType, nodeSerializer );
            }
        }

        for ( NodeDeserializer nodeDeserializer : nodeDeserializers )
        {
            for ( String contentType : nodeDeserializer.contentTypes() )
            {
                nodeDeserializerMap.put( contentType, nodeDeserializer );
            }
        }
    }

    @Override
    public NodeSerializer getNodeSerializer( String contentType )
    {
        if ( nodeSerializerMap.containsKey( contentType ) )
        {
            return nodeSerializerMap.get( contentType );
        }

        return null;
    }

    @Override
    public void serialize( RootNode rootNode, String contentType, OutputStream outputStream )
    {
        NodeSerializer nodeSerializer = getNodeSerializer( contentType );

        if ( nodeSerializer == null )
        {
            return; // TODO throw exception?
        }

        try
        {
            nodeSerializer.serialize( rootNode, outputStream );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }

    @Override
    public NodeDeserializer getNodeDeserializer( String contentType )
    {
        if ( nodeDeserializerMap.containsKey( contentType ) )
        {
            return nodeDeserializerMap.get( contentType );
        }

        return null;
    }

    @Override
    public RootNode deserialize( String contentType, InputStream inputStream )
    {
        NodeDeserializer nodeDeserializer = getNodeDeserializer( contentType );

        if ( nodeDeserializer == null )
        {
            return null; // TODO throw exception?
        }

        try
        {
            return nodeDeserializer.deserialize( inputStream );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public ComplexNode toNode( Object object )
    {
        Assert.notNull( object, "object can not be null" );
        return fieldFilterService.toComplexNode( new FieldFilterParams( Lists.newArrayList( object ), new ArrayList<>() ) );
    }

    @Override
    public CollectionNode toNode( List<Object> objects )
    {
        Assert.notNull( objects, "objects can not be null" );
        Assert.isTrue( objects.size() > 0, "objects list must be larger than 0" );

        return fieldFilterService.toCollectionNode( objects.get( 0 ).getClass(), new FieldFilterParams( objects, new ArrayList<>() ) );
    }
}
