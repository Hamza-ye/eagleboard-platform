package com.mass3d.notification;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import com.mass3d.dataelement.DataElement;
import com.mass3d.eventdatavalue.EventDataValue;
import com.mass3d.program.ProgramStageInstance;
import com.mass3d.program.notification.ProgramStageTemplateVariable;
import com.mass3d.trackedentityattributevalue.TrackedEntityAttributeValue;
import org.springframework.stereotype.Component;

@Component
public class ProgramStageNotificationMessageRenderer
    extends BaseNotificationMessageRenderer<ProgramStageInstance>
{
    private static final ImmutableMap<TemplateVariable, Function<ProgramStageInstance, String>> VARIABLE_RESOLVERS =
        new ImmutableMap.Builder<TemplateVariable, Function<ProgramStageInstance, String>>()
            .put( ProgramStageTemplateVariable.PROGRAM_NAME,         psi -> psi.getProgramStage().getProgram().getDisplayName() )
            .put( ProgramStageTemplateVariable.PROGRAM_STAGE_NAME,   psi -> psi.getProgramStage().getDisplayName() )
            .put( ProgramStageTemplateVariable.ORG_UNIT_NAME,        psi -> psi.getOrganisationUnit().getDisplayName() )
            .put( ProgramStageTemplateVariable.DUE_DATE,             psi -> formatDate( psi.getDueDate() ) )
            .put( ProgramStageTemplateVariable.DAYS_SINCE_DUE_DATE,  psi -> daysSince( psi.getDueDate() ) )
            .put( ProgramStageTemplateVariable.DAYS_UNTIL_DUE_DATE,  psi -> daysUntil( psi.getDueDate() ) )
            .put( ProgramStageTemplateVariable.CURRENT_DATE,         psi -> formatDate( new Date() ) )
            .build();

    private static final Set<ExpressionType> SUPPORTED_EXPRESSION_TYPES =
        ImmutableSet
            .of( ExpressionType.TRACKED_ENTITY_ATTRIBUTE, ExpressionType.VARIABLE, ExpressionType.DATA_ELEMENT );

    // -------------------------------------------------------------------------
    // Singleton instance
    // -------------------------------------------------------------------------

    public static final ProgramStageNotificationMessageRenderer INSTANCE = new ProgramStageNotificationMessageRenderer();

    // -------------------------------------------------------------------------
    // Overrides
    // -------------------------------------------------------------------------

    @Override
    protected ImmutableMap<TemplateVariable, Function<ProgramStageInstance, String>> getVariableResolvers()
    {
        return VARIABLE_RESOLVERS;
    }

    @Override
    protected Map<String, String> resolveTrackedEntityAttributeValues( Set<String> attributeKeys, ProgramStageInstance entity )
    {
        if ( attributeKeys.isEmpty() )
        {
            return Maps.newHashMap();
        }

        return entity.getProgramInstance().getEntityInstance().getTrackedEntityAttributeValues().stream()
            .filter( av -> attributeKeys.contains( av.getAttribute().getUid() ) )
            .collect( Collectors
                .toMap( av -> av.getAttribute().getUid(), ProgramStageNotificationMessageRenderer::filterValue ) );
    }

    @Override
    protected Map<String, String> resolveDataElementValues( Set<String> elementKeys, ProgramStageInstance entity )
    {
        if ( elementKeys.isEmpty() )
        {
            return Maps.newHashMap();
        }

        Map<String, DataElement> dataElementsMap = new HashMap<>();
        entity.getProgramStage().getDataElements().forEach( de -> dataElementsMap.put( de.getUid(), de ) );

        return entity.getEventDataValues().stream()
            .filter( dv -> elementKeys.contains( dv.getDataElement() ) )
            .collect( Collectors
                .toMap( EventDataValue::getDataElement, dv -> filterValue( dv, dataElementsMap.get( dv.getDataElement() ) ) ));
    }

    @Override
    protected TemplateVariable fromVariableName( String name )
    {
        return ProgramStageTemplateVariable.fromVariableName( name );
    }

    @Override
    protected Set<ExpressionType> getSupportedExpressionTypes()
    {
        return SUPPORTED_EXPRESSION_TYPES;
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

    private static String filterValue( EventDataValue dv, DataElement dataElement )
    {
        String value = dv.getValue();

        if ( value == null )
        {
            return CONFIDENTIAL_VALUE_REPLACEMENT;
        }

        // If the DV has an OptionSet -> substitute value with the name of the Option
        if ( dataElement != null && dataElement.hasOptionSet() )
        {
            value = dataElement.getOptionSet().getOptionByCode( value ).getName();
        }

        return value != null ? value : MISSING_VALUE_REPLACEMENT;
    }
}
