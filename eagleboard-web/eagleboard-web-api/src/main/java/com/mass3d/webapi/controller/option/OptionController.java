package com.mass3d.webapi.controller.option;

import com.mass3d.option.Option;
import com.mass3d.schema.descriptors.OptionSchemaDescriptor;
import com.mass3d.webapi.controller.AbstractCrudController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping( value = OptionSchemaDescriptor.API_ENDPOINT )
public class OptionController
    extends AbstractCrudController<Option>
{
}
