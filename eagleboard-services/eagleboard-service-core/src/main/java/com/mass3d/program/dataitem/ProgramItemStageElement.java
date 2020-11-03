package com.mass3d.program.dataitem;

import static com.mass3d.parser.expression.ParserUtils.assumeStageElementSyntax;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.ExprContext;

import org.hisp.dhis.antlr.ParserExceptionWithoutContext;
import com.mass3d.dataelement.DataElement;
import com.mass3d.parser.expression.CommonExpressionVisitor;
import com.mass3d.program.ProgramExpressionItem;
import com.mass3d.program.ProgramIndicator;
import com.mass3d.program.ProgramStage;
import com.mass3d.system.util.ValidationUtils;

/**
 * Program indicator expression data item ProgramItemStageElement
 *
 */
public class ProgramItemStageElement
    extends ProgramExpressionItem
{
    @Override
    public Object getDescription( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        assumeStageElementSyntax( ctx );

        String programStageId = ctx.uid0.getText();
        String dataElementId = ctx.uid1.getText();

        ProgramStage programStage = visitor.getProgramStageService().getProgramStage( programStageId );
        DataElement dataElement = visitor.getDataElementService().getDataElement( dataElementId );

        if ( programStage == null )
        {
            throw new ParserExceptionWithoutContext( "Program stage " + programStageId + " not found" );
        }

        if ( dataElement == null )
        {
            throw new ParserExceptionWithoutContext( "Data element " + dataElementId + " not found" );
        }

        String description = programStage.getDisplayName() + ProgramIndicator.SEPARATOR_ID + dataElement.getDisplayName();

        visitor.getItemDescriptions().put( ctx.getText(), description );

        return ValidationUtils.getSubstitutionValue( dataElement.getValueType() );
    }

    @Override
    public Object getSql( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        assumeStageElementSyntax( ctx );

        String programStageId = ctx.uid0.getText();
        String dataElementId = ctx.uid1.getText();

        String column = visitor.getStatementBuilder().getProgramIndicatorDataValueSelectSql(
            programStageId, dataElementId, visitor.getReportingStartDate(), visitor.getReportingEndDate(), visitor.getProgramIndicator() );

        if ( visitor.getReplaceNulls() )
        {
            DataElement dataElement = visitor.getDataElementService().getDataElement( dataElementId );

            if ( dataElement == null )
            {
                throw new ParserExceptionWithoutContext( "Data element " + dataElementId + " not found during SQL generation." );
            }

            column = replaceNullSqlValues( column, dataElement.getValueType() );
        }

        return column;
    }
}
