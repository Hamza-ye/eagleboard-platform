package com.mass3d.webapi.controller.option;

import com.mass3d.option.OptionGroupSet;
import com.mass3d.schema.descriptors.OptionGroupSetSchemaDescriptor;
import com.mass3d.webapi.controller.AbstractCrudController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping( value = OptionGroupSetSchemaDescriptor.API_ENDPOINT )
public class OptionGroupSetController
    extends AbstractCrudController<OptionGroupSet>
{
}
