package com.mass3d.parser.expression.dataitem;

import static com.mass3d.parser.expression.ParserUtils.DOUBLE_VALUE_IF_NULL;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.ExprContext;

import org.hisp.dhis.antlr.ParserExceptionWithoutContext;
import com.mass3d.constant.Constant;
import com.mass3d.parser.expression.CommonExpressionVisitor;
import com.mass3d.parser.expression.ExpressionItem;

/**
 * Expression item Constant
 *
 */
public class ItemConstant
    implements ExpressionItem
{
    @Override
    public Object getDescription( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        Constant constant = visitor.getConstantMap().get( ctx.uid0.getText() );

        if ( constant == null )
        {
            throw new ParserExceptionWithoutContext( "No constant defined for " + ctx.uid0.getText() );
        }

        visitor.getItemDescriptions().put( ctx.getText(), constant.getDisplayName() );

        return DOUBLE_VALUE_IF_NULL;
    }

    @Override
    public Object evaluate( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        Constant constant = visitor.getConstantMap().get( ctx.uid0.getText() );

        if ( constant == null ) // Shouldn't happen for a valid expression.
        {
            throw new ParserExceptionWithoutContext( "Can't find constant to evaluate " + ctx.uid0.getText() );
        }

        return constant.getValue();
    }

    @Override
    public Object getSql( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        Constant constant = visitor.getConstantMap().get( ctx.uid0.getText() );

        if ( constant == null )
        {
            throw new ParserExceptionWithoutContext( "Can't find constant for SQL " + ctx.uid0.getText() );
        }

        return Double.valueOf( constant.getValue() ).toString();
    }

    @Override
    public Object regenerate( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        Constant constant = visitor.getConstantMap().get( ctx.uid0.getText() );

        if ( constant == null )
        {
            return ctx.getText();
        }

        return Double.valueOf( constant.getValue() ).toString();
    }
}
