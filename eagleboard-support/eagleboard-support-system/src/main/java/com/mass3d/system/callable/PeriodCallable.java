package com.mass3d.system.callable;

import java.util.concurrent.ExecutionException;
import com.mass3d.common.IdScheme;
import com.mass3d.period.Period;
import com.mass3d.period.PeriodService;

public class PeriodCallable
    extends IdentifiableObjectCallable<Period>
{
    private PeriodService periodService;

    public PeriodCallable( PeriodService periodService, IdScheme idScheme, String id )
    {
        super( null, Period.class, idScheme, id );
        this.periodService = periodService;
    }

    @Override
    public Period call()
        throws ExecutionException
    {
        return periodService.reloadIsoPeriodInStatelessSession( id );
    }

    @Override
    public PeriodCallable setId( String id )
    {
        this.id = id;
        return this;
    }
}
