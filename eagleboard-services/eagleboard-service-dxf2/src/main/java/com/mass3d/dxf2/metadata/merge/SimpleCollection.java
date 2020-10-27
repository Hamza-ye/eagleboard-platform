package com.mass3d.dxf2.metadata.merge;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.util.ArrayList;
import java.util.List;

public class SimpleCollection
{
    private String name;

    private List<Simple> simples = new ArrayList<>();

    public SimpleCollection()
    {
    }

    public SimpleCollection( String name )
    {
        this.name = name;
    }

    @JsonProperty
    @JacksonXmlProperty
    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    @JsonProperty
    @JacksonXmlProperty( localName = "simple" )
    @JacksonXmlElementWrapper( localName = "simples" )
    public List<Simple> getSimples()
    {
        return simples;
    }

    public void setSimples( List<Simple> simples )
    {
        this.simples = simples;
    }
}
