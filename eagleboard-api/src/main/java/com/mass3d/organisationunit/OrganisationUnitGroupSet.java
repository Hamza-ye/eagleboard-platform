package com.mass3d.organisationunit;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.mass3d.common.BaseDimensionalItemObject;
import com.mass3d.common.BaseDimensionalObject;
import com.mass3d.common.DimensionType;
import com.mass3d.common.DimensionalItemObject;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.common.MetadataObject;
import com.mass3d.common.adapter.JacksonOrganisationUnitGroupSymbolSerializer;

@JacksonXmlRootElement( localName = "organisationUnitGroupSet", namespace = DxfNamespaces.DXF_2_0 )
public class OrganisationUnitGroupSet
    extends BaseDimensionalObject implements MetadataObject
{
    private boolean compulsory;

    private boolean includeSubhierarchyInAnalytics;

    private Set<OrganisationUnitGroup> organisationUnitGroups = new HashSet<>();

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public OrganisationUnitGroupSet()
    {
    }

    public OrganisationUnitGroupSet( String name, String description, boolean compulsory )
    {
        this.name = name;
        this.description = description;
        this.compulsory = compulsory;
        this.includeSubhierarchyInAnalytics = false;
    }

    public OrganisationUnitGroupSet( String name, String description, boolean compulsory, boolean dataDimension )
    {
        this( name, description, compulsory );
        this.dataDimension = dataDimension;
        this.includeSubhierarchyInAnalytics = false;
    }

    // -------------------------------------------------------------------------
    // Logic
    // -------------------------------------------------------------------------

    public void addOrganisationUnitGroup( OrganisationUnitGroup organisationUnitGroup )
    {
        organisationUnitGroups.add( organisationUnitGroup );
        organisationUnitGroup.getGroupSets().add( this );
    }

    public void removeOrganisationUnitGroup( OrganisationUnitGroup organisationUnitGroup )
    {
        organisationUnitGroups.remove( organisationUnitGroup );
        organisationUnitGroup.getGroupSets().remove( this );
    }

    public void removeAllOrganisationUnitGroups()
    {
        for ( OrganisationUnitGroup group : organisationUnitGroups )
        {
            group.getGroupSets().remove( this );
        }

        organisationUnitGroups.clear();
    }

    public Collection<OrganisationUnit> getOrganisationUnits()
    {
        List<OrganisationUnit> units = new ArrayList<>();

        for ( OrganisationUnitGroup group : organisationUnitGroups )
        {
            units.addAll( group.getMembers() );
        }

        return units;
    }

    public boolean isMemberOfOrganisationUnitGroups( OrganisationUnit organisationUnit )
    {
        for ( OrganisationUnitGroup group : organisationUnitGroups )
        {
            if ( group.getMembers().contains( organisationUnit ) )
            {
                return true;
            }
        }

        return false;
    }

    public boolean hasOrganisationUnitGroups()
    {
        return organisationUnitGroups != null && organisationUnitGroups.size() > 0;
    }

    public OrganisationUnitGroup getGroup( OrganisationUnit unit )
    {
        for ( OrganisationUnitGroup group : organisationUnitGroups )
        {
            if ( group.getMembers().contains( unit ) )
            {
                return group;
            }
        }

        return null;
    }

    public List<OrganisationUnitGroup> getSortedGroups()
    {
        List<OrganisationUnitGroup> sortedGroups = new ArrayList<>( organisationUnitGroups );

        Collections.sort( sortedGroups );

        return sortedGroups;
    }

    @Override
    public String getShortName()
    {
        if ( getName() == null || getName().length() <= 50 )
        {
            return getName();
        }
        else
        {
            return getName().substring( 0, 49 );
        }
    }

    // -------------------------------------------------------------------------
    // Dimensional object
    // -------------------------------------------------------------------------

    @Override
    @JsonProperty
    @JsonSerialize( contentAs = BaseDimensionalItemObject.class )
    @JacksonXmlElementWrapper( localName = "items", namespace = DxfNamespaces.DXF_2_0 )
    @JacksonXmlProperty( localName = "item", namespace = DxfNamespaces.DXF_2_0 )
    public List<DimensionalItemObject> getItems()
    {
        return new ArrayList<>( organisationUnitGroups );
    }

    @Override
    public DimensionType getDimensionType()
    {
        return DimensionType.ORGANISATION_UNIT_GROUP_SET;
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public boolean isCompulsory()
    {
        return compulsory;
    }

    public void setCompulsory( boolean compulsory )
    {
        this.compulsory = compulsory;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public boolean isIncludeSubhierarchyInAnalytics()
    {
        return includeSubhierarchyInAnalytics;
    }

    public void setIncludeSubhierarchyInAnalytics( boolean includeSubhierarchyInAnalytics )
    {
        this.includeSubhierarchyInAnalytics = includeSubhierarchyInAnalytics;
    }


    @JsonProperty( "organisationUnitGroups" )
    // @JsonSerialize(contentAs = BaseIdentifiableObject.class)
    @JsonSerialize( contentUsing = JacksonOrganisationUnitGroupSymbolSerializer.class )
    @JacksonXmlElementWrapper( localName = "organisationUnitGroups", namespace = DxfNamespaces.DXF_2_0 )
    @JacksonXmlProperty( localName = "organisationUnitGroup", namespace = DxfNamespaces.DXF_2_0 )
    public Set<OrganisationUnitGroup> getOrganisationUnitGroups()
    {
        return organisationUnitGroups;
    }

    public void setOrganisationUnitGroups( Set<OrganisationUnitGroup> organisationUnitGroups )
    {
        this.organisationUnitGroups = organisationUnitGroups;
    }
}
