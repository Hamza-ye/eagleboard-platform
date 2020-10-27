package com.mass3d.webapi.controller.indicator;

import com.mass3d.indicator.IndicatorType;
import com.mass3d.schema.descriptors.IndicatorTypeSchemaDescriptor;
import com.mass3d.webapi.controller.AbstractCrudController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping( value = IndicatorTypeSchemaDescriptor.API_ENDPOINT )
public class IndicatorTypeController
    extends AbstractCrudController<IndicatorType>
{
}
