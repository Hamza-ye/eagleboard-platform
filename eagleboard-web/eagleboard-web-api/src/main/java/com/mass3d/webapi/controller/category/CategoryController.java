package com.mass3d.webapi.controller.category;

import com.mass3d.category.Category;
import com.mass3d.schema.descriptors.CategorySchemaDescriptor;
import com.mass3d.webapi.controller.AbstractCrudController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping( value = CategorySchemaDescriptor.API_ENDPOINT )
public class CategoryController
    extends AbstractCrudController<Category>
{
}
