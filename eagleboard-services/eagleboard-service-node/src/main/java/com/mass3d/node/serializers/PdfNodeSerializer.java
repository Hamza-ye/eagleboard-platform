package com.mass3d.node.serializers;

import com.google.common.collect.Lists;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfPTable;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import com.mass3d.node.AbstractNodeSerializer;
import com.mass3d.node.Node;
import com.mass3d.node.types.CollectionNode;
import com.mass3d.node.types.ComplexNode;
import com.mass3d.node.types.RootNode;
import com.mass3d.node.types.SimpleNode;
import com.mass3d.system.util.PDFUtils;
import com.mass3d.util.DateUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope( value = "prototype", proxyMode = ScopedProxyMode.INTERFACES )
public class PdfNodeSerializer extends AbstractNodeSerializer
{
    private static final String[] CONTENT_TYPES = { "application/pdf" };

    private Document document;

    @Override
    public List<String> contentTypes()
    {
        return Lists.newArrayList( CONTENT_TYPES );
    }

    @Override
    protected void startSerialize( RootNode rootNode, OutputStream outputStream ) throws Exception
    {
        document = PDFUtils.openDocument( outputStream );
    }

    @Override
    protected void endSerialize( RootNode rootNode, OutputStream outputStream ) throws Exception
    {
        if ( document.isOpen() )
        {
            document.close();
        }
    }

    @Override
    protected void flushStream() throws Exception
    {

    }

    @Override
    protected void startWriteRootNode( RootNode rootNode ) throws Exception
    {
        for ( Node child : rootNode.getChildren() )
        {
            if ( child.isCollection() )
            {
                PdfPTable table = PDFUtils.getPdfPTable( true, 0.25f, 0.75f );
                boolean haveContent = false;

                for ( Node node : child.getChildren() )
                {
                    for ( Node property : node.getChildren() )
                    {
                        if ( property.isSimple() )
                        {
                            table.addCell( PDFUtils.getItalicCell( property.getName() ) );
                            table.addCell( PDFUtils.getTextCell( getValue( (SimpleNode) property ) ) );
                            haveContent = true;
                        }
                    }

                    if ( haveContent )
                    {
                        table.addCell( PDFUtils.getEmptyCell( 2, 15 ) );
                        haveContent = false;
                    }
                }

                document.add( table );
            }
        }
    }

    public String getValue( SimpleNode simpleNode )
    {
        if ( simpleNode.getValue() == null )
        {
            return "";
        }

        String value = String.format( "%s", simpleNode.getValue() );

        if ( Date.class.isAssignableFrom( simpleNode.getValue().getClass() ) )
        {
            value = DateUtils.getIso8601NoTz( (Date) simpleNode.getValue() );
        }

        return value;
    }

    @Override
    protected void endWriteRootNode( RootNode rootNode ) throws Exception
    {

    }

    @Override
    protected void startWriteSimpleNode( SimpleNode simpleNode ) throws Exception
    {

    }

    @Override
    protected void endWriteSimpleNode( SimpleNode simpleNode ) throws Exception
    {

    }

    @Override
    protected void startWriteComplexNode( ComplexNode complexNode ) throws Exception
    {

    }

    @Override
    protected void endWriteComplexNode( ComplexNode complexNode ) throws Exception
    {

    }

    @Override
    protected void startWriteCollectionNode( CollectionNode collectionNode ) throws Exception
    {

    }

    @Override
    protected void endWriteCollectionNode( CollectionNode collectionNode ) throws Exception
    {

    }

    @Override
    protected void dispatcher( Node node ) throws Exception
    {

    }
}
