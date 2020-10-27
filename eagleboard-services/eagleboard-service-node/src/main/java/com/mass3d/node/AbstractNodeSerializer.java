package com.mass3d.node;

import java.io.OutputStream;
import com.mass3d.node.config.Config;
import com.mass3d.node.types.CollectionNode;
import com.mass3d.node.types.ComplexNode;
import com.mass3d.node.types.RootNode;
import com.mass3d.node.types.SimpleNode;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public abstract class AbstractNodeSerializer implements NodeSerializer
{
    protected static final DateTimeFormatter DT_FORMATTER = DateTimeFormat.forPattern( "yyyy-MM-dd'T'HH:mm:ss.SSSZ" )
        .withZoneUTC();

    protected void startSerialize( RootNode rootNode, OutputStream outputStream ) throws Exception
    {
    }

    protected void endSerialize( RootNode rootNode, OutputStream outputStream ) throws Exception
    {
    }

    protected abstract void flushStream() throws Exception;

    protected Config config;

    @Override
    public void serialize( RootNode rootNode, OutputStream outputStream ) throws Exception
    {
        this.config = rootNode.getConfig();
        startSerialize( rootNode, outputStream );
        writeRootNode( rootNode );
        endSerialize( rootNode, outputStream );
        this.config = null;
    }

    protected abstract void startWriteRootNode( RootNode rootNode ) throws Exception;

    protected void writeRootNode( RootNode rootNode ) throws Exception
    {
        startWriteRootNode( rootNode );

        for ( Node node : rootNode.getChildren() )
        {
            dispatcher( node );
            flushStream();
        }

        endWriteRootNode( rootNode );
        flushStream();
    }

    protected abstract void endWriteRootNode( RootNode rootNode ) throws Exception;

    protected abstract void startWriteSimpleNode( SimpleNode simpleNode ) throws Exception;

    protected void writeSimpleNode( SimpleNode simpleNode ) throws Exception
    {
        if ( !config.getInclusionStrategy().include( simpleNode.getValue() ) )
        {
            return;
        }

        startWriteSimpleNode( simpleNode );
        endWriteSimpleNode( simpleNode );
    }

    protected abstract void endWriteSimpleNode( SimpleNode simpleNode ) throws Exception;

    protected abstract void startWriteComplexNode( ComplexNode complexNode ) throws Exception;

    protected void writeComplexNode( ComplexNode complexNode ) throws Exception
    {
        if ( !config.getInclusionStrategy().include( complexNode.getChildren() ) )
        {
            return;
        }

        startWriteComplexNode( complexNode );

        for ( Node node : complexNode.getChildren() )
        {
            dispatcher( node );
            flushStream();
        }

        endWriteComplexNode( complexNode );
    }

    protected abstract void endWriteComplexNode( ComplexNode complexNode ) throws Exception;

    protected abstract void startWriteCollectionNode( CollectionNode collectionNode ) throws Exception;

    protected void writeCollectionNode( CollectionNode collectionNode ) throws Exception
    {
        if ( !config.getInclusionStrategy().include( collectionNode.getChildren() ) )
        {
            return;
        }

        startWriteCollectionNode( collectionNode );

        for ( Node node : collectionNode.getChildren() )
        {
            dispatcher( node );
            flushStream();
        }

        endWriteCollectionNode( collectionNode );
    }

    protected abstract void endWriteCollectionNode( CollectionNode collectionNode ) throws Exception;

    protected void dispatcher( Node node ) throws Exception
    {
        switch ( node.getType() )
        {
            case SIMPLE:
                writeSimpleNode( (SimpleNode) node );
                break;
            case COMPLEX:
                writeComplexNode( (ComplexNode) node );
                break;
            case COLLECTION:
                writeCollectionNode( (CollectionNode) node );
                break;
        }
    }
}
