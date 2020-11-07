package com.mass3d.commons.action;

import com.opensymphony.xwork2.Action;
import com.mass3d.category.CategoryOption;
import com.mass3d.category.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GetCategoryOptionsAction
    implements Action
{
    @Autowired
    private CategoryService categoryService;

    // -------------------------------------------------------------------------
    // Output
    // -------------------------------------------------------------------------

    private List<CategoryOption> categoryOptions;

    public List<CategoryOption> getCategoryOptions()
    {
        return categoryOptions;
    }

    // -------------------------------------------------------------------------
    // Action
    // -------------------------------------------------------------------------
    
    @Override
    public String execute()
    {
        categoryOptions = new ArrayList<>( categoryService.getAllCategoryOptions() );
        
        Collections.sort( categoryOptions );
        
        return SUCCESS;
    }
}
