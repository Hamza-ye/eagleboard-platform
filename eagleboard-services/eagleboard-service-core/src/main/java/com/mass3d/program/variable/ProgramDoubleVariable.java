package com.mass3d.program.variable;

import static com.mass3d.parser.expression.CommonExpressionVisitor.DEFAULT_DOUBLE_VALUE;

/**
 * Program indicator double variable (uses default double for validity checking)
 *
 */
public abstract class ProgramDoubleVariable
    implements ProgramVariable
{
    @Override
    public final Object defaultVariableValue()
    {
        return DEFAULT_DOUBLE_VALUE;
    }
}
