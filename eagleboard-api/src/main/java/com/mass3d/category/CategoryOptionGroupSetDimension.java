package com.mass3d.category;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.ArrayList;
import java.util.List;
import com.mass3d.common.BaseIdentifiableObject;
import com.mass3d.common.DimensionalEmbeddedObject;
import com.mass3d.common.DxfNamespaces;

@JacksonXmlRootElement( localName = "categoryOptionGroupSetDimension", namespace = DxfNamespaces.DXF_2_0 )
public class CategoryOptionGroupSetDimension
    implements DimensionalEmbeddedObject
{
    private int id;
    
    private CategoryOptionGroupSet dimension;

    private List<CategoryOptionGroup> items = new ArrayList<>();

    public int getId()
    {
        return id;
    }

    public void setId( int id )
    {
        this.id = id;
    }

    @JsonProperty( "categoryOptionGroupSet" )
    @JsonSerialize( as = BaseIdentifiableObject.class )
    @JacksonXmlProperty( localName = "categoryOptionGroupSet", namespace = DxfNamespaces.DXF_2_0 )
    public CategoryOptionGroupSet getDimension()
    {
        return dimension;
    }

    public void setDimension( CategoryOptionGroupSet dimension )
    {
        this.dimension = dimension;
    }

    @JsonProperty( "categoryOptionGroups" )
    @JacksonXmlElementWrapper( localName = "categoryOptionGroups", namespace = DxfNamespaces.DXF_2_0 )
    @JacksonXmlProperty( localName = "categoryOptionGroup", namespace = DxfNamespaces.DXF_2_0 )
    public List<CategoryOptionGroup> getItems()
    {
        return items;
    }

    public void setItems( List<CategoryOptionGroup> items )
    {
        this.items = items;
    }
}
