package com.mass3d.validation.notification;

import java.util.EnumSet;
import java.util.Map;
import java.util.stream.Collectors;
import com.mass3d.notification.TemplateVariable;

public enum ValidationRuleTemplateVariable
    implements TemplateVariable
{
    RULE_NAME( "rule_name" ),
    DESCRIPTION( "rule_description" ),
    OPERATOR( "operator" ),
    IMPORTANCE( "importance" ),
    LEFT_SIDE_DESCRIPTION( "left_side_description" ),
    RIGHT_SIDE_DESCRIPTION( "right_side_description" ),
    LEFT_SIDE_VALUE( "left_side_value" ),
    RIGHT_SIDE_VALUE( "right_side_value" ),
    ORG_UNIT_NAME( "org_unit_name" ),
    PERIOD( "period" ),
    CURRENT_DATE( "current_date" );

    private static final Map<String, ValidationRuleTemplateVariable> variableNameMap =
        EnumSet.allOf( ValidationRuleTemplateVariable.class ).stream()
            .collect( Collectors.toMap( ValidationRuleTemplateVariable::getVariableName, e -> e ) );

    private final String variableName;

    ValidationRuleTemplateVariable( String variableName )
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

    public static ValidationRuleTemplateVariable fromVariableName( String variableName )
    {
        return variableNameMap.get( variableName );
    }
}
