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

@JacksonXmlRootElement( localName = "categoryDimension", namespace = DxfNamespaces.DXF_2_0 )
public class CategoryDimension
    implements DimensionalEmbeddedObject
{
    private int id;

    private Category dimension;

    private List<CategoryOption> items = new ArrayList<>();

    public int getId()
    {
        return id;
    }

    public void setId( int id )
    {
        this.id = id;
    }

    @JsonProperty( "category" )
    @JsonSerialize( as = BaseIdentifiableObject.class )
    @JacksonXmlProperty( localName = "category", namespace = DxfNamespaces.DXF_2_0 )
    public Category getDimension()
    {
        return dimension;
    }

    public void setDimension( Category dimension )
    {
        this.dimension = dimension;
    }

    @JsonProperty( "categoryOptions" )
    @JacksonXmlElementWrapper( localName = "categoryOptions", namespace = DxfNamespaces.DXF_2_0 )
    @JacksonXmlProperty( localName = "categoryOption", namespace = DxfNamespaces.DXF_2_0 )
    public List<CategoryOption> getItems()
    {
        return items;
    }

    public void setItems( List<CategoryOption> items )
    {
        this.items = items;
    }


    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "CategoryDimension{" );
        sb.append( "id=" ).append( id );
        sb.append( ", dimension=" ).append( dimension );
        sb.append( ", items=" ).append( items );
        sb.append( '}' );
        return sb.toString();
    }
}
