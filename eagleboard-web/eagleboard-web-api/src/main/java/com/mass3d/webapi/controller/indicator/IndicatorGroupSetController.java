package com.mass3d.webapi.controller.indicator;

import com.mass3d.indicator.IndicatorGroupSet;
import com.mass3d.schema.descriptors.IndicatorGroupSetSchemaDescriptor;
import com.mass3d.webapi.controller.AbstractCrudController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping( value = IndicatorGroupSetSchemaDescriptor.API_ENDPOINT )
public class IndicatorGroupSetController
    extends AbstractCrudController<IndicatorGroupSet>
{
}
