package com.mass3d.commons.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.mass3d.i18n.I18nFormat;
import com.mass3d.paging.ActionPagingSupport;
import com.mass3d.period.Period;
import com.mass3d.period.PeriodService;
import com.mass3d.period.PeriodType;
import com.mass3d.period.comparator.DescendingPeriodComparator;

public class GetPeriodsAction
    extends ActionPagingSupport<Period>
{
    private final static String ALL = "ALL";

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private PeriodService periodService;

    public void setPeriodService( PeriodService periodService )
    {
        this.periodService = periodService;
    }

    private I18nFormat format;

    public void setFormat( I18nFormat format )
    {
        this.format = format;
    }

    // -------------------------------------------------------------------------
    // Input & Output
    // -------------------------------------------------------------------------

    private String name;

    public void setName( String name )
    {
        this.name = name;
    }

    private List<Period> periods;

    public List<Period> getPeriods()
    {
        return periods;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    @Override
    public String execute()
        throws Exception
    {
        if ( name == null || name.equals( ALL ) )
        {
            List<PeriodType> periodTypes = periodService.getAllPeriodTypes();

            periods = new ArrayList<>();

            for ( PeriodType type : periodTypes )
            {
                periods.addAll( periodService.getPeriodsByPeriodType( type ) );
            }
        }
        else
        {
            PeriodType periodType = periodService.getPeriodTypeByName( name );

            periods = new ArrayList<>( periodService.getPeriodsByPeriodType( periodType ) );
        }

        for ( Period period : periods )
        {
            period.setName( format.formatPeriod( period ) );
        }

        Collections.sort( periods, new DescendingPeriodComparator() );

        if ( usePaging )
        {
            this.paging = createPaging( periods.size() );

            periods = periods.subList( paging.getStartPos(), paging.getEndPos() );
        }

        return SUCCESS;
    }
}
