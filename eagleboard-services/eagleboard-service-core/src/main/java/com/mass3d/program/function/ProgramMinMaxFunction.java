package com.mass3d.program.function;

import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.ExprContext;

import java.util.Date;
import com.mass3d.jdbc.StatementBuilder;
import com.mass3d.parser.expression.CommonExpressionVisitor;
import com.mass3d.program.AnalyticsType;
import com.mass3d.program.ProgramExpressionItem;
import com.mass3d.program.ProgramIndicator;

public abstract class ProgramMinMaxFunction
    extends ProgramExpressionItem
{
    @Override
    public Object getDescription( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        return getProgramArgType( ctx ).getDescription( ctx, visitor );
    }

    @Override
    public Object getSql( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        ProgramIndicator pi = visitor.getProgramIndicator();
        StatementBuilder sb = visitor.getStatementBuilder();

        String columnName = "";

        if ( ctx.uid1 == null ) // arg: PS_EVENTDATE:programStageUid
        {
            columnName = "\"executiondate\"";
        }
        else //  arg: #{programStageUid.dataElementUid}
        {
            String dataElement = ctx.uid1.getText();
            columnName = "\"" + dataElement + "\"";
        }

        if ( AnalyticsType.EVENT == pi.getAnalyticsType() )
        {
            return columnName;
        }

        Date startDate = visitor.getReportingStartDate();
        Date endDate = visitor.getReportingEndDate();

        String eventTableName = "analytics_event_" + pi.getProgram().getUid();
        String programStage = ctx.uid0.getText();

        return  "(select " + getAggregationOperator() + "(" + columnName + ") from " + eventTableName +
            " where " + eventTableName + ".pi = " + StatementBuilder.ANALYTICS_TBL_ALIAS + ".pi " +
            ( pi.getEndEventBoundary() != null ? ( "and " + sb.getBoundaryCondition( pi.getEndEventBoundary(), pi, startDate, endDate ) + " " ) : "" ) +
            ( pi.getStartEventBoundary() != null ? ( "and " + sb.getBoundaryCondition( pi.getStartEventBoundary(), pi, startDate, endDate ) + " " ) : "" ) + "and ps = '" + programStage + "')";
    }

    /***
     * Generate the function part of the SQL
     * @return string sql min/max functions
     */
    public abstract String getAggregationOperator();
}
