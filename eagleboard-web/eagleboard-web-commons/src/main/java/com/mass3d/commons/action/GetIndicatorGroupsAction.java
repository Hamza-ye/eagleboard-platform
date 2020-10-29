package com.mass3d.commons.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.mass3d.indicator.IndicatorGroup;
import com.mass3d.indicator.IndicatorService;
import com.mass3d.paging.ActionPagingSupport;
import com.mass3d.system.filter.IndicatorGroupWithoutGroupSetFilter;
import com.mass3d.commons.filter.FilterUtils;
import com.mass3d.common.IdentifiableObjectUtils;

public class GetIndicatorGroupsAction
    extends ActionPagingSupport<IndicatorGroup>
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

    public boolean filterNoGroupSet = false;

    public void setFilterNoGroupSet( boolean filterNoGroupSet )
    {
        this.filterNoGroupSet = filterNoGroupSet;
    }

    private List<IndicatorGroup> indicatorGroups;

    public List<IndicatorGroup> getIndicatorGroups()
    {
        return indicatorGroups;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    @Override
    public String execute()
    {
        indicatorGroups = new ArrayList<>( indicatorService.getAllIndicatorGroups() );

        if ( filterNoGroupSet )
        {
            FilterUtils.filter( indicatorGroups, new IndicatorGroupWithoutGroupSetFilter() );
        }

        if ( key != null )
        {
            indicatorGroups = IdentifiableObjectUtils.filterNameByKey( indicatorGroups, key, true );
        }

        Collections.sort( indicatorGroups );

        if ( usePaging )
        {
            this.paging = createPaging( indicatorGroups.size() );

            indicatorGroups = indicatorGroups.subList( paging.getStartPos(), paging.getEndPos() );
        }

        return SUCCESS;
    }
}
