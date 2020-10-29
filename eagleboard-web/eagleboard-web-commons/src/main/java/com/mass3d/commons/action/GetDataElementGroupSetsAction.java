package com.mass3d.commons.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.mass3d.dataelement.DataElementGroupSet;
import com.mass3d.dataelement.DataElementService;
import com.mass3d.paging.ActionPagingSupport;
import com.mass3d.common.IdentifiableObjectUtils;

public class GetDataElementGroupSetsAction
    extends ActionPagingSupport<DataElementGroupSet>
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

    private String key;

    public void setKey( String key )
    {
        this.key = key;
    }

    private List<DataElementGroupSet> dataElementGroupSets;

    public List<DataElementGroupSet> getDataElementGroupSets()
    {
        return dataElementGroupSets;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    @Override
    public String execute()
        throws Exception
    {
        dataElementGroupSets = new ArrayList<>( dataElementService.getAllDataElementGroupSets() );

        if ( key != null )
        {
            dataElementGroupSets = IdentifiableObjectUtils.filterNameByKey( dataElementGroupSets, key, true );
        }

        Collections.sort( dataElementGroupSets );

        if ( usePaging )
        {
            this.paging = createPaging( dataElementGroupSets.size() );

            dataElementGroupSets = dataElementGroupSets.subList( paging.getStartPos(), paging.getEndPos() );
        }

        return SUCCESS;
    }
}