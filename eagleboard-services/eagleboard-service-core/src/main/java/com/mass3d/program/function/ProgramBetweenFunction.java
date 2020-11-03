package com.mass3d.program.function;

import static org.hisp.dhis.antlr.AntlrParserUtils.castDate;
import static org.hisp.dhis.antlr.AntlrParserUtils.castString;
import static com.mass3d.parser.expression.CommonExpressionVisitor.DEFAULT_DOUBLE_VALUE;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.ExprContext;

import com.mass3d.parser.expression.CommonExpressionVisitor;
import com.mass3d.program.ProgramExpressionItem;

/**
 * Program indicator date/time between functions
 *
 */
public abstract class ProgramBetweenFunction
    extends ProgramExpressionItem
{
    @Override
    public Object getDescription( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        castDate( visitor.visit( ctx.expr( 0 ) ) );
        castDate( visitor.visit( ctx.expr( 1 ) ) );

        return DEFAULT_DOUBLE_VALUE; // Always a number of months, days, etc.
    }

    @Override
    public final Object getSql( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        String startDate = castString( visitor.visitAllowingNulls( ctx.expr( 0 ) ) );
        String endDate = castString( visitor.visitAllowingNulls( ctx.expr( 1 ) ) );

        return getSqlBetweenDates( startDate, endDate );
    }

    /**
     * Generate SQL to compare dates, based on the time/date unit to compare.
     *
     * @param startDate starting date
     * @param endDate ending date
     * @return the SQL to compare the dates based on the time/date unit
     */
    public abstract Object getSqlBetweenDates( String startDate, String endDate );
}
