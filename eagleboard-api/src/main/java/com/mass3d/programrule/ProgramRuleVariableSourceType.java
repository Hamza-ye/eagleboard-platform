package com.mass3d.programrule;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.google.common.collect.ImmutableSet;
import java.util.Set;
import com.mass3d.common.DxfNamespaces;

@JacksonXmlRootElement( localName = "programRuleVariableSourceType", namespace = DxfNamespaces.DXF_2_0 )
public enum ProgramRuleVariableSourceType
{
    DATAELEMENT_NEWEST_EVENT_PROGRAM_STAGE( "dataelement_newest_event_program_stage" ),
    DATAELEMENT_NEWEST_EVENT_PROGRAM( "dataelement_newest_event_program" ),
    DATAELEMENT_CURRENT_EVENT( "dataelement_current_event" ),
    DATAELEMENT_PREVIOUS_EVENT( "dataelement_previous_event" ),
    CALCULATED_VALUE( "calculated_value" ),
    TEI_ATTRIBUTE( "tei_attribute" );

    private final String value;

    private static final Set<ProgramRuleVariableSourceType> DATA_TYPES = new ImmutableSet.Builder<ProgramRuleVariableSourceType>().add( DATAELEMENT_NEWEST_EVENT_PROGRAM_STAGE,
        DATAELEMENT_NEWEST_EVENT_PROGRAM, DATAELEMENT_CURRENT_EVENT, DATAELEMENT_PREVIOUS_EVENT ).build();

    private static final Set<ProgramRuleVariableSourceType> ATTRIBUTE_TYPES =
        new ImmutableSet.Builder<ProgramRuleVariableSourceType>().add( TEI_ATTRIBUTE ).build();

    ProgramRuleVariableSourceType( String value )
    {
        this.value = value;
    }

    public static ProgramRuleVariableSourceType fromValue( String value )
    {
        for ( ProgramRuleVariableSourceType type : ProgramRuleVariableSourceType.values() )
        {
            if ( type.value.equalsIgnoreCase( value ) )
            {
                return type;
            }
        }

        return null;
    }

    public static Set<ProgramRuleVariableSourceType> getDataTypes()
    {
        return DATA_TYPES;
    }

    public static Set<ProgramRuleVariableSourceType> getAttributeTypes()
    {
        return ATTRIBUTE_TYPES;
    }
}
