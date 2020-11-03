package com.mass3d.program.function;

/**
 * Program indicator function: d2 years between
 *
 */
public class D2YearsBetween
    extends ProgramBetweenFunction
{
    @Override
    public Object getSqlBetweenDates( String startDate, String endDate )
    {
        return "(date_part('year',age(cast(" + endDate + " as date), cast(" + startDate + " as date))))";
    }
}
