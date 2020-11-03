package com.mass3d.program.function;

import static org.hisp.dhis.antlr.AntlrParserUtils.castClass;
import static org.hisp.dhis.antlr.AntlrParserUtils.trimQuotes;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.ExprContext;

import com.mass3d.parser.expression.CommonExpressionVisitor;
import com.mass3d.program.ProgramExpressionItem;

/**
 * Program indicator function: d2 condition
 *
 */
public class D2Condition
    extends ProgramExpressionItem
{
    @Override
    public final Object getDescription( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        String testExpression = trimQuotes( ctx.stringLiteral().getText() );

        visitor.getProgramIndicatorService()
            .validate( testExpression, Boolean.class, visitor.getItemDescriptions() );

        Object valueIfTrue = visitor.visit( ctx.expr( 0 ) );
        Object valueIfFalse = visitor.visit( ctx.expr( 1 ) );

        castClass( valueIfTrue.getClass(), valueIfFalse );

        return valueIfTrue;
    }

    @Override
    public Object getSql( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        String testExpression = trimQuotes( ctx.stringLiteral().getText() );

        String testSql = visitor.getProgramIndicatorService().getAnalyticsSql( testExpression,
            visitor.getProgramIndicator(), visitor.getReportingStartDate(), visitor.getReportingEndDate() );

        String valueIfTrue = visitor.castStringVisit( ctx.expr( 0 ) );
        String valueIfFalse = visitor.castStringVisit( ctx.expr( 1 ) );

        return "case when (" + testSql + ") then " + valueIfTrue + " else " + valueIfFalse + " end";
    }
}
