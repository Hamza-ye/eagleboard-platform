package com.mass3d.commons.action;

import com.mass3d.dataelement.DataElementGroup;
import com.mass3d.dataelement.DataElementService;

import com.opensymphony.xwork2.Action;

public class GetDataElementGroupAction
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

    private DataElementGroup dataElementGroup;

    public DataElementGroup getDataElementGroup()
    {
        return dataElementGroup;
    }

    private int memberCount;

    public int getMemberCount()
    {
        return memberCount;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    @Override
    public String execute()
    {
        if ( id != null )
        {
            dataElementGroup = dataElementService.getDataElementGroup( id );
            memberCount = dataElementGroup.getMembers().size();
        }

        return SUCCESS;
    }
}
