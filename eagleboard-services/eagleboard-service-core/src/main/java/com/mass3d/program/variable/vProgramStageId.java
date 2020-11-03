package com.mass3d.program.variable;

import com.mass3d.parser.expression.CommonExpressionVisitor;
import com.mass3d.program.AnalyticsType;

/**
 * Program indicator variable: program stage id
 *
 */
public class vProgramStageId
    implements ProgramVariable
{
    @Override
    public final Object defaultVariableValue()
    {
        return "WZbXY0S00lP";
    }

    @Override
    public Object getSql( CommonExpressionVisitor visitor )
    {
        return AnalyticsType.EVENT == visitor.getProgramIndicator().getAnalyticsType() ? "ps" : "''";
    }
}
