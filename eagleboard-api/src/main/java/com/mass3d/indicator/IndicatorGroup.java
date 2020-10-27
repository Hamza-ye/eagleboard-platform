package com.mass3d.indicator;

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
import com.mass3d.schema.PropertyType;
import com.mass3d.schema.annotation.Property;
import com.mass3d.schema.annotation.PropertyRange;

@JacksonXmlRootElement( localName = "indicatorGroup", namespace = DxfNamespaces.DXF_2_0 )
public class IndicatorGroup
    extends BaseIdentifiableObject implements MetadataObject
{
    private String description;

    private Set<Indicator> members = new HashSet<>();

    private IndicatorGroupSet groupSet;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public IndicatorGroup()
    {
    }

    public IndicatorGroup( String name )
    {
        this.name = name;
    }

    // -------------------------------------------------------------------------
    // Logic
    // -------------------------------------------------------------------------

    public void addIndicator( Indicator indicator )
    {
        members.add( indicator );
        indicator.getGroups().add( this );
    }

    public void removeIndicator( Indicator indicator )
    {
        members.remove( indicator );
        indicator.getGroups().remove( this );
    }

    public void updateIndicators( Set<Indicator> updates )
    {
        for ( Indicator indicator : new HashSet<>( members ) )
        {
            if ( !updates.contains( indicator ) )
            {
                removeIndicator( indicator );
            }
        }

        for ( Indicator indicator : updates )
        {
            addIndicator( indicator );
        }
    }

    // -------------------------------------------------------------------------
    // Logic
    // -------------------------------------------------------------------------

    public void removeAllIndicators()
    {
        members.clear();
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    @PropertyRange( min = 2 )
    public String getDescription()
    {
        return description;
    }

    public void setDescription( String description )
    {
        this.description = description;
    }

    @JsonProperty( "indicators" )
    @JsonSerialize( contentAs = BaseIdentifiableObject.class )
    @JacksonXmlElementWrapper( localName = "indicators", namespace = DxfNamespaces.DXF_2_0 )
    @JacksonXmlProperty( localName = "indicator", namespace = DxfNamespaces.DXF_2_0 )
    public Set<Indicator> getMembers()
    {
        return members;
    }

    public void setMembers( Set<Indicator> members )
    {
        this.members = members;
    }

    @JsonProperty( "indicatorGroupSet" )
    @JsonSerialize( as = BaseIdentifiableObject.class )
    @JacksonXmlProperty( localName = "indicatorGroupSet", namespace = DxfNamespaces.DXF_2_0 )
    @Property( value = PropertyType.REFERENCE, required = Property.Value.FALSE )
    public IndicatorGroupSet getGroupSet()
    {
        return groupSet;
    }

    public void setGroupSet( IndicatorGroupSet groupSet )
    {
        this.groupSet = groupSet;
    }
}
