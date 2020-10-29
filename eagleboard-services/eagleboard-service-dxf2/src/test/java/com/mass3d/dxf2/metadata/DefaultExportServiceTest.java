package com.mass3d.dxf2.metadata;

import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mass3d.todotask.TodoTask;
import com.mass3d.todotask.TodoTaskService;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.xml.xpath.XPathExpressionException;
import com.mass3d.DhisConvenienceTest;
import com.mass3d.DhisSpringTest;
import com.mass3d.common.IdentifiableObject;
import com.mass3d.dataelement.DataElement;
import com.mass3d.dataelement.DataElementService;
import com.mass3d.dataset.DataSet;
import com.mass3d.dataset.DataSetService;
import com.mass3d.period.MonthlyPeriodType;
import com.mass3d.period.Period;
import com.mass3d.period.PeriodService;
import com.mass3d.query.Query;
import com.mass3d.schema.SchemaService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class DefaultExportServiceTest
    extends DhisSpringTest
{
    @Autowired
    private DataElementService dataElementService;

    @Autowired
    private DataSetService dataSetService;

    @Autowired
    private TodoTaskService todoTaskService;

    @Autowired
    private PeriodService periodService;

    @Autowired
    private MetadataExportService exportService;

    @Autowired
    private SchemaService schemaService;

    @Autowired
    @Qualifier( "xmlMapper" )
    private ObjectMapper xmlMapper;

    private DataElement deA;

    private DataElement deB;

    private DataElement deC;

    private DataSet dsA;

    private TodoTask todoTaskA;

    private TodoTask todoTaskB;

    private Period peA;

    private Period peB;

    @Override
    public void setUpTest()
    {
        deA = createDataElement( 'A' );
        deB = createDataElement( 'B' );
        deC = createDataElement( 'C' );
        dsA = DhisConvenienceTest.createDataSet( 'A', new MonthlyPeriodType() );
        todoTaskA = DhisConvenienceTest.createTodotask( 'A' );
        todoTaskB = DhisConvenienceTest.createTodotask( 'B' );
        peA = DhisConvenienceTest.createPeriod( DhisConvenienceTest.getDate( 2012, 1, 1 ), DhisConvenienceTest.getDate( 2012, 1, 31 ) );
        peB = DhisConvenienceTest.createPeriod( DhisConvenienceTest.getDate( 2012, 2, 1 ), DhisConvenienceTest.getDate( 2012, 2, 29 ) );

        deA.setUid( "f7n9E0hX8qk" );
        deB.setUid( "Ix2HsbDMLea" );
        deC.setUid( "eY5ehpbEsB7" );
        dsA.setUid( "pBOMPrpg1QX" );
        todoTaskA.setUid( "DiszpKrYNg8" );
        todoTaskB.setUid( "BdfsJfj87js" );

        deA.setCode( "DE_A" );
        deB.setCode( "DE_B" );
        deC.setCode( "DE_C" );
        dsA.setCode( "DS_A" );
        todoTaskA.setCode( "OU_A" );
        todoTaskB.setCode( "OU_B" );

        dataElementService.addDataElement( deA );
        dataElementService.addDataElement( deB );
        dataElementService.addDataElement( deC );
        dataSetService.addDataSet( dsA );
        todoTaskService.addTodoTask(todoTaskA);
        todoTaskService.addTodoTask(todoTaskB);
        periodService.addPeriod( peA );
        periodService.addPeriod( peB );
    }

    @Test
    @SuppressWarnings( "unchecked" )
    public void exportMetaDataTest() throws IOException, XPathExpressionException
    {
        MetadataExportParams params = new MetadataExportParams();
        params.addQuery( Query.from( schemaService.getSchema( DataElement.class ) ) );
        params.addQuery( Query.from( schemaService.getSchema( TodoTask.class ) ) );

        Map<Class<? extends IdentifiableObject>, List<? extends IdentifiableObject>> metadataMap = exportService.getMetadata( params );

        Metadata metadata = new Metadata();
        metadata.setDataElements( (List<DataElement>) metadataMap.get( DataElement.class ) );
        metadata.setTodoTasks( (List<TodoTask>) metadataMap.get( TodoTask.class ) );

        String metaDataXml = xmlMapper.writeValueAsString( metadata );

        assertEquals( "1", xpathTest( "count(//d:todoTasks)", metaDataXml ) );
        assertEquals( "2", xpathTest( "count(//d:todoTask)", metaDataXml ) );
        assertEquals( "3", xpathTest( "count(//d:dataElement)", metaDataXml ) );
        assertEquals( "DE_A", xpathTest( "//d:dataElement[@name='DataElementA']/@code", metaDataXml ) );
    }
}
