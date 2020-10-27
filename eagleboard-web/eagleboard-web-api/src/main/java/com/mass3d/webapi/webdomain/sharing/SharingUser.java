package com.mass3d.webapi.webdomain.sharing;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SharingUser
{
    private String id;

    private String name;

    public SharingUser()
    {
    }

    @JsonProperty
    public String getId()
    {
        return id;
    }

    public void setId( String id )
    {
        this.id = id;
    }

    @JsonProperty
    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }
}
