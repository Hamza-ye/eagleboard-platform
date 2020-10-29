package com.mass3d.commons.action;

import java.util.ArrayList;
import java.util.List;

import com.mass3d.paging.ActionPagingSupport;
import com.mass3d.period.PeriodService;
import com.mass3d.period.PeriodType;

public class GetPeriodTypesAction
    extends ActionPagingSupport<PeriodType>
{
    // -------------------------------------------------------------------------
    // Dependency
    // -------------------------------------------------------------------------

    private PeriodService periodService;

    public void setPeriodService( PeriodService periodService )
    {
        this.periodService = periodService;
    }

    // -------------------------------------------------------------------------
    // Output
    // -------------------------------------------------------------------------

    private List<PeriodType> periodTypes;

    public List<PeriodType> getPeriodTypes()
    {
        return periodTypes;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    @Override
    public String execute()
        throws Exception
    {
        periodTypes = new ArrayList<>( periodService.getAllPeriodTypes() );

        if ( usePaging )
        {
            this.paging = createPaging( periodTypes.size() );

            periodTypes = periodTypes.subList( paging.getStartPos(), paging.getEndPos() );
        }

        return SUCCESS;
    }
}
