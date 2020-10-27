package com.mass3d.parser.expression.literal;

import static org.apache.commons.lang.StringEscapeUtils.escapeSql;
import static org.apache.commons.text.StringEscapeUtils.unescapeJava;
import static org.hisp.dhis.antlr.AntlrParserUtils.trimQuotes;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.BooleanLiteralContext;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.NumericLiteralContext;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.StringLiteralContext;

import org.hisp.dhis.antlr.AntlrExprLiteral;

/**
 * Gets literal value Strings from an ANTLR parse tree for use in SQL queries.
 */
public class SqlLiteral
    implements AntlrExprLiteral
{
    @Override
    public Object getNumericLiteral( NumericLiteralContext ctx )
    {
        return ctx.getText();
    }

    @Override
    public Object getStringLiteral( StringLiteralContext ctx )
    {
        return "'" + escapeSql( unescapeJava( trimQuotes( ctx.getText() ) ) ) + "'";
    }

    @Override
    public Object getBooleanLiteral( BooleanLiteralContext ctx )
    {
        return ctx.getText();
    }
}
