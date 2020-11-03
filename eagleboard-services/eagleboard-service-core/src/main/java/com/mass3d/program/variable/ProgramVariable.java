package com.mass3d.program.variable;

import com.mass3d.parser.expression.CommonExpressionVisitor;

/**
 * Program variable interface, used by classes that implement
 * the logic for each program variable.
 *
 */
public interface ProgramVariable
{
    /**
     * Finds the default value of program indicator variable.
     * it's just to check for validity.
     *
     * @return the value of the variable
     */
    Object defaultVariableValue();

    /**
     * Generates the SQL for a program indicator variable.
     *
     * @param visitor the tree visitor
     * @return the generated SQL (as a String) for the function
     */
    Object getSql(CommonExpressionVisitor visitor);
}
