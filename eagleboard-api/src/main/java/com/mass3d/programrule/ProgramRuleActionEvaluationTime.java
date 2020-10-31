package com.mass3d.programrule;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.google.common.collect.Sets;
import java.util.Set;
import com.mass3d.common.DxfNamespaces;

@JacksonXmlRootElement( localName = "programRuleEvaluationTime", namespace = DxfNamespaces.DXF_2_0 )
public enum ProgramRuleActionEvaluationTime
{
    ON_DATA_ENTRY( "on_data_entry" ),
    ON_COMPLETE( "on_complete" ),
    ALWAYS( "always" );

    private final String value;

    ProgramRuleActionEvaluationTime( String value )
    {
        this.value = value;
    }

    public static ProgramRuleActionEvaluationTime fromValue( String value )
    {
        for ( ProgramRuleActionEvaluationTime type : ProgramRuleActionEvaluationTime.values() )
        {
            if ( type.value.equalsIgnoreCase( value ) )
            {
                return type;
            }
        }

        return null;
    }

    public static ProgramRuleActionEvaluationTime getDefault()
    {
        return ALWAYS;
    }

    public static Set<ProgramRuleActionEvaluationTime> getAll()
    {
        return Sets.newHashSet( ProgramRuleActionEvaluationTime.values() );
    }
}
