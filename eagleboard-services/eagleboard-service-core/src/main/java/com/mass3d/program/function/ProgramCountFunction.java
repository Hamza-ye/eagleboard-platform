package com.mass3d.program.function;

import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.ExprContext;

import java.util.Date;
import org.hisp.dhis.antlr.ParserExceptionWithoutContext;
import com.mass3d.jdbc.StatementBuilder;
import com.mass3d.parser.expression.CommonExpressionVisitor;
import com.mass3d.program.ProgramExpressionItem;
import com.mass3d.program.ProgramIndicator;
import com.mass3d.program.dataitem.ProgramItemStageElement;

/**
 * Program indicator count functions
 *
 */
public abstract class ProgramCountFunction
    extends ProgramExpressionItem
{
    @Override
    public final Object getSql( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        validateCountFunctionArgs( ctx );

        ProgramIndicator pi = visitor.getProgramIndicator();
        StatementBuilder sb = visitor.getStatementBuilder();

        Date startDate = visitor.getReportingStartDate();
        Date endDate = visitor.getReportingEndDate();

        String programStage = ctx.uid0.getText();
        String dataElement = ctx.uid1.getText();

        String eventTableName = "analytics_event_" + pi.getProgram().getUid();
        String columnName = "\"" + dataElement + "\"";

        String conditionSql = getConditionSql( ctx, visitor );

        return "(select count(" + columnName + ") from " + eventTableName +
            " where " + eventTableName + ".pi = " + StatementBuilder.ANALYTICS_TBL_ALIAS + ".pi and " +
            columnName + " is not null and " + columnName + conditionSql + " " +
            (pi.getEndEventBoundary() != null ? ("and " +
                sb.getBoundaryCondition( pi.getEndEventBoundary(), pi,
                    startDate, endDate ) +
                " ") : "") + (pi.getStartEventBoundary() != null ? ("and " +
            sb.getBoundaryCondition( pi.getStartEventBoundary(), pi,
                startDate, endDate ) +
            " ") : "") + "and ps = '" + programStage + "')";
    }

    /**
     * Get the description for the first arg #{programStageUid.dataElementUid}
     * and return a value with its data type.
     *
     * @param ctx the expression context
     * @param visitor the tree visitor
     * @return a dummy value for the item (of the right type, for type checking)
     */
    protected Object getProgramStageElementDescription( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        validateCountFunctionArgs( ctx );

        return ( new ProgramItemStageElement() ).getDescription( ctx, visitor );
    }

    /**
     * Generate the conditional part of the SQL for a d2 count function
     *
     * @param ctx the expression context
     * @param visitor the program indicator expression tree visitor
     * @return the conditional part of the SQL
     */
    public abstract String getConditionSql( ExprContext ctx, CommonExpressionVisitor visitor );

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    private void validateCountFunctionArgs( ExprContext ctx )
    {
        if ( ! ( getProgramArgType( ctx ) instanceof ProgramItemStageElement ) )
        {
            throw new ParserExceptionWithoutContext( "First argument not supported for d2:count... functions: " + ctx.getText() );
        }
    }
}
