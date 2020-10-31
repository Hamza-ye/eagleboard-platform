package com.mass3d.programrule;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.HashSet;
import java.util.Set;
import com.mass3d.common.BaseIdentifiableObject;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.common.MetadataObject;
import com.mass3d.program.Program;
import com.mass3d.program.ProgramStage;

@JacksonXmlRootElement( localName = "programRule", namespace = DxfNamespaces.DXF_2_0 )
public class ProgramRule
    extends BaseIdentifiableObject
    implements MetadataObject
{
    /**
     * The description of the program rule
     */
    private String description;

    /**
     * The program that the rule belongs to
     */
    private Program program;

    /**
     * The programStage that the rule belongs to
     */
    private ProgramStage programStage;

    /**
     * The collection of actions that will be triggered if the the rule is triggered.
     */
    private Set<ProgramRuleAction> programRuleActions = new HashSet<>();

    /**
     * The condition expression, if this expression is evaluated to true, the actions is triggered.
     */
    private String condition;

    /**
     * The priority of the rule within the program. The lower the priority, the earlier the rule is run.
     * Null means that the rule is run last(together will all other null-rules)
     */
    private Integer priority;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public ProgramRule()
    {

    }

    public ProgramRule( String name, String description, Program program, ProgramStage programStage,
        Set<ProgramRuleAction> programRuleActions, String condition, Integer priority )
    {
        this.name = name;
        this.description = description;
        this.program = program;
        this.programStage = programStage;
        this.programRuleActions = programRuleActions;
        this.condition = condition;
        this.priority = priority;
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getDescription()
    {
        return description;
    }

    public void setDescription( String description )
    {
        this.description = description;
    }

    @JsonProperty
    @JsonSerialize( as = BaseIdentifiableObject.class )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public Program getProgram()
    {
        return program;
    }

    public void setProgram( Program program )
    {
        this.program = program;
    }

    @JsonProperty
    @JsonSerialize( as = BaseIdentifiableObject.class )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public ProgramStage getProgramStage()
    {
        return programStage;
    }

    public void setProgramStage( ProgramStage programStage )
    {
        this.programStage = programStage;
    }

    @JsonProperty
    @JsonSerialize( contentAs = BaseIdentifiableObject.class )
    @JacksonXmlElementWrapper( localName = "programRuleActions", namespace = DxfNamespaces.DXF_2_0 )
    @JacksonXmlProperty( localName = "programRuleAction", namespace = DxfNamespaces.DXF_2_0 )
    public Set<ProgramRuleAction> getProgramRuleActions()
    {
        return programRuleActions;
    }

    public void setProgramRuleActions( Set<ProgramRuleAction> programRuleActions )
    {
        this.programRuleActions = programRuleActions;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getCondition()
    {
        return condition;
    }

    public void setCondition( String condition )
    {
        this.condition = condition;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public Integer getPriority()
    {
        return priority;
    }

    public void setPriority( Integer priority )
    {
        this.priority = priority;
    }
}
