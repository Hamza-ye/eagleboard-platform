package com.mass3d.webapi.controller;

import com.mass3d.common.DhisApiVersion;
import com.mass3d.render.ObjectValueTypeRenderingOption;
import com.mass3d.render.StaticRenderingConfiguration;
import com.mass3d.webapi.mvc.annotation.ApiVersion;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping( value = "/staticConfiguration/" )
@ApiVersion( { DhisApiVersion.DEFAULT, DhisApiVersion.ALL } )
public class StaticRenderingConfigurationController
{

    /**
     * Returns the constraints of ValueType renderingTypes defined in the StaticRenderingConfiguration
     * @return a Set of rules representing application constraints for ValueType/RenderingType combinations
     */
    @GetMapping( value = "renderingOptions" )
    public Set<ObjectValueTypeRenderingOption> getMapping()
    {
        return StaticRenderingConfiguration.RENDERING_OPTIONS_MAPPING;
    }

}
