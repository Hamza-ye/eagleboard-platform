package com.mass3d.parser.expression.function;

import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.ExprContext;

import java.util.List;
import java.util.stream.Collectors;
import com.mass3d.parser.expression.CommonExpressionVisitor;
import com.mass3d.parser.expression.ExpressionItem;

/**
 * Function firstNonNull
 *
 */
public class FunctionFirstNonNull
    implements ExpressionItem
{
    @Override
    public Object evaluate( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        for ( ExprContext c : ctx.expr() )
        {
            Object value = visitor.visitAllowingNulls( c );

            if ( value != null )
            {
                return value;
            }
        }
        return null;
    }

    @Override
    public Object evaluateAllPaths( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        List<Object> values = ctx.expr().stream()
            .map( c -> visitor.visitAllowingNulls( c ) )
            .collect( Collectors.toList() );

        for ( Object value : values )
        {
            if ( value != null )
            {
                return value;
            }
        }
        return null;
    }

    @Override
    public Object getSql( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        String args = ctx.expr().stream()
            .map( c -> visitor.castStringVisit( c ) )
            .collect( Collectors.joining( "," ) );

        return "coalesce(" + args + ")";
    }
}
