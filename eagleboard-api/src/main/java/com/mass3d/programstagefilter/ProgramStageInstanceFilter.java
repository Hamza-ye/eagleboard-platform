package com.mass3d.programstagefilter;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.mass3d.common.BaseIdentifiableObject;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.common.MetadataObject;
import com.mass3d.schema.PropertyType;
import com.mass3d.schema.annotation.Property;
import com.mass3d.schema.annotation.PropertyRange;

@JacksonXmlRootElement( localName = "programStageInstanceFilter", namespace = DxfNamespaces.DXF_2_0 )
public class ProgramStageInstanceFilter extends BaseIdentifiableObject implements MetadataObject
{

    private static final long serialVersionUID = 1L;

    /**
     * Property for filtering events by program
     */
    private String program;

    /**
     * Property for filtering events by programstage
     */
    private String programStage;

    /**
     * Property indicating description of programStageInstanceFilter
     */
    private String description;

    /**
     * Criteria object representing selected projections, filtering and sorting
     * criteria in events
     */
    private EventQueryCriteria eventQueryCriteria;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public ProgramStageInstanceFilter()
    {

    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    @Property( value = PropertyType.IDENTIFIER, required = Property.Value.TRUE )
    @PropertyRange( min = 11, max = 11 )
    public String getProgram()
    {
        return program;
    }

    public void setProgram( String program )
    {
        this.program = program;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    @Property( value = PropertyType.IDENTIFIER, required = Property.Value.FALSE )
    @PropertyRange( min = 11, max = 11 )
    public String getProgramStage()
    {
        return programStage;
    }

    public void setProgramStage( String programStage )
    {
        this.programStage = programStage;
    }

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
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public EventQueryCriteria getEventQueryCriteria()
    {
        return eventQueryCriteria;
    }

    public void setEventQueryCriteria( EventQueryCriteria eventQueryCriteria )
    {
        this.eventQueryCriteria = eventQueryCriteria;
    }

    public void copyValuesFrom( ProgramStageInstanceFilter psiFilter )
    {
        if ( psiFilter != null )
        {
            this.eventQueryCriteria = psiFilter.getEventQueryCriteria();
            this.program = psiFilter.getProgram();
            this.programStage = psiFilter.getProgramStage();

            this.userAccesses.clear();
            if ( psiFilter.getUserAccesses() != null )
            {
                this.userAccesses.addAll( psiFilter.getUserAccesses() );
            }

            this.userGroupAccesses.clear();
            if ( psiFilter.getUserGroupAccesses() != null )
            {
                this.userGroupAccesses.addAll( psiFilter.getUserGroupAccesses() );
            }

            this.code = psiFilter.getCode();
            this.name = psiFilter.getName();
            this.description = psiFilter.getDescription();
            this.publicAccess = psiFilter.getPublicAccess();
        }
    }

}
