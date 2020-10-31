package com.mass3d.program.notification;

import java.util.EnumSet;
import java.util.Map;
import java.util.stream.Collectors;
import com.mass3d.notification.TemplateVariable;

/**
 * Defines the variable expression names for a {@link ProgramNotificationTemplate}
 * on a {@link com.mass3d.program.ProgramStage ProgramStage}.
 *
 * The supported variable names are:
 *
 * <ul>
 *     <li>program_name</li>
 *     <li>program_stage_name</li>
 *     <li>org_unit_name</li>
 *     <li>due_date</li>
 *     <li>days_since_due_date</li>
 *     <li>days_until_due_date</li>
 *     <li>current_date</li>
 * </ul>
 *
 */
public enum ProgramStageTemplateVariable
    implements TemplateVariable
{
    PROGRAM_NAME( "program_name" ),
    PROGRAM_STAGE_NAME( "program_stage_name" ),
    ORG_UNIT_NAME( "org_unit_name" ),
    DUE_DATE( "due_date" ),
    DAYS_SINCE_DUE_DATE( "days_since_due_date" ),
    DAYS_UNTIL_DUE_DATE( "days_until_due_date" ),
    CURRENT_DATE( "current_date" );

    private static final Map<String, ProgramStageTemplateVariable> variableNameMap =
        EnumSet.allOf( ProgramStageTemplateVariable.class ).stream()
            .collect( Collectors.toMap( ProgramStageTemplateVariable::getVariableName, e -> e ) );

    private final String variableName;

    ProgramStageTemplateVariable( String variableName )
    {
        this.variableName = variableName;
    }

    @Override
    public String getVariableName()
    {
        return variableName;
    }

    public static boolean isValidVariableName( String expressionName )
    {
        return variableNameMap.keySet().contains( expressionName );
    }

    public static ProgramStageTemplateVariable fromVariableName( String variableName )
    {
        return variableNameMap.get( variableName );
    }
}
