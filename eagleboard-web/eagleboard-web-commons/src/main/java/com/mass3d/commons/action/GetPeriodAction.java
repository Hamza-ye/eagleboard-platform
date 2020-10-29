package com.mass3d.commons.action;

import com.mass3d.period.Period;
import com.mass3d.period.PeriodService;

import com.opensymphony.xwork2.Action;

public class GetPeriodAction
    implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private PeriodService periodService;

    public void setPeriodService( PeriodService periodService )
    {
        this.periodService = periodService;
    }

    // -------------------------------------------------------------------------
    // Input & Output
    // -------------------------------------------------------------------------

    private Integer id;

    public void setId( Integer id )
    {
        this.id = id;
    }

    private Period period;

    public Period getPeriod()
    {
        return period;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    @Override
    public String execute()
    {
        if ( id != null )
        {
            period = periodService.getPeriod( id );
        }

        return SUCCESS;
    }
}
