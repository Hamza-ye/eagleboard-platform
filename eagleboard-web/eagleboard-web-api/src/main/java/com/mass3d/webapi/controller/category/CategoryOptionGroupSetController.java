package com.mass3d.webapi.controller.category;

import com.mass3d.category.CategoryOptionGroupSet;
import com.mass3d.schema.descriptors.CategoryOptionGroupSetSchemaDescriptor;
import com.mass3d.webapi.controller.AbstractCrudController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping( value = CategoryOptionGroupSetSchemaDescriptor.API_ENDPOINT )
public class CategoryOptionGroupSetController
    extends AbstractCrudController<CategoryOptionGroupSet>
{
}
