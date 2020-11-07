package com.mass3d.webapi.controller.dataelement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.cache2k.Cache;
import org.cache2k.Cache2kBuilder;
import com.mass3d.category.CategoryService;
import com.mass3d.common.DhisApiVersion;
import com.mass3d.common.IdentifiableObjectManager;
import com.mass3d.common.Pager;
import com.mass3d.commons.collection.CollectionUtils;
import com.mass3d.dataelement.DataElement;
import com.mass3d.dataelement.DataElementGroup;
import com.mass3d.dataelement.DataElementOperand;
import com.mass3d.dataset.DataSet;
import com.mass3d.dxf2.common.OrderParams;
import com.mass3d.fieldfilter.FieldFilterParams;
import com.mass3d.fieldfilter.FieldFilterService;
import com.mass3d.node.NodeUtils;
import com.mass3d.node.Preset;
import com.mass3d.node.types.RootNode;
import com.mass3d.query.Order;
import com.mass3d.query.Query;
import com.mass3d.query.QueryParserException;
import com.mass3d.query.QueryService;
import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaService;
import com.mass3d.schema.descriptors.DataElementOperandSchemaDescriptor;
import com.mass3d.user.CurrentUserService;
import com.mass3d.user.User;
import com.mass3d.webapi.mvc.annotation.ApiVersion;
import com.mass3d.webapi.service.ContextService;
import com.mass3d.webapi.service.LinkService;
import com.mass3d.webapi.utils.PaginationUtils;
import com.mass3d.webapi.webdomain.WebMetadata;
import com.mass3d.webapi.webdomain.WebOptions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;

import static com.google.common.base.Preconditions.checkNotNull;

@Controller
@RequestMapping( value = DataElementOperandSchemaDescriptor.API_ENDPOINT )
@ApiVersion( { DhisApiVersion.DEFAULT, DhisApiVersion.ALL } )
public class DataElementOperandController
{
    private final IdentifiableObjectManager manager;
    private final QueryService queryService;
    private final FieldFilterService fieldFilterService;
    private final LinkService linkService;
    private final ContextService contextService;
    private final SchemaService schemaService;
    private final CategoryService dataElementCategoryService;
    private final CurrentUserService currentUserService;

    private Cache<String,Integer> paginationCountCache = new Cache2kBuilder<String, Integer>() {}
        .expireAfterWrite( 1, TimeUnit.MINUTES )
        .build();

    public DataElementOperandController( IdentifiableObjectManager manager, QueryService queryService,
        FieldFilterService fieldFilterService, LinkService linkService, ContextService contextService,
        SchemaService schemaService,
        CategoryService dataElementCategoryService, CurrentUserService currentUserService )
    {
        checkNotNull( manager );
        checkNotNull( queryService );
        checkNotNull( fieldFilterService );
        checkNotNull( linkService );
        checkNotNull( contextService );
        checkNotNull( schemaService );
        checkNotNull( dataElementCategoryService );
        checkNotNull( currentUserService );

        this.manager = manager;
        this.queryService = queryService;
        this.fieldFilterService = fieldFilterService;
        this.linkService = linkService;
        this.contextService = contextService;
        this.schemaService = schemaService;
        this.dataElementCategoryService = dataElementCategoryService;
        this.currentUserService = currentUserService;
    }

    @GetMapping
    @SuppressWarnings( "unchecked" )
    public @ResponseBody RootNode getObjectList( @RequestParam Map<String, String> rpParameters, OrderParams orderParams )
            throws QueryParserException
    {
        Schema schema = schemaService.getDynamicSchema( DataElementOperand.class );

        List<String> fields = Lists.newArrayList( contextService.getParameterValues( "fields" ) );
        List<String> filters = Lists.newArrayList( contextService.getParameterValues( "filter" ) );
        List<Order> orders = orderParams.getOrders( schema );

        if ( fields.isEmpty() )
        {
            fields.addAll( Preset.ALL.getFields() );
        }

        WebOptions options = new WebOptions( rpParameters );
        WebMetadata metadata = new WebMetadata();
        List<DataElementOperand> dataElementOperands;

        if ( options.isTrue( "persisted" ) )
        {
            dataElementOperands = Lists.newArrayList( manager.getAll( DataElementOperand.class ) );
        }
        else
        {
            boolean totals = options.isTrue( "totals" );

            String deg = CollectionUtils.popStartsWith( filters, "dataElement.dataElementGroups.id:eq:" );
            deg = deg != null ? deg.substring( "dataElement.dataElementGroups.id:eq:".length() ) : null;

            String ds = options.get( "dataSet" );

            if ( deg != null )
            {
                DataElementGroup dataElementGroup = manager.get( DataElementGroup.class, deg );
                dataElementOperands = dataElementCategoryService.getOperands( dataElementGroup.getMembers(), totals );
            }
            else if ( ds != null )
            {
                DataSet dataSet = manager.get( DataSet.class, ds );
                dataElementOperands = dataElementCategoryService.getOperands( dataSet, totals );
            }
            else
            {
                List<DataElement> dataElements = new ArrayList<>( manager.getAllSorted( DataElement.class ) );
                dataElementOperands = dataElementCategoryService.getOperands( dataElements, totals );
            }
        }

        Query query = queryService.getQueryFromUrl( DataElementOperand.class, filters, orders,
            PaginationUtils.getPaginationData( options ), options.getRootJunction() );
        query.setDefaultOrder();
        query.setObjects( dataElementOperands );

        dataElementOperands = (List<DataElementOperand>) queryService.query( query );
        Pager pager = metadata.getPager();

        if ( options.hasPaging() && pager == null )
        {
            // fetch the count for the current query from a short-lived cache
            int count = paginationCountCache.computeIfAbsent(
                calculatePaginationCountKey( currentUserService.getCurrentUser(), filters, options ),
                () -> queryService.count( query ) );
            pager = new Pager( options.getPage(), count, options.getPageSize() );
            linkService.generatePagerLinks( pager, DataElementOperand.class );
        }

        RootNode rootNode = NodeUtils.createMetadata();

        if ( pager != null )
        {
            rootNode.addChild( NodeUtils.createPager( pager ) );
        }

        rootNode.addChild( fieldFilterService.toCollectionNode( DataElementOperand.class,
            new FieldFilterParams( dataElementOperands, fields ) ) );

        return rootNode;
    }

    private String calculatePaginationCountKey( User currentUser, List<String> filters, WebOptions options )
    {
        return currentUser.getUsername() + "." + "DataElementOperand" + "." + String.join( "|", filters ) + "."
            + options.getRootJunction().name() + options.get( "restrictToCaptureScope" );
    }
}
