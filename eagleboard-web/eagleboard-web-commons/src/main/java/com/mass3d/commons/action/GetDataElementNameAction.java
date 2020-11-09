package com.mass3d.commons.action;

import com.mass3d.category.CategoryOptionCombo;
import com.mass3d.category.CategoryService;
import com.mass3d.dataelement.DataElement;
import com.mass3d.dataelement.DataElementService;

import com.opensymphony.xwork2.Action;

public class GetDataElementNameAction
    implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private DataElementService dataElementService;

    public void setDataElementService( DataElementService dataElementService )
    {
        this.dataElementService = dataElementService;
    }

    private CategoryService categoryService;

    public void setCategoryService( CategoryService categoryService )
    {
        this.categoryService = categoryService;
    }

    // -------------------------------------------------------------------------
    // Input
    // -------------------------------------------------------------------------

    private Integer dataElementId;

    public void setDataElementId( Integer dataElementId )
    {
        this.dataElementId = dataElementId;
    }

    private Integer categoryOptionComboId;

    public void setCategoryOptionComboId( Integer categoryOptionComboId )
    {
        this.categoryOptionComboId = categoryOptionComboId;
    }

    // -------------------------------------------------------------------------
    // Output
    // -------------------------------------------------------------------------

    private String name;

    public String getName()
    {
        return name;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    @Override
    public String execute()
    {
        if ( dataElementId != null && categoryOptionComboId != null )
        {
            DataElement dataElement = dataElementService.getDataElement( dataElementId );

            CategoryOptionCombo categoryOptionCombo = categoryService
                .getCategoryOptionCombo( categoryOptionComboId );

            name = dataElement.getName();// + " " + categoryOptionCombo.getName();
        }

        return SUCCESS;
    }
}
