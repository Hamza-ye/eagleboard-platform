package com.mass3d.webapi.controller.category;

import com.mass3d.category.CategoryOptionGroup;
import com.mass3d.schema.descriptors.CategoryOptionGroupSchemaDescriptor;
import com.mass3d.webapi.controller.AbstractCrudController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping( value = CategoryOptionGroupSchemaDescriptor.API_ENDPOINT )
public class CategoryOptionGroupController
    extends AbstractCrudController<CategoryOptionGroup>
{
}
