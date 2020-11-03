package com.mass3d.program.function;

/**
 * Program indicator function: d2 weeks between
 *
 */
public class D2WeeksBetween
    extends ProgramBetweenFunction
{
    @Override
    public Object getSqlBetweenDates( String startDate, String endDate )
    {
        return "((cast(" + endDate + " as date) - cast(" + startDate + " as date))/7)";
    }
}
