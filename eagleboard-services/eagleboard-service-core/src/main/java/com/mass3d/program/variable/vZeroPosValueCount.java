package com.mass3d.program.variable;

import com.mass3d.commons.util.TextUtils;
import com.mass3d.parser.expression.CommonExpressionVisitor;

/**
 * Program indicator variable: zero pos value count
 *
 */
public class vZeroPosValueCount
    extends ProgramDoubleVariable
{
    @Override
    public Object getSql( CommonExpressionVisitor visitor )
    {
        String sql = "nullif(cast((";

        for ( String uid : visitor.getDataElementAndAttributeIdentifiers() )
        {
            sql += "case when " + visitor.getStatementBuilder().columnQuote( uid ) + " >= 0 then 1 else 0 end + ";
        }

        return TextUtils.removeLast( sql, "+" ).trim() + ") as " + visitor.getStatementBuilder().getDoubleColumnType() + "),0)";
    }
}
