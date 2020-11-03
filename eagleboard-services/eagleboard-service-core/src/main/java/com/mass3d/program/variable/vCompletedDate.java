package com.mass3d.program.variable;

import com.mass3d.parser.expression.CommonExpressionVisitor;
import com.mass3d.program.AnalyticsType;

public class vCompletedDate extends ProgramDateVariable
{
    @Override
    public Object getSql( CommonExpressionVisitor visitor )
    {
        if ( AnalyticsType.EVENT == visitor.getProgramIndicator().getAnalyticsType() )
        {
            return "completeddate";
        }

        return visitor.getStatementBuilder().getProgramIndicatorEventColumnSql(
                null, "completeddate", visitor.getReportingStartDate(),
                visitor.getReportingStartDate(), visitor.getProgramIndicator() );
    }
}
