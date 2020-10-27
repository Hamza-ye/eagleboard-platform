package com.mass3d.node.serializers;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.google.common.collect.Lists;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import com.mass3d.node.AbstractNodeSerializer;
import com.mass3d.node.Node;
import com.mass3d.node.types.CollectionNode;
import com.mass3d.node.types.ComplexNode;
import com.mass3d.node.types.RootNode;
import com.mass3d.node.types.SimpleNode;
import com.mass3d.util.DateUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Scope( value = "prototype", proxyMode = ScopedProxyMode.INTERFACES )
public class StAXNodeSerializer extends AbstractNodeSerializer
{
    public static final String CONTENT_TYPE = "application/xml";

    private static final XMLOutputFactory xmlFactory = XMLOutputFactory.newInstance();

    static
    {
        xmlFactory.setProperty( "javax.xml.stream.isRepairingNamespaces", true );
    }

    private XMLStreamWriter writer;

    @Override
    public List<String> contentTypes()
    {
        return Lists.newArrayList( CONTENT_TYPE );
    }

    @Override
    protected void startSerialize( RootNode rootNode, OutputStream outputStream ) throws Exception
    {
        writer = xmlFactory.createXMLStreamWriter( outputStream );
        writer.setDefaultNamespace( rootNode.getDefaultNamespace() );
    }

    @Override
    protected void flushStream() throws Exception
    {
        writer.flush();
    }

    @Override
    protected void startWriteRootNode( RootNode rootNode ) throws Exception
    {
        writer.writeStartDocument( "UTF-8", "1.0" );

        if ( !StringUtils.isEmpty( rootNode.getComment() ) )
        {
            writer.writeComment( rootNode.getComment() );
        }

        writeStartElement( rootNode );
    }

    @Override
    protected void endWriteRootNode( RootNode rootNode ) throws Exception
    {
        writer.writeEndElement();
        writer.writeEndDocument();
    }

    @Override
    protected void startWriteSimpleNode( SimpleNode simpleNode ) throws Exception
    {
        String value = null;

        if ( simpleNode.getValue() != null && Date.class.isAssignableFrom( simpleNode.getValue().getClass() ) )
        {
            value = DateUtils.getIso8601NoTz( (Date) simpleNode.getValue() );
        }
        else
        {
            value = String.valueOf( simpleNode.getValue() );
        }

        if ( simpleNode.isAttribute() )
        {
            if ( value == null )
            {
                return;
            }

            if ( !StringUtils.isEmpty( simpleNode.getNamespace() ) )
            {
                writer.writeAttribute( simpleNode.getNamespace(), simpleNode.getName(), value );
            }
            else
            {
                writer.writeAttribute( simpleNode.getName(), value );
            }
        }
        else
        {
            writeStartElement( simpleNode );
            if ( handledCustomSerializer( simpleNode, writer ) )
            {
                return;
            }
            else if ( value != null )
            {
                writer.writeCharacters( value );
            }
            else
            {
                return;
            }
        }
    }

    @Override
    protected void endWriteSimpleNode( SimpleNode simpleNode ) throws Exception
    {
        if ( !simpleNode.isAttribute() )
        {
            writer.writeEndElement();
        }
    }

    @Override
    protected void startWriteComplexNode( ComplexNode complexNode ) throws Exception
    {
        writeStartElement( complexNode );
    }

    @Override
    protected void endWriteComplexNode( ComplexNode complexNode ) throws Exception
    {
        writer.writeEndElement();
    }

    @Override
    protected void startWriteCollectionNode( CollectionNode collectionNode ) throws Exception
    {
        if ( collectionNode.isWrapping() && !collectionNode.getChildren().isEmpty() )
        {
            writeStartElement( collectionNode );
        }
    }

    @Override
    protected void endWriteCollectionNode( CollectionNode collectionNode ) throws Exception
    {
        if ( collectionNode.isWrapping() && !collectionNode.getChildren().isEmpty() )
        {
            writer.writeEndElement();
        }
    }

    private void writeStartElement( Node node ) throws XMLStreamException
    {
        if ( !StringUtils.isEmpty( node.getComment() ) )
        {
            writer.writeComment( node.getComment() );
        }

        if ( !StringUtils.isEmpty( node.getNamespace() ) )
        {
            writer.writeStartElement( node.getNamespace(), node.getName() );
        }
        else
        {
            writer.writeStartElement( node.getName() );
        }
    }

    /**
     * @param simpleNode the {@link SimpleNode}.
     * @param writer the {@link XMLStreamWriter}.
     *
     * @return true if given simpleNode has been serialized using custom JsonSerializer
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws IOException
     */
    private boolean handledCustomSerializer( SimpleNode simpleNode, XMLStreamWriter writer )
        throws IllegalAccessException, InstantiationException, IOException
    {
        if ( simpleNode.getProperty() != null )
        {
            JsonSerialize declaredAnnotation = simpleNode.getProperty().getGetterMethod().getAnnotation( JsonSerialize.class );
            if ( declaredAnnotation != null )
            {
                Class<? extends JsonSerializer> serializer = declaredAnnotation.using();

                if ( serializer != null )
                {
                    JsonSerializer serializerInstance = serializer.newInstance();
                    XmlFactory factory = new XmlFactory();
                    ToXmlGenerator generator = factory.createGenerator( writer );
                    serializerInstance.serialize( simpleNode.getValue(), generator, null );
                    return true;
                }
            }
        }
        return false;
    }
}
