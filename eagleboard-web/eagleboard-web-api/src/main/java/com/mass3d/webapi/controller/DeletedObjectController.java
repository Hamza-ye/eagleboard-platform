package com.mass3d.webapi.controller;

import com.google.common.collect.Lists;
import com.mass3d.common.DhisApiVersion;
import com.mass3d.deletedobject.DeletedObject;
import com.mass3d.deletedobject.DeletedObjectQuery;
import com.mass3d.deletedobject.DeletedObjectService;
import com.mass3d.fieldfilter.FieldFilterParams;
import com.mass3d.fieldfilter.FieldFilterService;
import com.mass3d.node.NodeUtils;
import com.mass3d.node.Preset;
import com.mass3d.node.types.RootNode;
import com.mass3d.webapi.mvc.annotation.ApiVersion;
import com.mass3d.webapi.service.ContextService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping( value = "/deletedObjects" )
@ApiVersion( { DhisApiVersion.DEFAULT, DhisApiVersion.ALL } )
public class DeletedObjectController
{
    private final FieldFilterService fieldFilterService;
    private final DeletedObjectService deletedObjectService;
    private final ContextService contextService;

    public DeletedObjectController( FieldFilterService fieldFilterService, DeletedObjectService deletedObjectService,
        ContextService contextService )
    {
        this.fieldFilterService = fieldFilterService;
        this.deletedObjectService = deletedObjectService;
        this.contextService = contextService;
    }

    @GetMapping
    @PreAuthorize( "hasRole('ALL')" )
    public RootNode getDeletedObjects( DeletedObjectQuery query )
    {
        List<String> fields = Lists.newArrayList( contextService.getParameterValues( "fields" ) );
        int totalDeletedObjects = deletedObjectService.countDeletedObjects( query );
        query.setTotal( totalDeletedObjects );

        if ( fields.isEmpty() )
        {
            fields.addAll( Preset.ALL.getFields() );
        }

        List<DeletedObject> deletedObjects = deletedObjectService.getDeletedObjects( query );

        RootNode rootNode = NodeUtils.createMetadata();

        if ( !query.isSkipPaging() )
        {
            query.setTotal( totalDeletedObjects );
            rootNode.addChild( NodeUtils.createPager( query.getPager() ) );
        }

        rootNode.addChild( fieldFilterService.toCollectionNode( DeletedObject.class, new FieldFilterParams( deletedObjects, fields ) ) );

        return rootNode;
    }
}
