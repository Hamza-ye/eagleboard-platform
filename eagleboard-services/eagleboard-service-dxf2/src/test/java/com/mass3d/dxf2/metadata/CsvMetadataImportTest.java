package com.mass3d.dxf2.metadata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import com.mass3d.DhisSpringTest;
import com.mass3d.common.IdentifiableObjectManager;
import com.mass3d.common.MergeMode;
import com.mass3d.dataelement.DataElement;
import com.mass3d.dataelement.DataElementService;
import com.mass3d.dxf2.csv.CsvImportClass;
import com.mass3d.dxf2.csv.CsvImportOptions;
import com.mass3d.dxf2.csv.CsvImportService;
import com.mass3d.dxf2.metadata.feedback.ImportReport;
import com.mass3d.option.OptionGroup;
import com.mass3d.option.OptionGroupSet;
import com.mass3d.option.OptionService;
import com.mass3d.option.OptionSet;
import com.mass3d.preheat.PreheatIdentifier;
import com.mass3d.schema.SchemaService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

public class CsvMetadataImportTest
    extends DhisSpringTest
{
    @Autowired
    private DataElementService dataElementService;

    @Autowired
    private OptionService optionService;

    @Autowired
    private CsvImportService csvImportService;

    @Autowired
    private MetadataImportService importService;

    @Autowired
    private SchemaService schemaService;

    @Autowired
    private IdentifiableObjectManager manager;

    private InputStream input;

    @Test
    public void testDataElementImport()
        throws Exception
    {
        input = new ClassPathResource( "metadata/dataElements.csv" ).getInputStream();

        Metadata metadata = csvImportService.fromCsv( input, new CsvImportOptions()
            .setImportClass( CsvImportClass.DATA_ELEMENT )
            .setFirstRowIsHeader( true ) );

        assertEquals( 2, metadata.getDataElements().size() );

        MetadataImportParams params = new MetadataImportParams();
        params.addMetadata( schemaService.getMetadataSchemas(), metadata );

        ImportReport importReport = importService.importMetadata( params );

        assertEquals( 2, importReport.getStats().getCreated() );

        Collection<DataElement> dataElements = dataElementService.getAllDataElements();

        assertEquals( 2, dataElements.size() );
    }

    @Test
    public void testOptionSetImport()
        throws Exception
    {
        input = new ClassPathResource( "metadata/optionSets.csv" ).getInputStream();

        Metadata metadata = csvImportService.fromCsv( input, new CsvImportOptions()
            .setImportClass( CsvImportClass.OPTION_SET )
            .setFirstRowIsHeader( true ) );

        assertEquals( 4, metadata.getOptionSets().size() );
        assertEquals( 3, metadata.getOptionSets().get( 0 ).getOptions().size() );
        assertEquals( 3, metadata.getOptionSets().get( 1 ).getOptions().size() );
        assertEquals( 3, metadata.getOptionSets().get( 2 ).getOptions().size() );
        assertEquals( 3, metadata.getOptionSets().get( 3 ).getOptions().size() );

        MetadataImportParams params = new MetadataImportParams();
        params.addMetadata( schemaService.getMetadataSchemas(), metadata );

        ImportReport importReport = importService.importMetadata( params );

        assertEquals( 16, importReport.getStats().getCreated() );

        List<OptionSet> optionSets = new ArrayList<>( optionService.getAllOptionSets() );

        assertEquals( 4, optionSets.size() );
        assertEquals( 3, optionSets.get( 0 ).getOptions().size() );
        assertEquals( 3, optionSets.get( 1 ).getOptions().size() );
        assertEquals( 3, optionSets.get( 2 ).getOptions().size() );
        assertEquals( 3, optionSets.get( 3 ).getOptions().size() );
    }

    @Test
    public void testOptionSetMerge() throws IOException
    {
        // Import 1 OptionSet with 3 Options
        input = new ClassPathResource( "metadata/optionSet_add.csv" ).getInputStream();

        Metadata metadata = csvImportService.fromCsv( input, new CsvImportOptions()
            .setImportClass( CsvImportClass.OPTION_SET )
            .setFirstRowIsHeader( true ) );

        MetadataImportParams params = new MetadataImportParams();
        params.addMetadata( schemaService.getMetadataSchemas(), metadata );

        ImportReport importReport = importService.importMetadata( params );

        assertEquals( 4, importReport.getStats().getCreated() );

        // Send payload with 2 new Options
        input = new ClassPathResource( "metadata/optionSet_update.csv" ).getInputStream();

        metadata = csvImportService.fromCsv( input, new CsvImportOptions()
            .setImportClass( CsvImportClass.OPTION_SET )
            .setFirstRowIsHeader( true ) );

        params = new MetadataImportParams();
        params.addMetadata( schemaService.getMetadataSchemas(), metadata );
        params.setMergeMode( MergeMode.MERGE );

        importReport = importService.importMetadata( params );

        assertEquals( 2, importReport.getStats().getCreated() );

        OptionSet optionSet = optionService.getOptionSetByCode( "COLOR" );

        // Total 5 options added
        assertEquals( 5, optionSet.getOptions().size() );
    }

    @Test
    public void testOptionSetMergeDuplicate() throws IOException
    {
        // Import 1 OptionSet with 3 Options
        input = new ClassPathResource( "metadata/optionSet_add.csv" ).getInputStream();

        Metadata metadata = csvImportService.fromCsv( input, new CsvImportOptions()
            .setImportClass( CsvImportClass.OPTION_SET )
            .setFirstRowIsHeader( true )  );

        MetadataImportParams params = new MetadataImportParams();
        params.addMetadata( schemaService.getMetadataSchemas(), metadata );

        ImportReport importReport = importService.importMetadata( params );

        assertEquals( 4, importReport.getStats().getCreated() );

        // Send payload with 5 Options, 2 new and 3 old from above
        input = new ClassPathResource( "metadata/optionSet_update_duplicate.csv" ).getInputStream();

        metadata = csvImportService.fromCsv( input, new CsvImportOptions()
            .setImportClass( CsvImportClass.OPTION_SET )
            .setFirstRowIsHeader( true ) );

        params = new MetadataImportParams();
        params.addMetadata( schemaService.getMetadataSchemas(), metadata );
        params.setIdentifier( PreheatIdentifier.CODE );
        params.setMergeMode( MergeMode.MERGE );

        importReport = importService.importMetadata( params );

        // Only 2 new Options are added
        assertEquals( 2, importReport.getStats().getCreated() );

        OptionSet optionSet = optionService.getOptionSetByCode( "COLOR" );

        // Total 5 Options added
        assertEquals( 5, optionSet.getOptions().size() );
    }

    @Test
    public void testOptionSetReplace() throws IOException
    {
        // Import 1 OptionSet with 3 Options
        input = new ClassPathResource( "metadata/optionSet_add.csv" ).getInputStream();

        Metadata metadata = csvImportService.fromCsv( input, new CsvImportOptions()
            .setImportClass( CsvImportClass.OPTION_SET )
            .setFirstRowIsHeader( true ) );

        MetadataImportParams params = new MetadataImportParams();
        params.addMetadata( schemaService.getMetadataSchemas(), metadata );

        ImportReport importReport = importService.importMetadata( params );

        assertEquals( 4, importReport.getStats().getCreated() );

        // Send payload with 2 new Options
        input = new ClassPathResource( "metadata/optionSet_update.csv" ).getInputStream();

        metadata = csvImportService.fromCsv( input, new CsvImportOptions()
            .setImportClass( CsvImportClass.OPTION_SET )
            .setFirstRowIsHeader( true ) );

        params = new MetadataImportParams();
        params.addMetadata( schemaService.getMetadataSchemas(), metadata );

        importReport = importService.importMetadata( params );

        assertEquals( 2, importReport.getStats().getCreated() );

        OptionSet optionSet = optionService.getOptionSetByCode( "COLOR" );

        // 3 old Options are replaced by 2 new added Options
        assertEquals( 2, optionSet.getOptions().size() );
    }

    @Test
    public void testImportOptionGroupSet()
        throws IOException
    {
        input = new ClassPathResource( "metadata/option_set.csv" ).getInputStream();
        Metadata metadata = csvImportService.fromCsv( input, new CsvImportOptions()
            .setImportClass( CsvImportClass.OPTION_SET )
            .setFirstRowIsHeader( true ) );

        MetadataImportParams params = new MetadataImportParams();
        params.addMetadata( schemaService.getMetadataSchemas(), metadata );

        ImportReport importReport = importService.importMetadata( params );

        assertEquals( 5, importReport.getStats().getCreated() );

        OptionSet optionSetA = manager.get( OptionSet.class, "xmRubJIhmaK" );
        assertNotNull( optionSetA );
        assertEquals( 2, optionSetA.getOptions().size() );

        OptionSet optionSetB = manager.get( OptionSet.class, "QYDAByFgTr1" );
        assertNotNull( optionSetB );
        assertEquals( 1, optionSetB.getOptions().size() );

        input = new ClassPathResource( "metadata/option_groups.csv" ).getInputStream();
        metadata = csvImportService.fromCsv( input, new CsvImportOptions()
            .setImportClass( CsvImportClass.OPTION_GROUP )
            .setFirstRowIsHeader( true ) );

        params = new MetadataImportParams();
        params.addMetadata( schemaService.getMetadataSchemas(), metadata );

        importReport = importService.importMetadata( params );

        assertEquals( 2, importReport.getStats().getCreated() );

        OptionGroup optionGroupA = manager.get( OptionGroup.class, "UeHtizvXbx6" );
        assertNotNull( optionGroupA );
        assertEquals( 2, optionGroupA.getMembers().size() );

        OptionGroup optionGroupB = manager.get( OptionGroup.class, "EVYjFX6Ez0a" );
        assertNotNull( optionGroupB );
        assertEquals( 1, optionGroupB.getMembers().size() );

        input = new ClassPathResource( "metadata/option_group_set.csv" ).getInputStream();
        metadata = csvImportService.fromCsv( input, new CsvImportOptions()
            .setImportClass( CsvImportClass.OPTION_GROUP_SET )
            .setFirstRowIsHeader( true ) );

        params = new MetadataImportParams();
        params.addMetadata( schemaService.getMetadataSchemas(), metadata );

        importReport = importService.importMetadata( params );

        assertEquals( 2, importReport.getStats().getCreated() );

        OptionGroupSet optionGroupSetA = manager.get( OptionGroupSet.class, "FB9i0Jl2R80" );
        assertNotNull( optionGroupSetA );

        OptionGroupSet optionGroupSetB = manager.get( OptionGroupSet.class, "K30djctzUtN" );
        assertNotNull( optionGroupSetB );

        input = new ClassPathResource( "metadata/option_group_set_members.csv" ).getInputStream();
        metadata = csvImportService.fromCsv( input, new CsvImportOptions()
            .setImportClass( CsvImportClass.OPTION_GROUP_SET_MEMBERSHIP )
            .setFirstRowIsHeader( true ) );

        params = new MetadataImportParams();
        params.addMetadata( schemaService.getMetadataSchemas(), metadata );

        importReport = importService.importMetadata( params );

        assertEquals( 2, importReport.getStats().getUpdated() );

        OptionGroupSet ogsA = optionService.getOptionGroupSet( "FB9i0Jl2R80" );
        OptionGroupSet ogsB = optionService.getOptionGroupSet( "K30djctzUtN" );

        assertEquals( 1, ogsA.getMembers().size() );
        assertEquals( 1, ogsB.getMembers().size() );

        assertEquals( 2, ogsA.getMembers().get( 0 ).getMembers().size() );

    }
}
