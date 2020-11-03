package com.mass3d.program.dataitem;

import static com.mass3d.parser.expression.CommonExpressionVisitor.DEFAULT_DATE_VALUE;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.ExprContext;

import org.hisp.dhis.antlr.ParserExceptionWithoutContext;
import com.mass3d.parser.expression.CommonExpressionVisitor;
import com.mass3d.program.ProgramExpressionItem;
import com.mass3d.program.ProgramStage;

/**
 * Program indicator expression data item PS_EVENTDATE: programStageUid
 *
 */
public class ProgramItemPsEventdate
    extends ProgramExpressionItem
{
    @Override
    public Object getDescription( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        String programStageUid = ctx.uid0.getText();

        ProgramStage programStage = visitor.getProgramStageService().getProgramStage( programStageUid );

        if ( programStage == null )
        {
            throw new ParserExceptionWithoutContext( "Program stage " + ctx.uid0.getText() + " not found" );
        }

        visitor.getItemDescriptions().put( programStageUid, programStage.getDisplayName() );

        return DEFAULT_DATE_VALUE;
    }

    @Override
    public final Object getSql( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        return visitor.getStatementBuilder().getProgramIndicatorEventColumnSql(
            ctx.uid0.getText(), "executiondate",
            visitor.getReportingStartDate(), visitor.getReportingEndDate(), visitor.getProgramIndicator() );
    }
}
