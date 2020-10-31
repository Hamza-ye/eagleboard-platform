package com.mass3d.program;

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
import com.mass3d.schema.annotation.PropertyRange;

@JacksonXmlRootElement( localName = "programIndicatorGroup", namespace = DxfNamespaces.DXF_2_0 )
public class ProgramIndicatorGroup
    extends BaseIdentifiableObject implements MetadataObject
{
    private Set<ProgramIndicator> members = new HashSet<>();

    private String description;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public ProgramIndicatorGroup()
    {
    }

    public ProgramIndicatorGroup( String name )
    {
        this.name = name;
    }

    // -------------------------------------------------------------------------
    // Logic
    // -------------------------------------------------------------------------

    public void addProgramIndicator( ProgramIndicator programIndicator )
    {
        members.add( programIndicator );
        programIndicator.getGroups().add( this );
    }

    public void removeProgramIndicator( ProgramIndicator indicator )
    {
        members.remove( indicator );
        indicator.getGroups().remove( this );
    }

    public void updateProgramIndicators( Set<ProgramIndicator> updates )
    {
        for ( ProgramIndicator indicator : new HashSet<>( members ) )
        {
            if ( !updates.contains( indicator ) )
            {
                removeProgramIndicator( indicator );
            }
        }

        for ( ProgramIndicator indicator : updates )
        {
            addProgramIndicator( indicator );
        }
    }

    // -------------------------------------------------------------------------
    // Logic
    // -------------------------------------------------------------------------

    public void removeAllProgramIndicators()
    {
        members.clear();
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    @PropertyRange( min = 1 )
    public String getDescription()
    {
        return description;
    }

    public void setDescription( String description )
    {
        this.description = description;
    }

    @JsonProperty( "programIndicators" )
    @JsonSerialize( contentAs = BaseIdentifiableObject.class )
    @JacksonXmlElementWrapper( localName = "programIndicators", namespace = DxfNamespaces.DXF_2_0 )
    @JacksonXmlProperty( localName = "programIndicator", namespace = DxfNamespaces.DXF_2_0 )
    public Set<ProgramIndicator> getMembers()
    {
        return members;
    }

    public void setMembers( Set<ProgramIndicator> members )
    {
        this.members = members;
    }
}
