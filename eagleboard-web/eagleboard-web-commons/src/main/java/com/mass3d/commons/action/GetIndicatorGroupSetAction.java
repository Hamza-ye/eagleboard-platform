package com.mass3d.commons.action;

import com.mass3d.indicator.IndicatorGroupSet;
import com.mass3d.indicator.IndicatorService;

import com.opensymphony.xwork2.Action;

public class GetIndicatorGroupSetAction
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
    // Input & Output
    // -------------------------------------------------------------------------

    private Integer id;

    public void setId( Integer id )
    {
        this.id = id;
    }

    private IndicatorGroupSet indicatorGroupSet;

    public IndicatorGroupSet getIndicatorGroupSet()
    {
        return indicatorGroupSet;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    @Override
    public String execute()
    {
        if ( id != null )
        {
            indicatorGroupSet = indicatorService.getIndicatorGroupSet( id );
        }

        return SUCCESS;
    }
}
