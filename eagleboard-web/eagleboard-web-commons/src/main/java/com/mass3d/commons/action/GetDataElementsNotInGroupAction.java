package com.mass3d.commons.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.mass3d.dataelement.DataElement;
import com.mass3d.dataelement.DataElementGroup;
import com.mass3d.dataelement.DataElementService;
import com.mass3d.paging.ActionPagingSupport;

public class GetDataElementsNotInGroupAction
    extends ActionPagingSupport<DataElement>
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

    private Integer groupId;

    public void setId( Integer groupId )
    {
        this.groupId = groupId;
    }

    // -------------------------------------------------------------------------
    // Output
    // -------------------------------------------------------------------------

    private List<DataElement> groupMembers = new ArrayList<>();

    public List<DataElement> getGroupMembers()
    {
        return groupMembers;
    }

    private List<DataElement> dataElements = new ArrayList<>();

    public List<DataElement> getDataElements()
    {
        return dataElements;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    @Override
    public String execute()
    {
        // ---------------------------------------------------------------------
        // Get group members
        // ---------------------------------------------------------------------

        if ( groupId != null )
        {
            DataElementGroup group = dataElementService.getDataElementGroup( groupId );

            groupMembers = new ArrayList<>( group.getMembers() );

            Collections.sort( groupMembers );
        }

        // ---------------------------------------------------------------------
        // Get available elements
        // ---------------------------------------------------------------------

        dataElements = new ArrayList<>( dataElementService.getAllDataElements() );

        dataElements.removeAll( groupMembers );

        Collections.sort( dataElements );

        if ( usePaging )
        {
            this.paging = createPaging( dataElements.size() );

            dataElements = dataElements.subList( paging.getStartPos(), paging.getEndPos() );
        }

        return SUCCESS;
    }
}
