package com.mass3d.program.variable;

import com.mass3d.parser.expression.CommonExpressionVisitor;

/**
 * Program indicator variable: enrollment count
 *
 */
public class vEnrollmentCount
    extends ProgramDoubleVariable
{
    @Override
    public Object getSql( CommonExpressionVisitor visitor )
    {
        return "distinct pi";
    }
}
