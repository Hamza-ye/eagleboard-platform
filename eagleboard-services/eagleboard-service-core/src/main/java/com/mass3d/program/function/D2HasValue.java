package com.mass3d.program.function;

import static com.mass3d.parser.expression.CommonExpressionVisitor.DEFAULT_BOOLEAN_VALUE;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.ExprContext;

import com.mass3d.parser.expression.CommonExpressionVisitor;
import com.mass3d.program.ProgramExpressionItem;

/**
 * Program indicator function: d2 has value
 *
 */
public class D2HasValue
    extends ProgramExpressionItem
{
    @Override
    public Object getDescription( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        getProgramArgType( ctx ).getDescription( ctx, visitor );

        return DEFAULT_BOOLEAN_VALUE;
    }

    @Override
    public Object getSql( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        boolean savedReplaceNulls = visitor.getReplaceNulls();

        visitor.setReplaceNulls( false );

        String argSql = (String) getProgramArgType( ctx ).getSql( ctx, visitor );

        visitor.setReplaceNulls( savedReplaceNulls );

        return "(" + argSql + " is not null)";
    }
}
