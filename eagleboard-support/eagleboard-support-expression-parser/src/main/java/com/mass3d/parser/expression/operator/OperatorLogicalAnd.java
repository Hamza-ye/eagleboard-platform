package com.mass3d.parser.expression.operator;

import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.ExprContext;

import org.hisp.dhis.antlr.operator.AntlrOperatorLogicalAnd;
import com.mass3d.parser.expression.CommonExpressionVisitor;
import com.mass3d.parser.expression.ExpressionItem;

/**
 * Logical operator: And
 * <pre>
 *
 * Truth table (same as for SQL):
 *
 *       A      B    A and B
 *     -----  -----  -------
 *     null   null    null
 *     null   false   null
 *     null   true    null
 *
 *     false  null    false
 *     false  false   false
 *     false  true    false
 *
 *     true   null    null
 *     true   false   false
 *     true   true    true
 * </pre>
 *
 */
public class OperatorLogicalAnd
    extends AntlrOperatorLogicalAnd
    implements ExpressionItem
{
    @Override
    public Object evaluateAllPaths( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        Boolean value0 = visitor.castBooleanVisit( ctx.expr( 0 ) );
        Boolean value1 = visitor.castBooleanVisit( ctx.expr( 1 ) );

        return value0 != null && value0 ? value1 : true;
    }

    @Override
    public Object getSql( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        return visitor.castStringVisit( ctx.expr( 0 ) )
            + " and " + visitor.castStringVisit( ctx.expr( 1 ) );
    }
}
