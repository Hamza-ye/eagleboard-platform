package com.mass3d.parser.expression;

import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.ExprContext;

/**
 * Applies a method in an expression item class.
 *
 */
@FunctionalInterface
public interface ExpressionItemMethod
{
    /**
     * Invokes a method in an expression item
     *
     * @param item the item to evaluate
     * @param ctx the expression context in which to evaluate the item
     * @param visitor the visitor class for supporting methods
     * @return the method result from the expression item class
     */
    Object apply(ExpressionItem item, ExprContext ctx, CommonExpressionVisitor visitor);
}
