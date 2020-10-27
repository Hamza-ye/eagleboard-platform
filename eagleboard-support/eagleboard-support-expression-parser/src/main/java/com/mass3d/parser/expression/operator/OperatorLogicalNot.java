package com.mass3d.parser.expression.operator;

import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.ExprContext;

import org.hisp.dhis.antlr.operator.AntlrOperatorLogicalNot;
import com.mass3d.parser.expression.CommonExpressionVisitor;
import com.mass3d.parser.expression.ExpressionItem;

/**
 * Logical operator: Not
 * <pre>
 *
 * Truth table (same as for SQL):
 *
 *       A    not A
 *     -----  -----
 *     null   null
 *     false  true
 *     true   false
 * </pre>
 *
 */
public class OperatorLogicalNot
    extends AntlrOperatorLogicalNot
    implements ExpressionItem
{
    @Override
    public Object getSql( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        return "not " + visitor.castStringVisit( ctx.expr( 0 ) );
    }
}
