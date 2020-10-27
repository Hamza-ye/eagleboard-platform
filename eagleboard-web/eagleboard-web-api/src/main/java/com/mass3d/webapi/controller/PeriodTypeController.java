package com.mass3d.webapi.controller;

import java.util.List;
import java.util.stream.Collectors;

import com.mass3d.common.DhisApiVersion;
import com.mass3d.fieldfilter.FieldFilterParams;
import com.mass3d.fieldfilter.FieldFilterService;
import com.mass3d.node.NodeUtils;
import com.mass3d.node.Preset;
import com.mass3d.node.types.RootNode;
import com.mass3d.period.PeriodService;
import com.mass3d.period.RelativePeriodEnum;
import com.mass3d.webapi.mvc.annotation.ApiVersion;
import com.mass3d.webapi.service.ContextService;
import com.mass3d.webapi.webdomain.PeriodTypeDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;

@RestController
@ApiVersion( { DhisApiVersion.DEFAULT, DhisApiVersion.ALL } )
@RequestMapping( value = "/periodTypes" )
public class PeriodTypeController
{
    private final PeriodService periodService;
    private final ContextService contextService;
    private final FieldFilterService fieldFilterService;

    public PeriodTypeController( PeriodService periodService, ContextService contextService, FieldFilterService fieldFilterService )
    {
        this.periodService = periodService;
        this.contextService = contextService;
        this.fieldFilterService = fieldFilterService;
    }

    @GetMapping
    public RootNode getPeriodTypes()
    {
        List<String> fields = Lists.newArrayList( contextService.getParameterValues( "fields" ) );
        List<PeriodTypeDto> periodTypes = periodService.getAllPeriodTypes().stream()
            .map( PeriodTypeDto::new )
            .collect( Collectors.toList() );

        if ( fields.isEmpty() )
        {
            fields.addAll( Preset.ALL.getFields() );
        }

        RootNode rootNode = NodeUtils.createMetadata();
        rootNode.addChild( fieldFilterService.toCollectionNode( PeriodTypeDto.class, new FieldFilterParams( periodTypes, fields ) ) );

        return rootNode;
    }

    @RequestMapping( value = "/relativePeriodTypes", method = RequestMethod.GET, produces = { "application/json", "application/javascript" } )
    public @ResponseBody RelativePeriodEnum[] getRelativePeriodTypes()
    {
        return RelativePeriodEnum.values();
    }
}
