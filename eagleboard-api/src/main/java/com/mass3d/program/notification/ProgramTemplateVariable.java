package com.mass3d.program.notification;

import java.util.EnumSet;
import java.util.Map;
import java.util.stream.Collectors;
import com.mass3d.notification.TemplateVariable;

/**
 * Defines the variables for a {@link ProgramNotificationTemplate}.
 * on a {@link com.mass3d.program.Program Program}.
 *
 * The supported variable names are:
 *
 * <ul>
 *     <li>program_name</li>
 *     <li>org_unit_name</li>
 *     <li>current_date</li>
 *     <li>enrollment_date</li>
 *     <li>days_since_enrollment_date</li>
 *     <li>incident_date</li>
 * </ul>
 *
 */
public enum ProgramTemplateVariable
    implements TemplateVariable
{
    PROGRAM_NAME( "program_name" ),
    ORG_UNIT_NAME( "org_unit_name" ),
    CURRENT_DATE( "current_date" ),
    ENROLLMENT_DATE( "enrollment_date" ),
    DAYS_SINCE_ENROLLMENT_DATE( "days_since_enrollment_date" ),
    INCIDENT_DATE( "incident_date" );

    private static final Map<String, ProgramTemplateVariable> variableNameMap =
        EnumSet.allOf( ProgramTemplateVariable.class ).stream()
            .collect( Collectors.toMap( ProgramTemplateVariable::getVariableName, e -> e ) );

    private final String variableName;

    ProgramTemplateVariable( String variableName )
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

    public static ProgramTemplateVariable fromVariableName( String variableName )
    {
        return variableNameMap.get( variableName );
    }
}
