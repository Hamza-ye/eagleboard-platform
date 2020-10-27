package com.mass3d.webapi.service;

import com.mass3d.common.Pager;
import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaService;
import com.mass3d.todotask.TodoTask;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Unit tests for {@link DefaultLinkService}.
 *
 */
public class DefaultLinkServiceTest
{
    @Mock
    private SchemaService schemaService;

    @Mock
    private ContextService contextService;

    @InjectMocks
    private DefaultLinkService service;

    private MockHttpServletRequest request = new MockHttpServletRequest();

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Test
    public void noLinks()
    {
        Mockito.when( schemaService.getDynamicSchema( Mockito.eq( TodoTask.class ) ) ).thenAnswer( invocation -> {
            Schema schema = new Schema( TodoTask.class, "todoTask", "todoTasks" );
            schema.setApiEndpoint( "/todoTasks" );
            return schema;
        } );

        request.setRequestURI( "/todoTasks" );
        Mockito.when( contextService.getRequest() ).thenReturn( request );

        final Pager pager = new Pager();
        service.generatePagerLinks( pager, TodoTask.class );
        Assert.assertNull( pager.getPrevPage() );
        Assert.assertNull( pager.getNextPage() );
    }

    @Test
    public void nextLinkDefaultParameters()
    {
        Mockito.when( schemaService.getDynamicSchema( Mockito.eq( TodoTask.class ) ) ).thenAnswer( invocation -> {
            Schema schema = new Schema( TodoTask.class, "todoTask", "todoTasks" );
            schema.setRelativeApiEndpoint( "/todoTasks" );
            return schema;
        } );

        request.setRequestURI( "/todoTasks" );
        Mockito.when( contextService.getRequest() ).thenReturn( request );

        Mockito.when( contextService.getApiPath() ).thenReturn( "/demo/api/456" );

        Mockito.when( contextService.getParameterValuesMap() ).thenAnswer( invocation -> {
            final Map<String, List<String>> map = new HashMap<>();
            map.put( "page", Collections.singletonList( "1" ) );
            map.put( "pageSize", Collections.singletonList( "55" ) );
            return map;
        } );

        final Pager pager = new Pager( 1, 1000 );
        service.generatePagerLinks( pager, TodoTask.class );
        Assert.assertNull( pager.getPrevPage() );
        Assert.assertEquals( "/demo/api/456/todoTasks?page=2", pager.getNextPage() );
    }

    @Test
    public void nextLinkParameters()
    {
        Mockito.when( schemaService.getDynamicSchema( Mockito.eq( TodoTask.class ) ) ).thenAnswer( invocation -> {
            Schema schema = new Schema( TodoTask.class, "todoTask", "todoTasks" );
            schema.setRelativeApiEndpoint( "/todoTasks" );
            return schema;
        } );

        request.setRequestURI( "/todoTasks.json" );
        Mockito.when( contextService.getRequest() ).thenReturn( request );

        Mockito.when( contextService.getApiPath() ).thenReturn( "/demo/api/456" );

        Mockito.when( contextService.getParameterValuesMap() ).thenAnswer( invocation -> {
            final Map<String, List<String>> map = new HashMap<>();
            map.put( "page", Collections.singletonList( "1" ) );
            map.put( "pageSize", Collections.singletonList( "55" ) );
            map.put( "fields", Collections.singletonList( "id,name,value[id,text]" ) );
            map.put( "value[x]", Arrays.asList( "test1", "test2\u00D8" ) );
            return map;
        } );

        final Pager pager = new Pager( 1, 1000 );
        service.generatePagerLinks( pager, TodoTask.class );
        Assert.assertNull( pager.getPrevPage() );
        Assert.assertEquals( "/demo/api/456/todoTasks.json?page=2&fields=id%2Cname%2Cvalue%5Bid%2Ctext%5D&value%5Bx%5D=test1&value%5Bx%5D=test2%C3%98", pager.getNextPage() );
    }

    @Test
    public void prevLinkDefaultParameters()
    {
        Mockito.when( schemaService.getDynamicSchema( Mockito.eq( TodoTask.class ) ) ).thenAnswer( invocation -> {
            Schema schema = new Schema( TodoTask.class, "todoTask", "todoTasks" );
            schema.setRelativeApiEndpoint( "/todoTasks" );
            return schema;
        } );

        request.setRequestURI( "/todoTasks.xml" );
        Mockito.when( contextService.getRequest() ).thenReturn( request );

        Mockito.when( contextService.getApiPath() ).thenReturn( "/demo/api/456" );

        Mockito.when( contextService.getParameterValuesMap() ).thenAnswer( invocation -> {
            final Map<String, List<String>> map = new HashMap<>();
            map.put( "page", Collections.singletonList( "1" ) );
            map.put( "pageSize", Collections.singletonList( "55" ) );
            return map;
        } );

        final Pager pager = new Pager( 2, 60 );
        service.generatePagerLinks( pager, TodoTask.class );
        Assert.assertEquals( "/demo/api/456/todoTasks.xml", pager.getPrevPage() );
        Assert.assertNull( pager.getNextPage() );
    }

