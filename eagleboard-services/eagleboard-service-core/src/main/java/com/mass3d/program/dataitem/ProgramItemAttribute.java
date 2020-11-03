package com.mass3d.program.dataitem;

import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.ExprContext;

import org.hisp.dhis.antlr.ParserExceptionWithoutContext;
import com.mass3d.parser.expression.CommonExpressionVisitor;
import com.mass3d.program.ProgramExpressionItem;
import com.mass3d.system.util.ValidationUtils;
import com.mass3d.trackedentity.TrackedEntityAttribute;

/**
 * Program indicator expression data item ProgramAttribute
 *
 */
public class ProgramItemAttribute
    extends ProgramExpressionItem
{
    @Override
    public Object getDescription( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        String attributeId = getProgramAttributeId( ctx );

        TrackedEntityAttribute attribute = visitor.getAttributeService().getTrackedEntityAttribute( attributeId );

        if ( attribute == null )
        {
            throw new ParserExceptionWithoutContext( "Tracked entity attribute " + attributeId + " not found." );
        }

        visitor.getItemDescriptions().put( ctx.getText(), attribute.getDisplayName() );

        return ValidationUtils.getSubstitutionValue( attribute.getValueType() );
    }

    @Override
    public Object getSql( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        String attributeId = getProgramAttributeId( ctx );

        String column = visitor.getStatementBuilder().columnQuote( attributeId );

        if ( visitor.getReplaceNulls() )
        {
            TrackedEntityAttribute attribute = visitor.getAttributeService().getTrackedEntityAttribute( attributeId );

            if ( attribute == null )
            {
                throw new ParserExceptionWithoutContext( "Tracked entity attribute " + attributeId + " not found during SQL generation." );
            }

            column = replaceNullSqlValues( column, attribute.getValueType() );
        }

        return column;
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    /**
     * Makes sure that the parsed A{...} has a syntax that could be used
     * be used in an program expression for A{attributeUid}
     *
     * @param ctx the item context
     * @return the attribute UID.
     */
    private String getProgramAttributeId( ExprContext ctx )
    {
        if ( ctx.uid1 != null )
        {
            throw new ParserExceptionWithoutContext(
                "Program attribute must have one UID: " + ctx.getText() );
        }

        return ctx.uid0.getText();
    }
}
