package com.mass3d.webapi.controller.dimension;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Collections.emptyList;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static com.mass3d.common.CodeGenerator.isValidUid;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import com.mass3d.common.DataQueryRequest;
import com.mass3d.common.DimensionService;
import com.mass3d.common.DimensionalItemObject;
import com.mass3d.common.DimensionalObject;
import com.mass3d.common.IdentifiableObjectManager;
import com.mass3d.dataset.DataSet;
import com.mass3d.dxf2.common.OrderParams;
import com.mass3d.dxf2.webmessage.WebMessageException;
import com.mass3d.dxf2.webmessage.WebMessageUtils;
import com.mass3d.fieldfilter.Defaults;
import com.mass3d.fieldfilter.FieldFilterParams;
import com.mass3d.node.AbstractNode;
import com.mass3d.node.Node;
import com.mass3d.node.NodeUtils;
import com.mass3d.node.Preset;
import com.mass3d.node.types.CollectionNode;
import com.mass3d.node.types.RootNode;
import com.mass3d.query.Order;
import com.mass3d.query.Query;
import com.mass3d.query.QueryParserException;
import com.mass3d.webapi.controller.AbstractCrudController;
import com.mass3d.webapi.webdomain.WebMetadata;
import com.mass3d.webapi.webdomain.WebOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping( value = DimensionController.RESOURCE_PATH )
public class DimensionController
    extends AbstractCrudController<DimensionalObject>
{
    public static final String RESOURCE_PATH = "/dimensions";

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    @Autowired
    private DimensionService dimensionService;

//    @Autowired
//    private AnalyticsDimensionService analyticsDimensionService;

    @Autowired
    private IdentifiableObjectManager identifiableObjectManager;

    @Autowired
    private DimensionItemPageHandler dimensionItemPageHandler;

    // -------------------------------------------------------------------------
    // Controller
    // -------------------------------------------------------------------------

    @Override
    @SuppressWarnings( "unchecked" )
    protected @ResponseBody List<DimensionalObject> getEntityList( WebMetadata metadata, WebOptions options,
        List<String> filters, List<Order> orders )
        throws QueryParserException
    {
        List<DimensionalObject> dimensionalObjects;
        Query query = queryService.getQueryFromUrl( DimensionalObject.class, filters, orders,
            getPaginationData( options ), options.getRootJunction() );
        query.setDefaultOrder();
        query.setDefaults( Defaults.valueOf( options.get( "defaults", DEFAULTS ) ) );
        query.setObjects( dimensionService.getAllDimensions() );
        dimensionalObjects = (List<DimensionalObject>) queryService.query( query );

        return dimensionalObjects;
    }

    @Override
    protected List<DimensionalObject> getEntity( String uid, WebOptions options )
    {
        // This check prevents a NPE. Otherwise it will result in HTTP 500 to the client.
        if ( isNotBlank( uid ) && isValidUid( uid ) )
        {
            return newArrayList( dimensionService.getDimensionalObjectCopy( uid, true ) );
        }

        return emptyList();
    }

    @SuppressWarnings( "unchecked" )
    @RequestMapping( value = "/{uid}/items", method = RequestMethod.GET )
    public @ResponseBody RootNode getItems( @PathVariable String uid, @RequestParam Map<String, String> parameters,
        OrderParams orderParams )
        throws QueryParserException
    {
        List<String> fields = newArrayList( contextService.getParameterValues( "fields" ) );
        List<String> filters = newArrayList( contextService.getParameterValues( "filter" ) );
        List<Order> orders = orderParams.getOrders( getSchema( DimensionalItemObject.class ) );
        WebOptions options = new WebOptions( parameters );

        if ( fields.isEmpty() )
        {
            fields.addAll( Preset.defaultPreset().getFields() );
        }

        // Retrieving all items available for the given uid.
        List<DimensionalItemObject> totalItems = dimensionService.getCanReadDimensionItems( uid );

        // Creating a query based on the previous items found and pagination data/rules.
        Query query = queryService.getQueryFromUrl( DimensionalItemObject.class, filters, orders,
            getPaginationData( options ) );
        query.setObjects( totalItems );
        query.setDefaultOrder();

        // Querying the items based on the query rules built.
        List<DimensionalItemObject> paginatedItems = (List<DimensionalItemObject>) queryService.query( query );

        // Creating the response root node.
        RootNode rootNode = NodeUtils.createMetadata();

        CollectionNode collectionNode = rootNode
            .addChild( fieldFilterService.toCollectionNode( DimensionalItemObject.class,
                new FieldFilterParams( paginatedItems, fields ) ) );
        collectionNode.setName( "items" );

        for ( Node node : collectionNode.getChildren() )
        {
            ((AbstractNode) node).setName( "item" );
        }

        // Adding pagination elements to the root node.
        final int totalOfItems = isNotEmpty( totalItems ) ? totalItems.size() : 0;
        dimensionItemPageHandler.addPaginationToNodeIfEnabled( rootNode, options, uid, totalOfItems );

        return rootNode;
    }

//    @RequestMapping( value = "/constraints", method = RequestMethod.GET )
//    public @ResponseBody RootNode getDimensionConstraints(
//        @RequestParam( value = "links", defaultValue = "true", required = false ) Boolean links )
//    {
//        List<String> fields = newArrayList( contextService.getParameterValues( "fields" ) );
//        List<DimensionalObject> dimensionConstraints = dimensionService.getDimensionConstraints();
//
//        if ( links )
//        {
//            linkService.generateLinks( dimensionConstraints, false );
//        }
//
//        RootNode rootNode = NodeUtils.createMetadata();
//        rootNode.addChild( fieldFilterService.toCollectionNode( getEntityClass(),
//            new FieldFilterParams( dimensionConstraints, fields ) ) );
//
//        return rootNode;
//    }

//    @RequestMapping( value = "/recommendations", method = RequestMethod.GET )
//    public @ResponseBody RootNode getRecommendedDimensions( @RequestParam Set<String> dimension )
//    {
//        List<String> fields = newArrayList( contextService.getParameterValues( "fields" ) );
//        DataQueryRequest request = DataQueryRequest.newBuilder().dimension( dimension ).build();
//
//        if ( fields.isEmpty() )
//        {
//            fields.addAll( Preset.defaultPreset().getFields() );
//        }
//
//        List<DimensionalObject> dimensions = analyticsDimensionService.getRecommendedDimensions( request );
//
//        RootNode rootNode = NodeUtils.createMetadata();
//        rootNode.addChild(
//            fieldFilterService.toCollectionNode( getEntityClass(), new FieldFilterParams( dimensions, fields ) ) );
//
//        return rootNode;
//    }

    @RequestMapping( value = "/dataSet/{uid}", method = RequestMethod.GET )
    public @ResponseBody RootNode getDimensionsForDataSet( @PathVariable String uid,
        @RequestParam( value = "links", defaultValue = "true", required = false ) Boolean links,
        Model model, HttpServletResponse response )
        throws WebMessageException
    {
        WebMetadata metadata = new WebMetadata();
        List<String> fields = newArrayList( contextService.getParameterValues( "fields" ) );

        DataSet dataSet = identifiableObjectManager.get( DataSet.class, uid );

        if ( dataSet == null )
        {
            throw new WebMessageException( WebMessageUtils.notFound( "Data set not found: " + uid ) );
        }

        List<DimensionalObject> dimensions = new ArrayList<>();
        dimensions.addAll( dataSet.getCategoryCombo().getCategories().stream()
            .filter( ca -> !ca.isDefault() )
            .collect( Collectors.toList() ) );
        dimensions.addAll( dataSet.getCategoryOptionGroupSets() );

        dimensions = dimensionService.getCanReadObjects( dimensions );

        for ( DimensionalObject dim : dimensions )
        {
            metadata.getDimensions().add( dimensionService.getDimensionalObjectCopy( dim.getUid(), true ) );
        }

        if ( links )
        {
            linkService.generateLinks( metadata, false );
        }

        RootNode rootNode = NodeUtils.createMetadata();
        rootNode.addChild( fieldFilterService.toCollectionNode( getEntityClass(),
            new FieldFilterParams( metadata.getDimensions(), fields ) ) );

        return rootNode;
    }
}