    @Test
    public void nextLink()
    {
        Mockito.when( schemaService.getDynamicSchema( Mockito.eq( TodoTask.class ) ) ).thenAnswer( invocation -> {
            Schema schema = new Schema( TodoTask.class, "todoTask", "todoTasks" );
            schema.setRelativeApiEndpoint( "/todoTasks" );
            return schema;
        } );

        request.setRequestURI( "/todoTasks.xml.gz" );
        Mockito.when( contextService.getRequest() ).thenReturn( request );

        Mockito.when( contextService.getApiPath() ).thenReturn( "/demo/api/456" );

        Mockito.when( contextService.getParameterValuesMap() ).thenAnswer( invocation -> {
            final Map<String, List<String>> map = new HashMap<>();
            map.put( "page", Collections.singletonList( "1" ) );
            map.put( "pageSize", Collections.singletonList( "55" ) );
            return map;
        } );

        final Pager pager = new Pager( 2, 60 );
        service.generatePagerLinks( pager, TodoTask.class );
        Assert.assertEquals( "/demo/api/456/todoTasks.xml.gz", pager.getPrevPage() );
        Assert.assertNull( pager.getNextPage() );
    }

    @Test
    public void nextLinkWithDotsInPath()
    {
        Mockito.when( schemaService.getDynamicSchema( Mockito.eq( TodoTask.class ) ) ).thenAnswer( invocation -> {
            Schema schema = new Schema( TodoTask.class, "todoTask", "todoTasks" );
            schema.setRelativeApiEndpoint( "/todoTasks" );
            return schema;
        } );

        request.setRequestURI( "https://play.dhis2.org/2.30/api/30/todoTasks.xml.gz" );
        Mockito.when( contextService.getRequest() ).thenReturn( request );

        Mockito.when( contextService.getApiPath() ).thenReturn( "/2.30/api/30" );

        Mockito.when( contextService.getParameterValuesMap() ).thenAnswer( invocation -> {
            final Map<String, List<String>> map = new HashMap<>();
            map.put( "page", Collections.singletonList( "1" ) );
            map.put( "pageSize", Collections.singletonList( "55" ) );
            return map;
        } );

        final Pager pager = new Pager( 2, 60 );
        service.generatePagerLinks( pager, TodoTask.class );
        Assert.assertEquals( "/2.30/api/30/todoTasks.xml.gz", pager.getPrevPage() );
        Assert.assertNull( pager.getNextPage() );
    }

    @Test
    public void prevLinkParameters()
    {
        Mockito.when( schemaService.getDynamicSchema( Mockito.eq( TodoTask.class ) ) ).thenAnswer( invocation -> {
            Schema schema = new Schema( TodoTask.class, "todoTask", "todoTasks" );
            schema.setRelativeApiEndpoint( "/todoTasks" );
            return schema;
        } );

        Mockito.when( contextService.getRequest() ).thenReturn( request );

        Mockito.when( contextService.getApiPath() ).thenReturn( "/demo/api/456" );

        Mockito.when( contextService.getParameterValuesMap() ).thenAnswer( invocation -> {
            final Map<String, List<String>> map = new HashMap<>();
            map.put( "page", Collections.singletonList( "1" ) );
            map.put( "pageSize", Collections.singletonList( "55" ) );
            map.put( "fields", Collections.singletonList( "id,name,value[id,text]" ) );
            map.put( "value[x]", Arrays.asList( "test1", "test2\u00D8" ) );
            return map;
        } );

        final Pager pager = new Pager( 3, 110 );
        service.generatePagerLinks( pager, TodoTask.class );
        Assert.assertNull( pager.getNextPage() );
        Assert.assertEquals( "/demo/api/456/todoTasks?page=2&fields=id%2Cname%2Cvalue%5Bid%2Ctext%5D&value%5Bx%5D=test1&value%5Bx%5D=test2%C3%98", pager.getPrevPage() );
    }

    @Test
    public void prevLinkParametersPage1()
    {
        Mockito.when( schemaService.getDynamicSchema( Mockito.eq( TodoTask.class ) ) ).thenAnswer( invocation -> {
            Schema schema = new Schema( TodoTask.class, "todoTask", "todoTasks" );
            schema.setRelativeApiEndpoint( "/todoTasks" );
            return schema;
        } );

        Mockito.when( contextService.getRequest() ).thenReturn( request );

        Mockito.when( contextService.getApiPath() ).thenReturn( "/demo/api/456" );

        Mockito.when( contextService.getParameterValuesMap() ).thenAnswer( invocation -> {
            final Map<String, List<String>> map = new HashMap<>();
            map.put( "page", Collections.singletonList( "1" ) );
            map.put( "pageSize", Collections.singletonList( "55" ) );
            map.put( "fields", Collections.singletonList( "id,name,value[id,text]" ) );
            map.put( "value[x]", Arrays.asList( "test1", "test2\u00D8" ) );
            return map;
        } );

        final Pager pager = new Pager( 2, 90 );
        service.generatePagerLinks( pager, TodoTask.class );
        Assert.assertNull( pager.getNextPage() );
        Assert.assertEquals( "/demo/api/456/todoTasks?fields=id%2Cname%2Cvalue%5Bid%2Ctext%5D&value%5Bx%5D=test1&value%5Bx%5D=test2%C3%98", pager.getPrevPage() );
    }
}