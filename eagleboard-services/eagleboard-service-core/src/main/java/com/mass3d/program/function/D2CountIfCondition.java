package com.mass3d.program.function;

import static org.hisp.dhis.antlr.AntlrParserUtils.castDouble;
import static org.hisp.dhis.antlr.AntlrParserUtils.trimQuotes;
import static com.mass3d.parser.expression.CommonExpressionVisitor.DEFAULT_DOUBLE_VALUE;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.ExprContext;

import com.mass3d.parser.expression.CommonExpressionVisitor;

/**
 * Program indicator function: d2 count if condition
 *
 */
public class D2CountIfCondition
    extends ProgramCountFunction
{
    @Override
    public Object getDescription( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        castDouble( getProgramStageElementDescription( ctx, visitor ) );

        String conditionExpression = getConditionalExpression( ctx );

        visitor.getProgramIndicatorService().validate( conditionExpression,
            Boolean.class, visitor.getItemDescriptions() );

        return DEFAULT_DOUBLE_VALUE;
    }

    @Override
    public String getConditionSql( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        String conditionExpression = getConditionalExpression( ctx );

        String conditionSql = visitor.getProgramIndicatorService().getAnalyticsSql(
            conditionExpression, visitor.getProgramIndicator(),
            visitor.getReportingStartDate(), visitor.getReportingEndDate() );

        return conditionSql.substring( 1 );
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    /**
     * Gets a complete expression that is used to test a condition (e.g. "<5")
     * by putting a "0" in front to get a complete expression (e.g., "0<5").
     *
     * @param ctx the expression context
     * @return the complete expression
     */
    private String getConditionalExpression( ExprContext ctx )
    {
        return "0" + trimQuotes( ctx.stringLiteral().getText() );
    }
}
