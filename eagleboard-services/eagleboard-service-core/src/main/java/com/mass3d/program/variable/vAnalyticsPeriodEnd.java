package com.mass3d.program.variable;

import com.mass3d.parser.expression.CommonExpressionVisitor;
import com.mass3d.util.DateUtils;

/**
 * Program indicator variable: analytics period end
 *
 */
public class vAnalyticsPeriodEnd
    extends ProgramDateVariable
{
    @Override
    public Object getSql( CommonExpressionVisitor visitor )
    {
        return visitor.getStatementBuilder().encode( DateUtils.getSqlDateString( visitor.getReportingEndDate() ) );
    }
}
