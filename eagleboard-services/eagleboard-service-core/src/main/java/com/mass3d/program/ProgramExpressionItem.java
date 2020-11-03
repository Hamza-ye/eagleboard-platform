package com.mass3d.program;

import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.ExprContext;

import org.hisp.dhis.antlr.ParserExceptionWithoutContext;
import com.mass3d.common.ValueType;
import com.mass3d.parser.expression.CommonExpressionVisitor;
import com.mass3d.parser.expression.ExpressionItem;
import com.mass3d.program.dataitem.ProgramItemAttribute;
import com.mass3d.program.dataitem.ProgramItemPsEventdate;
import com.mass3d.program.dataitem.ProgramItemStageElement;
import com.mass3d.program.variable.ProgramVariableItem;

/**
 * Program indicator expression item
 * <p/>
 * The only two methods that are used by program indicator-only items are
 * {@link ExpressionItem#getDescription} and {@link ExpressionItem#getSql}.
 * <p/>
 * getDescription checks the expression item syntax, and returns the
 * expected return data type. For data items, it also registers the
 * translation of any UIDs into human-readable object names.
 *
 */
public abstract class ProgramExpressionItem
    implements ExpressionItem
{
    @Override
    public final Object getItemId( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        throw new ParserExceptionWithoutContext( "Internal parsing error: getItemId called for program indicator item " + ctx.getText() );
    }

    @Override
    public final Object getOrgUnitGroup( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        throw new ParserExceptionWithoutContext( "Internal parsing error: getOrgUnitGroup called for program indicator item " + ctx.getText() );
    }

    @Override
    public final Object evaluate( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        throw new ParserExceptionWithoutContext( "Internal parsing error: evaluate called for program indicator item " + ctx.getText() );
    }

    /**
     * Get the program expression item that matches the parsed arguments
     *
     * @param ctx the expression context
     * @return the program expression item that can handle the parsed arguments
     */
    protected ProgramExpressionItem getProgramArgType( ExprContext ctx )
    {
        if ( ctx.psEventDate != null )
        {
            return new ProgramItemPsEventdate();
        }

        if ( ctx.uid1 != null )
        {
            return new ProgramItemStageElement();
        }

        if ( ctx.uid0 != null )
        {
            return new ProgramItemAttribute();
        }

        if ( ctx.programVariable() != null )
        {
            return new ProgramVariableItem();
        }

        throw new ParserExceptionWithoutContext( "Illegal argument in program indicator expression: " + ctx.getText() );
    }

    /**
     * Replace null SQL query values with 0 or '', depending on the value type.
     *
     * @param column the column (may be a subquery)
     * @param valueType the type of value that might be null
     * @return SQL to replace a null value with 0 or '' depending on type
     */
    protected String replaceNullSqlValues( String column, ValueType valueType )
    {
        return valueType.isNumeric() || valueType.isBoolean()
            ? "coalesce(" + column + "::numeric,0)"
            : "coalesce(" + column + ",'')";
    }
}
