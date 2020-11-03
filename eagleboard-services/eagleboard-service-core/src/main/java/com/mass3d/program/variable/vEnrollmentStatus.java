package com.mass3d.program.variable;

import com.mass3d.parser.expression.CommonExpressionVisitor;

/**
 * Program indicator variable: enrollment status
 *
 */
public class vEnrollmentStatus
    implements ProgramVariable
{
    @Override
    public final Object defaultVariableValue()
    {
        return "COMPLETED";
    }

    @Override
    public Object getSql( CommonExpressionVisitor visitor )
    {
        return "enrollmentstatus";
    }
}
