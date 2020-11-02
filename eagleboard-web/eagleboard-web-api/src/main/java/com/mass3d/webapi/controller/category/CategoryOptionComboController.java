package com.mass3d.webapi.controller.category;

import com.mass3d.category.CategoryOptionCombo;
import com.mass3d.schema.descriptors.CategoryOptionComboSchemaDescriptor;
import com.mass3d.webapi.controller.AbstractCrudController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping( value = CategoryOptionComboSchemaDescriptor.API_ENDPOINT )
public class CategoryOptionComboController
    extends AbstractCrudController<CategoryOptionCombo>
{
}
