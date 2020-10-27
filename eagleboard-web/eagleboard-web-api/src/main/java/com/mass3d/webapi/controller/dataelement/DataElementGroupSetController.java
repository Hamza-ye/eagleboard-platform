package com.mass3d.webapi.controller.dataelement;

import com.mass3d.dataelement.DataElementGroupSet;
import com.mass3d.schema.descriptors.DataElementGroupSetSchemaDescriptor;
import com.mass3d.webapi.controller.AbstractCrudController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping( value = DataElementGroupSetSchemaDescriptor.API_ENDPOINT )
public class DataElementGroupSetController
    extends AbstractCrudController<DataElementGroupSet>
{
}
