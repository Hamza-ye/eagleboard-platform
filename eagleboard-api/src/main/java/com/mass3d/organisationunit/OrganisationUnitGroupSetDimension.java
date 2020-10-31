package com.mass3d.organisationunit;

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

@JacksonXmlRootElement( localName = "organisationUnitGroupSetDimension", namespace = DxfNamespaces.DXF_2_0 )
public class OrganisationUnitGroupSetDimension
    implements DimensionalEmbeddedObject
{
    private int id;
    
    private OrganisationUnitGroupSet dimension;
    
    private List<OrganisationUnitGroup> items = new ArrayList<>();

    public int getId()
    {
        return id;
    }

    public void setId( int id )
    {
        this.id = id;
    }

    @JsonProperty( "organisationUnitGroupSet" )
    @JsonSerialize( as = BaseIdentifiableObject.class )
    @JacksonXmlProperty( localName = "organisationUnitGroupSet", namespace = DxfNamespaces.DXF_2_0 )
    public OrganisationUnitGroupSet getDimension()
    {
        return dimension;
    }

    public void setDimension( OrganisationUnitGroupSet dimension )
    {
        this.dimension = dimension;
    }

    @JsonProperty( "organisationUnitGroups" )
    @JacksonXmlElementWrapper( localName = "organisationUnitGroups", namespace = DxfNamespaces.DXF_2_0 )
    @JacksonXmlProperty( localName = "organisationUnitGroup", namespace = DxfNamespaces.DXF_2_0 )
    public List<OrganisationUnitGroup> getItems()
    {
        return items;
    }

    public void setItems( List<OrganisationUnitGroup> items )
    {
        this.items = items;
    }
}
