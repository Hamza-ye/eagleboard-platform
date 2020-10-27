package com.mass3d.webapi.controller;

import com.mass3d.constant.Constant;
import com.mass3d.schema.descriptors.ConstantSchemaDescriptor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping( value = ConstantSchemaDescriptor.API_ENDPOINT )
public class ConstantController
    extends AbstractCrudController<Constant>
{
}
