package com.mass3d.notification;

import static com.mass3d.validation.notification.ValidationRuleTemplateVariable.CURRENT_DATE;
import static com.mass3d.validation.notification.ValidationRuleTemplateVariable.DESCRIPTION;
import static com.mass3d.validation.notification.ValidationRuleTemplateVariable.IMPORTANCE;
import static com.mass3d.validation.notification.ValidationRuleTemplateVariable.LEFT_SIDE_DESCRIPTION;
import static com.mass3d.validation.notification.ValidationRuleTemplateVariable.LEFT_SIDE_VALUE;
import static com.mass3d.validation.notification.ValidationRuleTemplateVariable.OPERATOR;
import static com.mass3d.validation.notification.ValidationRuleTemplateVariable.ORG_UNIT_NAME;
import static com.mass3d.validation.notification.ValidationRuleTemplateVariable.PERIOD;
import static com.mass3d.validation.notification.ValidationRuleTemplateVariable.RIGHT_SIDE_DESCRIPTION;
import static com.mass3d.validation.notification.ValidationRuleTemplateVariable.RIGHT_SIDE_VALUE;
import static com.mass3d.validation.notification.ValidationRuleTemplateVariable.RULE_NAME;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import com.mass3d.validation.ValidationResult;
import com.mass3d.validation.notification.ValidationRuleTemplateVariable;
import org.springframework.stereotype.Service;

@Service( "validationNotificationMessageRenderer" )
public class ValidationNotificationMessageRenderer
    extends BaseNotificationMessageRenderer<ValidationResult>
{
    private static final ImmutableMap<TemplateVariable, Function<ValidationResult, String>> VARIABLE_RESOLVERS =
        new ImmutableMap.Builder<TemplateVariable, Function<ValidationResult, String>>()
            .put( RULE_NAME, vr -> vr.getValidationRule().getDisplayName() )
            .put( DESCRIPTION, vr -> vr.getValidationRule().getDescription() )
            .put( OPERATOR, vr -> vr.getValidationRule().getOperator().getMathematicalOperator() )
            .put( IMPORTANCE, vr -> vr.getValidationRule().getImportance().name() )
            .put( LEFT_SIDE_DESCRIPTION, vr -> vr.getValidationRule().getLeftSide().getDescription() )
            .put( RIGHT_SIDE_DESCRIPTION, vr -> vr.getValidationRule().getRightSide().getDescription() )
            .put( LEFT_SIDE_VALUE, vr -> Double.toString( vr.getLeftsideValue() ) )
            .put( RIGHT_SIDE_VALUE, vr -> Double.toString( vr.getRightsideValue() ) )
            .put( ORG_UNIT_NAME, vr -> vr.getOrganisationUnit().getDisplayName() )
            .put( PERIOD, vr -> vr.getPeriod().getDisplayName() )
            .put( CURRENT_DATE, vr -> formatDate( new Date() ) )
            .build();

    private static final ImmutableSet<ExpressionType> SUPPORTED_EXPRESSION_TYPES = ImmutableSet.of( ExpressionType.VARIABLE );

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public ValidationNotificationMessageRenderer()
    {
    }

    // -------------------------------------------------------------------------
    // Overrides
    // -------------------------------------------------------------------------

    @Override
    protected ImmutableMap<TemplateVariable, Function<ValidationResult, String>> getVariableResolvers()
    {
        return VARIABLE_RESOLVERS;
    }

    @Override
    protected Map<String, String> resolveTrackedEntityAttributeValues( Set<String> attributeKeys, ValidationResult result )
    {
        // Attributes are not supported for validation notifications
        return Collections.emptyMap();
    }

    @Override
    protected TemplateVariable fromVariableName( String name )
    {
        return ValidationRuleTemplateVariable.fromVariableName( name );
    }

    @Override
    protected Set<ExpressionType> getSupportedExpressionTypes()
    {
        return SUPPORTED_EXPRESSION_TYPES;
    }

    @Override
    protected Map<String, String> resolveDataElementValues( Set<String> elementKeys, ValidationResult entity )
    {
        // DataElements are not supported for validation notifications
        return Collections.emptyMap();
    }
}
