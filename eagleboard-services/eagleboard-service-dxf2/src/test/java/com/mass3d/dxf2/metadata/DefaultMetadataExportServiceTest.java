package com.mass3d.dxf2.metadata;

import com.mass3d.dataelement.DataElement;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import com.mass3d.common.IdentifiableObject;
import com.mass3d.common.SetMap;
import com.mass3d.fieldfilter.FieldFilterParams;
import com.mass3d.fieldfilter.FieldFilterService;
import com.mass3d.node.types.CollectionNode;
import com.mass3d.option.Option;
import com.mass3d.query.QueryService;
import com.mass3d.scheduling.JobConfiguration;
import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaService;
import com.mass3d.system.SystemService;
import com.mass3d.user.CurrentUserService;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.stubbing.Answer;

/**
 * Unit tests for {@link DefaultMetadataExportService}.
 *
 */
public class DefaultMetadataExportServiceTest
{
    @Mock
    private SchemaService schemaService;

    @Mock
    private QueryService queryService;

    @Mock
    private FieldFilterService fieldFilterService;

    @Mock
    private CurrentUserService currentUserService;

//    @Mock
//    private ProgramRuleService programRuleService;
//
//    @Mock
//    private ProgramRuleVariableService programRuleVariableService;

    @Mock
    private SystemService systemService;

    @InjectMocks
    private DefaultMetadataExportService service;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Test
    public void getMetadataWithDependenciesAsNodeSharing()
    {
        DataElement attribute = new DataElement();
        SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> metadata = new SetMap<>();
        metadata.put( DataElement.class, new HashSet<>() );

        service = Mockito.spy( service );
        Mockito.when( service.getMetadataWithDependencies( Mockito.eq( attribute ) ) ).thenReturn( metadata );

        Mockito.when( fieldFilterService.toCollectionNode( Mockito.eq( DataElement.class ), Mockito.any() ) ).then((Answer<CollectionNode>) invocation ->
        {
            FieldFilterParams fieldFilterParams = invocation.getArgument( 1 );
            Assert.assertFalse( fieldFilterParams.getSkipSharing() );
            return new CollectionNode( "test" );
        });

        MetadataExportParams params = new MetadataExportParams();
        service.getMetadataWithDependenciesAsNode( attribute, params );

        Mockito.verify( fieldFilterService, Mockito.only() ).toCollectionNode( Mockito.eq( DataElement.class ), Mockito.any() );
    }


    @Test
    public void getMetadataWithDependenciesAsNodeSkipSharing()
    {
        DataElement attribute = new DataElement();
        SetMap<Class<? extends IdentifiableObject>, IdentifiableObject> metadata = new SetMap<>();
        metadata.put( DataElement.class, new HashSet<>() );

        service = Mockito.spy( service );
        Mockito.when( service.getMetadataWithDependencies( Mockito.eq( attribute ) ) ).thenReturn( metadata );

        Mockito.when( fieldFilterService.toCollectionNode( Mockito.eq( DataElement.class ), Mockito.any() ) ).then((Answer<CollectionNode>) invocation ->
        {
            FieldFilterParams fieldFilterParams = invocation.getArgument( 1 );
            Assert.assertTrue( fieldFilterParams.getSkipSharing() );
            return new CollectionNode( "test" );
        });

        MetadataExportParams params = new MetadataExportParams();
        params.setSkipSharing( true );
        service.getMetadataWithDependenciesAsNode( attribute, params );

        Mockito.verify( fieldFilterService, Mockito.only() ).toCollectionNode( Mockito.eq( DataElement.class ), Mockito.any() );
    }

    @Test
    public void getParamsFromMapIncludedSecondary()
    {
        Mockito.when( schemaService.getSchemaByPluralName( Mockito.eq( "jobConfigurations" ) ) )
            .thenReturn( new Schema( JobConfiguration.class, "jobConfiguration", "jobConfigurations" ) );
        Mockito.when( schemaService.getSchemaByPluralName( Mockito.eq( "options" ) ) )
            .thenReturn( new Schema( Option.class, "option", "options" ) );

        final Map<String, List<String>> params = new HashMap<>();
        params.put( "jobConfigurations", Collections.singletonList( "true" ) );
        params.put( "options", Collections.singletonList( "true" ) );

        MetadataExportParams exportParams = service.getParamsFromMap( params );
        Assert.assertTrue( exportParams.getClasses().contains( JobConfiguration.class ) );
        Assert.assertTrue( exportParams.getClasses().contains( Option.class ) );
    }

    @Test
    public void getParamsFromMapNotIncludedSecondary()
    {
        Mockito.when( schemaService.getSchemaByPluralName( Mockito.eq( "jobConfigurations" ) ) )
            .thenReturn( new Schema( JobConfiguration.class, "jobConfiguration", "jobConfigurations" ) );
        Mockito.when( schemaService.getSchemaByPluralName( Mockito.eq( "options" ) ) )
            .thenReturn( new Schema( Option.class, "option", "options" ) );

        final Map<String, List<String>> params = new HashMap<>();
        params.put( "jobConfigurations", Arrays.asList( "true", "false" ) );
        params.put( "options", Collections.singletonList( "true" ) );

        MetadataExportParams exportParams = service.getParamsFromMap( params );
        Assert.assertFalse( exportParams.getClasses().contains( JobConfiguration.class ) );
        Assert.assertTrue( exportParams.getClasses().contains( Option.class ) );
    }

    @Test
    public void getParamsFromMapNoSecondary()
    {
        Mockito.when( schemaService.getSchemaByPluralName( Mockito.eq( "options" ) ) )
            .thenReturn( new Schema( Option.class, "option", "options" ) );

        final Map<String, List<String>> params = new HashMap<>();
        params.put( "options", Collections.singletonList( "true" ) );

        MetadataExportParams exportParams = service.getParamsFromMap( params );
        Assert.assertFalse( exportParams.getClasses().contains( JobConfiguration.class ) );
        Assert.assertTrue( exportParams.getClasses().contains( Option.class ) );
    }
}