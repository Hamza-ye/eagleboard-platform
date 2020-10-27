package com.mass3d.webapi.webdomain.sharing;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Meta
{
    @JsonProperty
    private boolean allowPublicAccess;

    @JsonProperty
    private boolean allowExternalAccess;

    public Meta()
    {
    }

    public boolean isAllowPublicAccess()
    {
        return allowPublicAccess;
    }

    public void setAllowPublicAccess( boolean allowPublicAccess )
    {
        this.allowPublicAccess = allowPublicAccess;
    }

    public boolean isAllowExternalAccess()
    {
        return allowExternalAccess;
    }

    public void setAllowExternalAccess( boolean allowExternalAccess )
    {
        this.allowExternalAccess = allowExternalAccess;
    }
}
