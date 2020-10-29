package com.mass3d.commons.action;

import com.mass3d.indicator.IndicatorGroup;
import com.mass3d.indicator.IndicatorService;

import com.opensymphony.xwork2.Action;

public class GetIndicatorGroupAction
    implements Action
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
    // Input & output
    // -------------------------------------------------------------------------

    private Integer id;

    public void setId( Integer id )
    {
        this.id = id;
    }

    private IndicatorGroup indicatorGroup;

    public IndicatorGroup getIndicatorGroup()
    {
        return indicatorGroup;
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
            indicatorGroup = indicatorService.getIndicatorGroup( id );
            memberCount = indicatorGroup.getMembers().size();
        }

        return SUCCESS;
    }
}
