package com.mass3d.node.serializers;

import com.google.common.collect.Lists;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFHyperlink;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.mass3d.node.AbstractNodeSerializer;
import com.mass3d.node.Node;
import com.mass3d.node.types.CollectionNode;
import com.mass3d.node.types.ComplexNode;
import com.mass3d.node.types.RootNode;
import com.mass3d.node.types.SimpleNode;
import com.mass3d.schema.PropertyType;
import com.mass3d.util.DateUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope( value = "prototype", proxyMode = ScopedProxyMode.INTERFACES )
public class ExcelNodeSerializer extends AbstractNodeSerializer
{
    private static final String[] CONTENT_TYPES = { "application/vnd.ms-excel" };

    @Override
    public List<String> contentTypes()
    {
        return Lists.newArrayList( CONTENT_TYPES );
    }

    private XSSFWorkbook workbook;

    private XSSFSheet sheet;

    @Override
    protected void startSerialize( RootNode rootNode, OutputStream outputStream ) throws Exception
    {
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet( "Sheet1" );

        XSSFFont boldFont = workbook.createFont();
        boldFont.setBold( true );

        XSSFCellStyle boldCellStyle = workbook.createCellStyle();
        boldCellStyle.setFont( boldFont );

        // build schema
        for ( Node child : rootNode.getChildren() )
        {
            if ( child.isCollection() )
            {
                if ( !child.getChildren().isEmpty() )
                {
                    Node node = child.getChildren().get( 0 );

                    XSSFRow row = sheet.createRow( 0 );

                    int cellIdx = 0;

                    for ( Node property : node.getChildren() )
                    {
                        if ( property.isSimple() )
                        {
                            XSSFCell cell = row.createCell( cellIdx++ );
                            cell.setCellValue( property.getName() );
                            cell.setCellStyle( boldCellStyle );
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void endSerialize( RootNode rootNode, OutputStream outputStream ) throws Exception
    {
        int columns = sheet.getRow( 0 ).getPhysicalNumberOfCells();

        for ( int i = 0; i < columns; i++ )
        {
            sheet.autoSizeColumn( i );
        }

        workbook.write( outputStream );
    }

    @Override
    protected void flushStream() throws Exception
    {

    }

    @Override
    protected void startWriteRootNode( RootNode rootNode ) throws Exception
    {
        XSSFCreationHelper creationHelper = workbook.getCreationHelper();

        int rowIdx = 1;

        for ( Node collectionNode : rootNode.getChildren() )
        {
            if ( collectionNode.isCollection() )
            {
                for ( Node complexNode : collectionNode.getChildren() )
                {
                    XSSFRow row = sheet.createRow( rowIdx++ );
                    int cellIdx = 0;

                    for ( Node node : complexNode.getChildren() )
                    {
                        if ( node.isSimple() )
                        {
                            XSSFCell cell = row.createCell( cellIdx++ );
                            cell.setCellValue( getValue( (SimpleNode) node ) );

                            if ( node.haveProperty() && PropertyType.URL.equals( node.getProperty().getPropertyType() ) )
                            {
                                XSSFHyperlink hyperlink = creationHelper.createHyperlink( HyperlinkType.URL );
                                hyperlink.setAddress( getValue( (SimpleNode) node ) );
                                hyperlink.setLabel( getValue( (SimpleNode) node ) );

                                cell.setHyperlink( hyperlink );
                            }
                            else if ( node.haveProperty() && PropertyType.EMAIL.equals( node.getProperty().getPropertyType() ) )
                            {
                                XSSFHyperlink hyperlink = creationHelper.createHyperlink( HyperlinkType.EMAIL );
                                hyperlink.setAddress( getValue( (SimpleNode) node ) );
                                hyperlink.setLabel( getValue( (SimpleNode) node ) );

                                cell.setHyperlink( hyperlink );
                            }
                        }
                    }
                }
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
}
