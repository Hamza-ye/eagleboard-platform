package com.mass3d.webapi.webdomain.sharing;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Sharing
{
    @JsonProperty
    private Meta meta = new Meta();

    @JsonProperty
    private SharingObject object = new SharingObject();

    public Sharing()
    {
    }

    public Meta getMeta()
    {
        return meta;
    }

    public void setMeta( Meta meta )
    {
        this.meta = meta;
    }

    public SharingObject getObject()
    {
        return object;
    }

    public void setObject( SharingObject object )
    {
        this.object = object;
    }
}
