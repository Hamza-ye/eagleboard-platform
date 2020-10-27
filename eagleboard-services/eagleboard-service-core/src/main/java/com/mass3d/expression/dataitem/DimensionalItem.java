package com.mass3d.expression.dataitem;

import static com.mass3d.parser.expression.ParserUtils.DOUBLE_VALUE_IF_NULL;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.ExprContext;

import org.hisp.dhis.antlr.ParserExceptionWithoutContext;
import com.mass3d.common.DimensionalItemId;
import com.mass3d.common.DimensionalItemObject;
import com.mass3d.parser.expression.CommonExpressionVisitor;
import com.mass3d.parser.expression.ExpressionItem;

/**
 * Parsed dimensional item as handled by the expression service.
 *
 */
public abstract class DimensionalItem
    implements ExpressionItem
{
    @Override
    public final Object getDescription( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        DimensionalItemId itemId = getDimensionalItemId( ctx, visitor );

        DimensionalItemObject item = visitor.getDimensionService().getDataDimensionalItemObject( itemId );

        if ( item == null )
        {
            throw new ParserExceptionWithoutContext(
                "Can't find " + itemId.getDimensionItemType().name() + " for '" + itemId + "'" );
        }

        visitor.getItemDescriptions().put( ctx.getText(), item.getDisplayName() );

        return DOUBLE_VALUE_IF_NULL;
    }

    @Override
    public final Object getItemId( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        visitor.getItemIds().add( getDimensionalItemId( ctx, visitor ) );

        return DOUBLE_VALUE_IF_NULL;
    }

    @Override
    public final Object getOrgUnitGroup( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        return DOUBLE_VALUE_IF_NULL;
    }

    @Override
    public final Object evaluate( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        Double value = visitor.getItemValueMap().get( getId( ctx, visitor ) );

        return visitor.handleNulls( value );
    }

    /**
     * Constructs the DimensionalItemId object for this item.
     *
     * @param ctx the parser item context
     * @param visitor
     * @return the DimensionalItemId object for this item
     */
    public abstract DimensionalItemId getDimensionalItemId( ExprContext ctx,
        CommonExpressionVisitor visitor );

    /**
     * Returns the id for this item.
     * <p/>
     * For example, uid, or uid0.uid1, etc.
     *
     * @param ctx the parser item context
     * @return the id for this item
     */
    public abstract String getId( ExprContext ctx, CommonExpressionVisitor visitor );
}
