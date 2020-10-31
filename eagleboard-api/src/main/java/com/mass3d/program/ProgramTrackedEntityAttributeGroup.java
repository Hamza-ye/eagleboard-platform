package com.mass3d.program;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.HashSet;
import java.util.Set;
import com.mass3d.common.BaseIdentifiableObject;
import com.mass3d.common.BaseNameableObject;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.common.MetadataObject;

@JacksonXmlRootElement( localName = "programTrackedEntityAttributeGroup", namespace = DxfNamespaces.DXF_2_0 )
public class ProgramTrackedEntityAttributeGroup
    extends BaseNameableObject implements MetadataObject
{
    private Set<ProgramTrackedEntityAttribute> attributes = new HashSet<>();

    private UniqunessType uniqunessType;

    private String description;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public ProgramTrackedEntityAttributeGroup()
    {
    }

    // -------------------------------------------------------------------------
    // Logic
    // -------------------------------------------------------------------------

    public void addAttribute( ProgramTrackedEntityAttribute attribute )
    {
        attributes.add( attribute );
        attribute.getGroups().add( this );
    }

    public void removeAttribute( ProgramTrackedEntityAttribute attribute )
    {
        attributes.remove( attribute );
        attribute.getGroups().remove( this );
    }

    public void removeAllAttributes()
    {
        for ( ProgramTrackedEntityAttribute attribute : attributes )
        {
            attribute.getGroups().remove( this );
        }

        attributes.clear();
    }

    public void updateAttributes( Set<ProgramTrackedEntityAttribute> updates )
    {
        for ( ProgramTrackedEntityAttribute attribute : new HashSet<>( attributes ) )
        {
            if ( !updates.contains( attribute ) )
            {
                removeAttribute( attribute );
            }
        }

        for ( ProgramTrackedEntityAttribute attribute : updates )
        {
            addAttribute( attribute );
        }
    }

    // -------------------------------------------------------------------------
    // Getters and Setters
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

    @JsonProperty( value = "attributes" )
    @JsonSerialize( contentAs = BaseIdentifiableObject.class )
    @JacksonXmlElementWrapper( localName = "attributes", namespace = DxfNamespaces.DXF_2_0 )
    @JacksonXmlProperty( localName = "attribute", namespace = DxfNamespaces.DXF_2_0 )
    public Set<ProgramTrackedEntityAttribute> getAttributes()
    {
        return attributes;
    }

    public void setAttributes( Set<ProgramTrackedEntityAttribute> attributes )
    {
        this.attributes = attributes;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public UniqunessType getUniqunessType()
    {
        return uniqunessType;
    }

    public void setUniqunessType( UniqunessType uniqunessType )
    {
        this.uniqunessType = uniqunessType;
    }
}
