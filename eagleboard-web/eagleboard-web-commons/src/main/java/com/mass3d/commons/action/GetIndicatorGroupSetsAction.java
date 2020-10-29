package com.mass3d.commons.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.mass3d.indicator.IndicatorGroupSet;
import com.mass3d.indicator.IndicatorService;
import com.mass3d.paging.ActionPagingSupport;
import com.mass3d.common.IdentifiableObjectUtils;

public class GetIndicatorGroupSetsAction
    extends ActionPagingSupport<IndicatorGroupSet>
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private IndicatorService indicatorService;

    public void setIndicatorService( IndicatorService indicatorService )
    {
        this.indicatorService = indicatorService;
    }

    // -------------------------------------------------------------------------
    // Input & Output
    // -------------------------------------------------------------------------

    private String key;

    public void setKey( String key )
    {
        this.key = key;
    }

    private List<IndicatorGroupSet> indicatorGroupSets;

    public List<IndicatorGroupSet> getIndicatorGroupSets()
    {
        return indicatorGroupSets;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    @Override
    public String execute()
        throws Exception
    {
        indicatorGroupSets = new ArrayList<>( indicatorService.getAllIndicatorGroupSets() );

        if ( key != null )
        {
            indicatorGroupSets = IdentifiableObjectUtils.filterNameByKey( indicatorGroupSets, key, true );
        }

        Collections.sort( indicatorGroupSets );

        if ( usePaging )
        {
            this.paging = createPaging( indicatorGroupSets.size() );

            indicatorGroupSets = indicatorGroupSets.subList( paging.getStartPos(), paging.getEndPos() );
        }

        return SUCCESS;
    }
}
