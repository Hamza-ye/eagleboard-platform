package com.mass3d.organisationunit;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.mass3d.common.BaseIdentifiableObject;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.common.MetadataObject;
import com.mass3d.schema.annotation.PropertyRange;

@JacksonXmlRootElement( localName = "organisationUnitLevel", namespace = DxfNamespaces.DXF_2_0 )
public class OrganisationUnitLevel
    extends BaseIdentifiableObject implements MetadataObject
{
    private int level;

    private Integer offlineLevels;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public OrganisationUnitLevel()
    {
    }

    public OrganisationUnitLevel( int level, String name )
    {
        this.level = level;
        this.name = name;
    }

    public OrganisationUnitLevel( int level, String name, Integer offlineLevels )
    {
        this.level = level;
        this.name = name;
        this.offlineLevels = offlineLevels;
    }

    @Override
    public String toString()
    {
        return "[Name: " + name + ", level: " + level + "]";
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    @JsonProperty
    @JacksonXmlProperty( isAttribute = true )
    @PropertyRange( min = 1, max = 999 )
    public int getLevel()
    {
        return level;
    }

    public void setLevel( int level )
    {
        this.level = level;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public Integer getOfflineLevels()
    {
        return offlineLevels;
    }

    public void setOfflineLevels( Integer offlineLevels )
    {
        this.offlineLevels = offlineLevels;
    }
}
