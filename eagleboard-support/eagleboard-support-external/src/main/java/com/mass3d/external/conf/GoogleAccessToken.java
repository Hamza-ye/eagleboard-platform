package com.mass3d.external.conf;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public class GoogleAccessToken
{
    private String accessToken;
    
    private String clientId;

    private long expiresInSeconds;
    
    private LocalDateTime expiresOn;
    
    public GoogleAccessToken()
    {
    }

    @JsonProperty( value = "access_token" )
    public String getAccessToken()
    {
        return accessToken;
    }

    public void setAccessToken( String accessToken )
    {
        this.accessToken = accessToken;
    }

    @JsonProperty( value = "client_id" )
    public String getClientId()
    {
        return clientId;
    }

    public void setClientId( String clientId )
    {
        this.clientId = clientId;
    }
    
    @JsonProperty( value = "expires_in" )
    public long getExpiresInSeconds()
    {
        return expiresInSeconds;
    }

    public void setExpiresInSeconds( long expiresInSeconds )
    {
        this.expiresInSeconds = expiresInSeconds;
    }

    @JsonIgnore
    public LocalDateTime getExpiresOn()
    {
        return expiresOn;
    }

    public void setExpiresOn( LocalDateTime expiresOn )
    {
        this.expiresOn = expiresOn;
    }
}
