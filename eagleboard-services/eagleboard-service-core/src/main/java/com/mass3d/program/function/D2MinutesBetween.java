package com.mass3d.program.function;

/**
 * Program indicator function: d2 minutes between
 *
 */
public class D2MinutesBetween
    extends ProgramBetweenFunction
{
    @Override
    public Object getSqlBetweenDates( String startDate, String endDate )
    {
        return "(extract(epoch from (cast(" + endDate + " as timestamp) - cast(" + startDate + " as timestamp))) / 60)";
    }
}
