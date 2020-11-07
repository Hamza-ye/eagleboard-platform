package com.mass3d.commons.action;

import com.opensymphony.xwork2.Action;
import com.mass3d.category.CategoryOptionGroup;
import com.mass3d.category.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 
 * @version $ GetCategoryOptionGroupsAction.java Feb 13, 2014 6:17:33 PM $
 */
public class GetCategoryOptionGroupsAction
    implements Action
{
    @Autowired
    private CategoryService dataElementCategoryService;

    // -------------------------------------------------------------------------
    // Output
    // -------------------------------------------------------------------------

    private List<CategoryOptionGroup> categoryOptionGroups;

    public List<CategoryOptionGroup> getCategoryOptionGroups()
    {
        return categoryOptionGroups;
    }

    // -------------------------------------------------------------------------
    // Action
    // -------------------------------------------------------------------------

    @Override
    public String execute()
    {
        categoryOptionGroups = new ArrayList<>(
            dataElementCategoryService.getAllCategoryOptionGroups() );

        Collections.sort( categoryOptionGroups );

        return SUCCESS;
    }
}
