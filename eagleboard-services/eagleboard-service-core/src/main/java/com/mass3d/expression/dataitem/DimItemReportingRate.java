package com.mass3d.expression.dataitem;

import static com.mass3d.common.DimensionItemType.REPORTING_RATE;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.ExprContext;

import com.mass3d.common.DimensionalItemId;
import com.mass3d.parser.expression.CommonExpressionVisitor;

/**
 * Expression item ReportingRate
 *
 */
public class DimItemReportingRate
    extends DimensionalItem
{
    @Override
    public DimensionalItemId getDimensionalItemId( ExprContext ctx,
        CommonExpressionVisitor visitor )
    {
        return new DimensionalItemId( REPORTING_RATE,
            ctx.uid0.getText(),
            ctx.REPORTING_RATE_TYPE().getText() );
    }

    @Override
    public String getId( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        return ctx.uid0.getText() + "." +
            ctx.REPORTING_RATE_TYPE().getText() +
            (visitor.getPeriodOffset() == 0 ? "" : "." + visitor.getPeriodOffset());
    }
}
