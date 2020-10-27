package com.mass3d.parser.expression.literal;

import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.BooleanLiteralContext;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.StringLiteralContext;

import org.hisp.dhis.antlr.AntlrExprLiteral;
import org.hisp.dhis.parser.expression.antlr.ExpressionParser;

/**
 * Gets literal values from an ANTLR parse tree to regenerate the expression.
 */
public class RegenerateLiteral
    implements AntlrExprLiteral
{
    @Override
    public Object getNumericLiteral( ExpressionParser.NumericLiteralContext ctx )
    {
        return ctx.getText();
    }

    @Override
    public Object getStringLiteral( StringLiteralContext ctx )
    {
        return ctx.getText();
    }

    @Override
    public Object getBooleanLiteral( BooleanLiteralContext ctx )
    {
        return ctx.getText();
    }
}
