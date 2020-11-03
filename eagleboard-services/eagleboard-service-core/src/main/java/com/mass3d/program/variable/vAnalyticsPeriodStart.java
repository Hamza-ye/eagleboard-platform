package com.mass3d.program.variable;

import com.mass3d.parser.expression.CommonExpressionVisitor;
import com.mass3d.util.DateUtils;

/**
 * Program indicator variable: analytics period start
 *
 */
public class vAnalyticsPeriodStart
    extends ProgramDateVariable
{
    @Override
    public Object getSql( CommonExpressionVisitor visitor )
    {
        return visitor.getStatementBuilder().encode( DateUtils.getSqlDateString( visitor.getReportingStartDate() ) );
    }
}
