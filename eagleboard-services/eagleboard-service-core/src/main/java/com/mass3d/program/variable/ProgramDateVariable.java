package com.mass3d.program.variable;

import static com.mass3d.parser.expression.CommonExpressionVisitor.DEFAULT_DATE_VALUE;

/**
 * Program indicator date variable (uses default date for validity checking)
 *
 */
public abstract class ProgramDateVariable
    implements ProgramVariable
{
    @Override
    public final Object defaultVariableValue()
    {
        return DEFAULT_DATE_VALUE;
    }
}
