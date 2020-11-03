package com.mass3d.webapi.controller;

import com.mass3d.attribute.Attribute;
import com.mass3d.schema.descriptors.AttributeSchemaDescriptor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping( value = AttributeSchemaDescriptor.API_ENDPOINT )
public class AttributeController
    extends AbstractCrudController<Attribute>
{
}
