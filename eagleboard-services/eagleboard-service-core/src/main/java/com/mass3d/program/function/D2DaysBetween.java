package com.mass3d.program.function;

/**
 * Program indicator function: d2 days between
 *
 */
public class D2DaysBetween
    extends ProgramBetweenFunction
{
    @Override
    public Object getSqlBetweenDates( String startDate, String endDate )
    {
        return "(cast(" + endDate + " as date) - cast(" + startDate + " as date))";
    }
}
