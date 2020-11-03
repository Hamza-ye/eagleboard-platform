package com.mass3d.program.variable;

import com.mass3d.parser.expression.CommonExpressionVisitor;

/**
 * Program indicator variable: tei count
 *
 */
public class vTeiCount
    extends ProgramDoubleVariable
{
    @Override
    public Object getSql( CommonExpressionVisitor visitor )
    {
        return "distinct tei";
    }
}
