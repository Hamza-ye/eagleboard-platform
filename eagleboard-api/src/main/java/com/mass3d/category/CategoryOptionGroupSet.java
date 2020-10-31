package com.mass3d.category;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.apache.commons.collections4.CollectionUtils;
import com.mass3d.common.BaseDimensionalItemObject;
import com.mass3d.common.BaseDimensionalObject;
import com.mass3d.common.BaseIdentifiableObject;
import com.mass3d.common.DataDimensionType;
import com.mass3d.common.DimensionType;
import com.mass3d.common.DimensionalItemObject;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.common.MetadataObject;

@JacksonXmlRootElement( localName = "categoryOptionGroupSet", namespace = DxfNamespaces.DXF_2_0 )
public class CategoryOptionGroupSet
    extends BaseDimensionalObject implements MetadataObject
{
    private List<CategoryOptionGroup> members = new ArrayList<>();

    private DataDimensionType dataDimensionType;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public CategoryOptionGroupSet()
    {
    }

    public CategoryOptionGroupSet( String name )
    {
        this.name = name;
    }

    // -------------------------------------------------------------------------
    // Logic
    // -------------------------------------------------------------------------

    // TODO link group set to category to avoid conflicting grouping of category
    // option combos

    public CategoryOptionGroup getGroup( CategoryOptionCombo optionCombo )
    {
        Set<CategoryOption> categoryOptions = optionCombo.getCategoryOptions();

        for ( CategoryOptionGroup group : members )
        {
            if ( !CollectionUtils.intersection( group.getMembers(), categoryOptions ).isEmpty() )
            {
                return group;
            }
        }

        return null;
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
        return DimensionType.CATEGORY_OPTION_GROUP_SET;
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    @JsonProperty( "categoryOptionGroups" )
    @JsonSerialize( contentAs = BaseIdentifiableObject.class )
    @JacksonXmlElementWrapper( localName = "categoryOptionGroups", namespace = DxfNamespaces.DXF_2_0 )
    @JacksonXmlProperty( localName = "categoryOptionGroup", namespace = DxfNamespaces.DXF_2_0 )
    public List<CategoryOptionGroup> getMembers()
    {
        return members;
    }

    public void setMembers( List<CategoryOptionGroup> members )
    {
        this.members = members;
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

    public void addCategoryOptionGroup( CategoryOptionGroup categoryOptionGroup )
    {
        members.add( categoryOptionGroup );
        categoryOptionGroup.getGroupSets().add( this );
    }

    public void removeCategoryOptionGroup( CategoryOptionGroup categoryOptionGroup )
    {
        members.remove( categoryOptionGroup );
        categoryOptionGroup.getGroupSets().remove( this );
    }
}
