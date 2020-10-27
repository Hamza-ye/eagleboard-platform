package com.mass3d.dataelement;

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

@JacksonXmlRootElement( localName = "dataElementGroupSetDimension", namespace = DxfNamespaces.DXF_2_0 )
public class DataElementGroupSetDimension
    implements DimensionalEmbeddedObject
{
    private int id;
    
    private DataElementGroupSet dimension;
    
    private List<DataElementGroup> items = new ArrayList<>();

    public int getId()
    {
        return id;
    }

    public void setId( int id )
    {
        this.id = id;
    }

    @JsonProperty( "dataElementGroupSet" )
    @JsonSerialize( as = BaseIdentifiableObject.class )
    @JacksonXmlProperty( localName = "dataElementGroupSet", namespace = DxfNamespaces.DXF_2_0 )
    public DataElementGroupSet getDimension()
    {
        return dimension;
    }

    public void setDimension( DataElementGroupSet dimension )
    {
        this.dimension = dimension;
    }

    @JsonProperty( "dataElementGroups" )
    @JacksonXmlElementWrapper( localName = "dataElementGroups", namespace = DxfNamespaces.DXF_2_0 )
    @JacksonXmlProperty( localName = "dataElementGroup", namespace = DxfNamespaces.DXF_2_0 )
    public List<DataElementGroup> getItems()
    {
        return items;
    }

    public void setItems( List<DataElementGroup> items )
    {
        this.items = items;
    }
}
