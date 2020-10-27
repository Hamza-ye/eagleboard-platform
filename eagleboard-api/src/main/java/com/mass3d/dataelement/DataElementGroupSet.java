package com.mass3d.dataelement;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import com.mass3d.common.BaseDimensionalItemObject;
import com.mass3d.common.BaseDimensionalObject;
import com.mass3d.common.BaseIdentifiableObject;
import com.mass3d.common.DimensionType;
import com.mass3d.common.DimensionalItemObject;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.common.MetadataObject;

/**
 * DataElementGroupSet is a set of DataElementGroups. It is by default
 * exclusive, in the sense that a DataElement can only be a member of one or
 * zero of the DataElementGroups in a DataElementGroupSet.
 *
 */
@JacksonXmlRootElement( localName = "dataElementGroupSet", namespace = DxfNamespaces.DXF_2_0 )
public class DataElementGroupSet
    extends BaseDimensionalObject implements MetadataObject
{
    private Boolean compulsory = false;

    private List<DataElementGroup> members = new ArrayList<>();

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public DataElementGroupSet()
    {

    }

    public DataElementGroupSet( String name )
    {
        this.name = name;
        this.compulsory = false;
    }

    public DataElementGroupSet( String name, Boolean compulsory )
    {
        this( name );
        this.compulsory = compulsory;
    }

    public DataElementGroupSet( String name, String description, Boolean compulsory )
    {
        this( name, compulsory );
        this.description = description;
    }

    public DataElementGroupSet( String name, String description, boolean compulsory, boolean dataDimension )
    {
        this( name, description, compulsory );
        this.dataDimension = dataDimension;
    }

    // -------------------------------------------------------------------------
    // Logic
    // -------------------------------------------------------------------------

    public void addDataElementGroup( DataElementGroup dataElementGroup )
    {
        members.add( dataElementGroup );
        dataElementGroup.getGroupSets().add( this );
    }

    public void removeDataElementGroup( DataElementGroup dataElementGroup )
    {
        members.remove( dataElementGroup );
        dataElementGroup.getGroupSets().remove( this );
    }

    public void removeAllDataElementGroups()
    {
        for ( DataElementGroup dataElementGroup : members )
        {
            dataElementGroup.getGroupSets().remove( this );
        }

        members.clear();
    }

    public Collection<DataElement> getDataElements()
    {
        List<DataElement> dataElements = new ArrayList<>();

        for ( DataElementGroup group : members )
        {
            dataElements.addAll( group.getMembers() );
        }

        return dataElements;
    }

    public DataElementGroup getGroup( DataElement dataElement )
    {
        for ( DataElementGroup group : members )
        {
            if ( group.getMembers().contains( dataElement ) )
            {
                return group;
            }
        }

        return null;
    }

    public Boolean isMemberOfDataElementGroups( DataElement dataElement )
    {
        for ( DataElementGroup group : members )
        {
            if ( group.getMembers().contains( dataElement ) )
            {
                return true;
            }
        }

        return false;
    }

    public Boolean hasDataElementGroups()
    {
        return members != null && members.size() > 0;
    }

    public List<DataElementGroup> getSortedGroups()
    {
        List<DataElementGroup> sortedGroups = new ArrayList<>( members );

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
        return Lists.newArrayList( members );
    }

    @Override
    public DimensionType getDimensionType()
    {
        return DimensionType.DATA_ELEMENT_GROUP_SET;
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public Boolean isCompulsory()
    {
        if ( compulsory == null )
        {
            return false;
        }

        return compulsory;
    }

    public void setCompulsory( Boolean compulsory )
    {
        this.compulsory = compulsory;
    }

    @JsonProperty( "dataElementGroups" )
    @JsonSerialize( contentAs = BaseIdentifiableObject.class )
    @JacksonXmlElementWrapper( localName = "dataElementGroups", namespace = DxfNamespaces.DXF_2_0 )
    @JacksonXmlProperty( localName = "dataElementGroup", namespace = DxfNamespaces.DXF_2_0 )
    public List<DataElementGroup> getMembers()
    {
        return members;
    }

    public void setMembers( List<DataElementGroup> members )
    {
        this.members = members;
    }
}
