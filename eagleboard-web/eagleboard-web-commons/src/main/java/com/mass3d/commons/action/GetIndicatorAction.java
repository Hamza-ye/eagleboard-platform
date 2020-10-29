package com.mass3d.commons.action;

import com.mass3d.indicator.Indicator;
import com.mass3d.indicator.IndicatorService;

import com.opensymphony.xwork2.Action;

public class GetIndicatorAction
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

    private Indicator indicator;

    public Indicator getIndicator()
    {
        return indicator;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    @Override
    public String execute()
    {
        if ( id != null )
        {
            indicator = indicatorService.getIndicator( id );
        }

        return SUCCESS;
    }
}
