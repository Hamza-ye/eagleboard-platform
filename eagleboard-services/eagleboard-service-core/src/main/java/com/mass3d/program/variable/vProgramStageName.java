package com.mass3d.program.variable;

import com.mass3d.parser.expression.CommonExpressionVisitor;
import com.mass3d.program.AnalyticsType;

/**
 * Program indicator variable: program stage name
 *
 */
public class vProgramStageName
    implements ProgramVariable
{
    @Override
    public final Object defaultVariableValue()
    {
        return "First antenatal care visit";
    }

    @Override
    public Object getSql( CommonExpressionVisitor visitor )
    {
        return AnalyticsType.EVENT == visitor.getProgramIndicator().getAnalyticsType() ?
            "(select name from programstage where uid = ps)" : "''";
    }
}
