package com.mass3d.programrule;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.google.common.collect.Sets;
import java.util.Set;
import com.mass3d.common.DxfNamespaces;

@JacksonXmlRootElement( localName = "programRuleEvaluationEnvironment", namespace = DxfNamespaces.DXF_2_0 )
public enum ProgramRuleActionEvaluationEnvironment
{
    WEB( "web" ),
    ANDROID( "android" );

    private final String value;

    ProgramRuleActionEvaluationEnvironment( String value )
    {
        this.value = value;
    }

    public static ProgramRuleActionEvaluationEnvironment fromValue( String value )
    {
        for ( ProgramRuleActionEvaluationEnvironment type : ProgramRuleActionEvaluationEnvironment.values() )
        {
            if ( type.value.equalsIgnoreCase( value ) )
            {
                return type;
            }
        }

        return null;
    }

    /**
     * By default, actions should be run in all environments, and its up to the client
     * to decide which actions are unsuited to be run or not.
     *
     * @return Default environments where the actions should be run
     */
    public static Set<ProgramRuleActionEvaluationEnvironment> getDefault()
    {
        return Sets.newHashSet( ProgramRuleActionEvaluationEnvironment.values() );
    }
}
