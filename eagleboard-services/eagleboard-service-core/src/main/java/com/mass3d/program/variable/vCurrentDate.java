package com.mass3d.program.variable;

import com.mass3d.parser.expression.CommonExpressionVisitor;
import com.mass3d.util.DateUtils;

/**
 * Program indicator variable: current date
 *
 */
public class vCurrentDate
    extends ProgramDateVariable
{
    @Override
    public Object getSql( CommonExpressionVisitor visitor )
    {
        return visitor.getStatementBuilder().encode( DateUtils.getLongDateString() );
    }
}
