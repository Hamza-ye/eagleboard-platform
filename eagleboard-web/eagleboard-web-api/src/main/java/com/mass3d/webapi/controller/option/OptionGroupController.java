package com.mass3d.webapi.controller.option;

import com.mass3d.option.OptionGroup;
import com.mass3d.schema.descriptors.OptionGroupSchemaDescriptor;
import com.mass3d.webapi.controller.AbstractCrudController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping( value = OptionGroupSchemaDescriptor.API_ENDPOINT )
public class OptionGroupController
    extends AbstractCrudController<OptionGroup>
{
}
