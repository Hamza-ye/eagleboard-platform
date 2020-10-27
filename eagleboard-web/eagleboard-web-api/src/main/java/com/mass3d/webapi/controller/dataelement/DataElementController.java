package com.mass3d.webapi.controller.dataelement;

import com.mass3d.dataelement.DataElement;
import com.mass3d.schema.descriptors.DataElementSchemaDescriptor;
import com.mass3d.webapi.controller.AbstractCrudController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = DataElementSchemaDescriptor.API_ENDPOINT)
public class DataElementController
    extends AbstractCrudController<DataElement> {

}
