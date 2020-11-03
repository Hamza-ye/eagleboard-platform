package com.mass3d.program.function;

/**
 * Program indicator function: d2 months between
 *
 */
public class D2MonthsBetween
    extends ProgramBetweenFunction
{
    @Override
    public Object getSqlBetweenDates( String startDate, String endDate )
    {
        return "((date_part('year',age(cast(" + endDate + " as date), cast(" + startDate + " as date)))) * 12 + " +
               "date_part('month',age(cast(" + endDate + " as date), cast(" + startDate + " as date))))";
    }
}
