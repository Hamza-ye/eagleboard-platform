package com.mass3d.parser.expression.function;

import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.ExprContext;

import java.util.stream.Collectors;
import com.mass3d.parser.expression.CommonExpressionVisitor;

/**
 * Function greatest
 *
 */
public class FunctionGreatest
    extends FunctionGreatestOrLeast
{
    @Override
    public Object evaluate( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        return greatestOrLeast( ctx.expr(), visitor, 1.0 );
    }

    @Override
    public Object getSql( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        String args = ctx.expr().stream()
            .map( c -> visitor.castStringVisit( c ) )
            .collect( Collectors.joining( "," ) );

        return "greatest(" + args + ")";
    }
}
