package com.mass3d.program.variable;

import com.mass3d.parser.expression.CommonExpressionVisitor;

/**
 * Program indicator variable: event date (also used for execution date)
 *
 */
public class vEventDate
    extends ProgramDateVariable
{
    @Override
    public Object getSql( CommonExpressionVisitor visitor )
    {
        return visitor.getStatementBuilder().getProgramIndicatorEventColumnSql( 
            null, "executiondate", visitor.getReportingStartDate(), visitor.getReportingEndDate(), 
            visitor.getProgramIndicator() );
    }
}
