package com.mass3d.commons.action;

import com.mass3d.dataelement.DataElementGroupSet;
import com.mass3d.dataelement.DataElementService;

import com.opensymphony.xwork2.Action;

public class GetDataElementGroupSetAction
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

    // -------------------------------------------------------------------------
    // Input & Output
    // -------------------------------------------------------------------------

    private Integer id;

    public void setId( Integer id )
    {
        this.id = id;
    }

    private DataElementGroupSet dataElementGroupSet;

    public DataElementGroupSet getDataElementGroupSet()
    {
        return dataElementGroupSet;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    @Override
    public String execute()
    {
        if ( id != null )
        {
            dataElementGroupSet = dataElementService.getDataElementGroupSet( id );
        }

        return SUCCESS;
    }
}