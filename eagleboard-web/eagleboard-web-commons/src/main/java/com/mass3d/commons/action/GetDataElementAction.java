package com.mass3d.commons.action;

import com.mass3d.dataelement.DataElement;
import com.mass3d.dataelement.DataElementService;

import com.opensymphony.xwork2.Action;

public class GetDataElementAction
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
    // Input
    // -------------------------------------------------------------------------

    private Integer id;

    public void setId( Integer id )
    {
        this.id = id;
    }

    // -------------------------------------------------------------------------
    // Output
    // -------------------------------------------------------------------------

    private DataElement dataElement;

    public DataElement getDataElement()
    {
        return dataElement;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    @Override
    public String execute()
    {
        if ( id != null )
        {
            dataElement = dataElementService.getDataElement( id );
        }

        return SUCCESS;
    }
}
