package com.mass3d.program.variable;

import com.mass3d.parser.expression.CommonExpressionVisitor;

/**
 * Program indicator variable: event count
 *
 */
public class vEventCount
    extends ProgramDoubleVariable
{
    @Override
    public Object getSql( CommonExpressionVisitor visitor )
    {
        return "distinct psi";
    }
}
