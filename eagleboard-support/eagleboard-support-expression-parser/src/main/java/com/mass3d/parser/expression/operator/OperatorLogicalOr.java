package com.mass3d.parser.expression.operator;

import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.ExprContext;

import org.hisp.dhis.antlr.operator.AntlrOperatorLogicalOr;
import com.mass3d.parser.expression.CommonExpressionVisitor;
import com.mass3d.parser.expression.ExpressionItem;

/**
 * Logical operator: Or
 * <pre>
 *
 * Truth table (same as for SQL):
 *
 *       A      B    A or B
 *     -----  -----  ------
 *     null   null    null
 *     null   false   null
 *     null   true    true
 *
 *     false  null    null
 *     false  false   false
 *     false  true    true
 *
 *     true   null    true
 *     true   false   true
 *     true   true    true
 * </pre>
 *
 */
public class OperatorLogicalOr
    extends AntlrOperatorLogicalOr
    implements ExpressionItem
{
    @Override
    public Object evaluateAllPaths( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        Boolean value = visitor.castBooleanVisit( ctx.expr( 0 ) );
        Boolean value1 = visitor.castBooleanVisit( ctx.expr( 1 ) );

        if ( value == null )
        {
            value = value1;

            if ( value != null && !value )
            {
                value = null;
            }
        }
        else if ( !value )
        {
            value = value1;
        }

        return value;
    }

    @Override
    public Object getSql( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        return visitor.castStringVisit( ctx.expr( 0 ) )
            + " or " + visitor.castStringVisit( ctx.expr( 1 ) );
    }
}
