package com.mass3d.webapi.webdomain.sharing;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

public class SharingUserAccess
{
    private String id;

    private String name;

    private String displayName;

    private String access;

    public SharingUserAccess()
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

    @JsonProperty
    public String getDisplayName()
    {
        return displayName;
    }

    public void setDisplayName( String displayName )
    {
        this.displayName = displayName;
    }

    @JsonProperty
    public String getAccess()
    {
        return access;
    }

    public void setAccess( String access )
    {
        this.access = access;
    }

    public String toString()
    {
        return MoreObjects.toStringHelper( this ).
            add( "id", id ).add( "name", name ).add( "access", access ).toString();
    }
}
