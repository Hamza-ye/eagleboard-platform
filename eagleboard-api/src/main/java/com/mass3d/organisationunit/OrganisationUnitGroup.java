package com.mass3d.organisationunit;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.vividsolutions.jts.geom.Geometry;
import java.util.HashSet;
import java.util.Set;
import com.mass3d.common.*;
import com.mass3d.common.coordinate.CoordinateObject;
import com.mass3d.common.coordinate.CoordinateUtils;

@JacksonXmlRootElement( localName = "organisationUnitGroup", namespace = DxfNamespaces.DXF_2_0 )
public class OrganisationUnitGroup
    extends BaseDimensionalItemObject
    implements MetadataObject, CoordinateObject
{
    private String symbol;
    
    private String color;

    private Set<OrganisationUnit> members = new HashSet<>();

    private Set<OrganisationUnitGroupSet> groupSets = new HashSet<>();

    private Geometry geometry;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public OrganisationUnitGroup()
    {
    }

    public OrganisationUnitGroup( String name )
    {
        this.name = name;
    }

    // -------------------------------------------------------------------------
    // Logic
    // -------------------------------------------------------------------------

    public boolean addOrganisationUnit( OrganisationUnit organisationUnit )
    {
        members.add( organisationUnit );
        return organisationUnit.getGroups().add( this );
    }

    public boolean removeOrganisationUnit( OrganisationUnit organisationUnit )
    {
        members.remove( organisationUnit );
        return organisationUnit.getGroups().remove( this );
    }

    public void removeAllOrganisationUnits()
    {
        for ( OrganisationUnit organisationUnit : members )
        {
            organisationUnit.getGroups().remove( this );
        }

        members.clear();
    }

    public void updateOrganisationUnits( Set<OrganisationUnit> updates )
    {
        for ( OrganisationUnit unit : new HashSet<>( members ) )
        {
            if ( !updates.contains( unit ) )
            {
                removeOrganisationUnit( unit );
            }
        }

        for ( OrganisationUnit unit : updates )
        {
            addOrganisationUnit( unit );
        }
    }

    public boolean hasSymbol()
    {
        return symbol != null && !symbol.trim().isEmpty();
    }

    // -------------------------------------------------------------------------
    // DimensionalItemObject
    // -------------------------------------------------------------------------

    @Override
    public DimensionItemType getDimensionItemType()
    {
        return DimensionItemType.ORGANISATION_UNIT_GROUP;
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getSymbol()
    {
        return symbol;
    }

    public void setSymbol( String symbol )
    {
        this.symbol = symbol;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getColor()
    {
        return color;
    }

    public void setColor( String color )
    {
        this.color = color;
    }

    @JsonProperty( "organisationUnits" )
    @JsonSerialize( contentAs = BaseIdentifiableObject.class )
    @JacksonXmlElementWrapper( localName = "organisationUnits", namespace = DxfNamespaces.DXF_2_0 )
    @JacksonXmlProperty( localName = "organisationUnit", namespace = DxfNamespaces.DXF_2_0 )
    public Set<OrganisationUnit> getMembers()
    {
        return members;
    }

    public void setMembers( Set<OrganisationUnit> members )
    {
        this.members = members;
    }

    @JsonProperty
    @JsonSerialize( contentAs = BaseIdentifiableObject.class )
    @JacksonXmlElementWrapper( localName = "groupSets", namespace = DxfNamespaces.DXF_2_0 )
    @JacksonXmlProperty( localName = "groupSet", namespace = DxfNamespaces.DXF_2_0 )
    public Set<OrganisationUnitGroupSet> getGroupSets()
    {
        return groupSets;
    }

    public void setGroupSets( Set<OrganisationUnitGroupSet> groupSets )
    {
        this.groupSets = groupSets;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public Geometry getGeometry()
    {
        return geometry;
    }

    public void setGeometry( Geometry geometry )
    {
        this.geometry = geometry;
    }


    public boolean hasDescendantsWithCoordinates()
    {
        return CoordinateUtils.hasDescendantsWithCoordinates( members );
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public FeatureType getFeatureType()
    {

        return geometry != null ? FeatureType.getTypeFromName(this.geometry.getGeometryType()) : null;
    }

    @Override
    public String getCoordinates()
    {
        return extractCoordinates( this.geometry );
    }

    @Override
    public boolean hasCoordinates()
    {
        return this.geometry != null;
    }
}