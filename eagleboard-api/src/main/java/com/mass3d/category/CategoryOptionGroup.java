package com.mass3d.category;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.HashSet;
import java.util.Set;
import com.mass3d.common.BaseDimensionalItemObject;
import com.mass3d.common.BaseIdentifiableObject;
import com.mass3d.common.DataDimensionType;
import com.mass3d.common.DimensionItemType;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.common.MetadataObject;

@JacksonXmlRootElement( localName = "categoryOptionGroup", namespace = DxfNamespaces.DXF_2_0 )
public class CategoryOptionGroup
    extends BaseDimensionalItemObject implements MetadataObject
{
    private Set<CategoryOption> members = new HashSet<>();

    private Set<CategoryOptionGroupSet> groupSets = new HashSet<>();

    private DataDimensionType dataDimensionType;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public CategoryOptionGroup()
    {

    }

    public CategoryOptionGroup( String name )
    {
        this();
        this.name = name;
    }


    // -------------------------------------------------------------------------
    // DimensionalItemObject
    // -------------------------------------------------------------------------

    @Override
    public DimensionItemType getDimensionItemType()
    {
        return DimensionItemType.CATEGORY_OPTION_GROUP;
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    @JsonProperty( "categoryOptions" )
    @JsonSerialize( contentAs = BaseIdentifiableObject.class )
    @JacksonXmlElementWrapper( localName = "categoryOptions", namespace = DxfNamespaces.DXF_2_0 )
    @JacksonXmlProperty( localName = "categoryOption", namespace = DxfNamespaces.DXF_2_0 )
    public Set<CategoryOption> getMembers()
    {
        return members;
    }

    public void setMembers( Set<CategoryOption> members )
    {
        this.members = members;
    }

    @JsonProperty
    @JsonSerialize( contentAs = BaseIdentifiableObject.class )
    @JacksonXmlElementWrapper( localName = "groupSets", namespace = DxfNamespaces.DXF_2_0 )
    @JacksonXmlProperty( localName = "groupSet", namespace = DxfNamespaces.DXF_2_0 )
    public Set<CategoryOptionGroupSet> getGroupSets()
    {
        return groupSets;
    }

    public void setGroupSets( Set<CategoryOptionGroupSet> groupSets )
    {
        this.groupSets = groupSets;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public DataDimensionType getDataDimensionType()
    {
        return dataDimensionType;
    }

    public void setDataDimensionType( DataDimensionType dataDimensionType )
    {
        this.dataDimensionType = dataDimensionType;
    }

    // -------------------------------------------------------------------------
    // Logic
    // -------------------------------------------------------------------------

    public void addCategoryOption( CategoryOption categoryOption )
    {
        members.add( categoryOption );
        categoryOption.getGroups().add( this );
    }

    public void removeCategoryOption( CategoryOption categoryOption )
    {
        members.remove( categoryOption );
        categoryOption.getGroups().remove( this );
    }
}
