package com.mass3d.dataelement;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.HashSet;
import java.util.Set;
import com.mass3d.analytics.AggregationType;
import com.mass3d.common.BaseDimensionalItemObject;
import com.mass3d.common.BaseIdentifiableObject;
import com.mass3d.common.DimensionItemType;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.common.MetadataObject;
import com.mass3d.common.ValueType;
import com.mass3d.period.PeriodType;

@JacksonXmlRootElement( localName = "dataElementGroup", namespace = DxfNamespaces.DXF_2_0 )
public class DataElementGroup
    extends BaseDimensionalItemObject implements MetadataObject
{
    private Set<DataElement> members = new HashSet<>();

    private Set<DataElementGroupSet> groupSets = new HashSet<>();

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public DataElementGroup()
    {
    }

    public DataElementGroup( String name )
    {
        this.name = name;
    }

    // -------------------------------------------------------------------------
    // Logic
    // -------------------------------------------------------------------------

    public void addDataElement( DataElement dataElement )
    {
        members.add( dataElement );
        dataElement.getGroups().add( this );
    }

    public void removeDataElement( DataElement dataElement )
    {
        members.remove( dataElement );
        dataElement.getGroups().remove( this );
    }

    public void removeAllDataElements()
    {
        for ( DataElement dataElement : members )
        {
            dataElement.getGroups().remove( this );
        }

        members.clear();
    }

    public void updateDataElements( Set<DataElement> updates )
    {
        for ( DataElement dataElement : new HashSet<>( members ) )
        {
            if ( !updates.contains( dataElement ) )
            {
                removeDataElement( dataElement );
            }
        }

        for ( DataElement dataElement : updates )
        {
            addDataElement( dataElement );
        }
    }

    /**
     * Returns the value type of the data elements in this group. Uses an arbitrary
     * member to determine the value type.
     */
    public ValueType getValueType()
    {
        return hasMembers() ? members.iterator().next().getValueType() : null;
    }

    /**
     * Returns the aggregation type of the data elements in this group. Uses
     * an arbitrary member to determine the aggregation operator.
     */
    public AggregationType getAggregationType()
    {
        return hasMembers() ? members.iterator().next().getAggregationType() : null;
    }

    /**
     * Returns the period type of the data elements in this group. Uses an
     * arbitrary member to determine the period type.
     */
    public PeriodType getPeriodType()
    {
        return hasMembers() ? members.iterator().next().getPeriodType() : null;
    }

    /**
     * Indicates whether this group has a period type.
     */
    public boolean hasPeriodType()
    {
        return getPeriodType() != null;
    }

    /**
     * Indicates whether this group has any members.
     */
    public boolean hasMembers()
    {
        return members != null && !members.isEmpty();
    }

    // -------------------------------------------------------------------------
    // DimensionalItemObject
    // -------------------------------------------------------------------------

    @Override
    public DimensionItemType getDimensionItemType()
    {
        return DimensionItemType.DATA_ELEMENT_GROUP;
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    @JsonProperty( "dataElements" )
    @JsonSerialize( contentAs = BaseIdentifiableObject.class )
    @JacksonXmlElementWrapper( localName = "dataElements", namespace = DxfNamespaces.DXF_2_0 )
    @JacksonXmlProperty( localName = "dataElement", namespace = DxfNamespaces.DXF_2_0 )
    public Set<DataElement> getMembers()
    {
        return members;
    }

    public void setMembers( Set<DataElement> members )
    {
        this.members = members;
    }

    @JsonProperty
    @JsonSerialize( contentAs = BaseIdentifiableObject.class )
    @JacksonXmlElementWrapper( localName = "groupSets", namespace = DxfNamespaces.DXF_2_0 )
    @JacksonXmlProperty( localName = "groupSet", namespace = DxfNamespaces.DXF_2_0 )
    public Set<DataElementGroupSet> getGroupSets()
    {
        return groupSets;
    }

    public void setGroupSets( Set<DataElementGroupSet> groupSets )
    {
        this.groupSets = groupSets;
    }
}
