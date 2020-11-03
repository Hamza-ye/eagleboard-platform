package com.mass3d.program.variable;

import com.mass3d.parser.expression.CommonExpressionVisitor;

/**
 * Program indicator variable: enrolment date
 *
 */
public class vEnrollmentDate
    extends ProgramDateVariable
{
    @Override
    public Object getSql( CommonExpressionVisitor visitor )
    {
        return "enrollmentdate";
    }
}
