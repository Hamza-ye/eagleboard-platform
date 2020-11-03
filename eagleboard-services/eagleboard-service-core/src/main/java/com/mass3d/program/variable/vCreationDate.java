package com.mass3d.program.variable;

import com.mass3d.parser.expression.CommonExpressionVisitor;

/**
 * Program indicator variable: creation date
 *
 */
public class vCreationDate
    extends ProgramDateVariable
{
    @Override
    public Object getSql( CommonExpressionVisitor visitor )
    {
        return visitor.getStatementBuilder().getProgramIndicatorEventColumnSql(
            null, "created", visitor.getReportingStartDate(),
            visitor.getReportingStartDate(), visitor.getProgramIndicator() );
    }
}
