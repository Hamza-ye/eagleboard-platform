package com.mass3d.notification;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import com.mass3d.program.ProgramInstance;
import com.mass3d.program.notification.ProgramTemplateVariable;
import com.mass3d.trackedentityattributevalue.TrackedEntityAttributeValue;
import org.springframework.stereotype.Component;

@Component
public class ProgramNotificationMessageRenderer
    extends BaseNotificationMessageRenderer<ProgramInstance>
{
    private static final ImmutableMap<TemplateVariable, Function<ProgramInstance, String>> VARIABLE_RESOLVERS =
        new ImmutableMap.Builder<TemplateVariable, Function<ProgramInstance, String>>()
            .put( ProgramTemplateVariable.PROGRAM_NAME,                 pi -> pi.getProgram().getDisplayName() )
            .put( ProgramTemplateVariable.ORG_UNIT_NAME,                pi -> pi.getOrganisationUnit().getDisplayName() )
            .put( ProgramTemplateVariable.CURRENT_DATE,                 pi -> formatDate( new Date() ) )
            .put( ProgramTemplateVariable.ENROLLMENT_DATE,              pi -> formatDate( pi.getEnrollmentDate() ) )
            .put( ProgramTemplateVariable.INCIDENT_DATE,                pi -> formatDate( pi.getIncidentDate() ) )
            .put( ProgramTemplateVariable.DAYS_SINCE_ENROLLMENT_DATE,   pi -> daysSince( pi.getEnrollmentDate() ) )
            .build();

    private static final Set<ExpressionType> SUPPORTED_EXPRESSION_TYPES =
        ImmutableSet.of( ExpressionType.TRACKED_ENTITY_ATTRIBUTE, ExpressionType.VARIABLE );

    // -------------------------------------------------------------------------
    // Overrides
    // -------------------------------------------------------------------------

    @Override
    protected ImmutableMap<TemplateVariable, Function<ProgramInstance, String>> getVariableResolvers()
    {
        return VARIABLE_RESOLVERS;
    }

    @Override
    protected Map<String, String> resolveTrackedEntityAttributeValues( Set<String> attributeKeys, ProgramInstance entity )
    {
        if ( attributeKeys.isEmpty() )
        {
            return Maps.newHashMap();
        }

        return entity.getEntityInstance().getTrackedEntityAttributeValues().stream()
            .filter( av -> attributeKeys.contains( av.getAttribute().getUid() ) )
            .collect( Collectors
                .toMap( av -> av.getAttribute().getUid(), ProgramNotificationMessageRenderer::filterValue ) );
    }

    @Override
    protected TemplateVariable fromVariableName( String name )
    {
        return ProgramTemplateVariable.fromVariableName( name );
    }

    @Override
    protected Set<ExpressionType> getSupportedExpressionTypes()
    {
        return SUPPORTED_EXPRESSION_TYPES;
    }

    @Override
    protected Map<String, String> resolveDataElementValues( Set<String> elementKeys, ProgramInstance entity )
    {
        // DataElements are not supported for program notifications
        return Collections.emptyMap();
    }

    // -------------------------------------------------------------------------
    // Internal methods
    // -------------------------------------------------------------------------

    private static String filterValue( TrackedEntityAttributeValue av )
    {
        String value = av.getPlainValue();

        if ( value == null )
        {
            return CONFIDENTIAL_VALUE_REPLACEMENT;
        }

        // If the AV has an OptionSet -> substitute value with the name of the Option
        if ( av.getAttribute().hasOptionSet() )
        {
            value = av.getAttribute().getOptionSet().getOptionByCode( value ).getName();
        }

        return value != null ? value : MISSING_VALUE_REPLACEMENT;
    }
}
