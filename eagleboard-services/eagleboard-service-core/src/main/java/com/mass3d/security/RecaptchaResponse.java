package com.mass3d.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

public class RecaptchaResponse
{
    @JsonProperty( value = "success" )
    private Boolean success;
    
    @JsonProperty( value = "challenge_ts" )
    private String challengeTs;
    
    @JsonProperty( value = "hostname" )
    private String hostname;
    
    @JsonProperty( value = "error-codes" )
    private List<String> errorCodes = new ArrayList<>();

    @JsonIgnore
    public boolean success()
    {
        return success != null && success;
    }
    
    public Boolean getSuccess()
    {
        return success;
    }

    public void setSuccess( Boolean success )
    {
        this.success = success;
    }

    public String getChallengeTs()
    {
        return challengeTs;
    }

    public void setChallengeTs( String challengeTs )
    {
        this.challengeTs = challengeTs;
    }

    public String getHostname()
    {
        return hostname;
    }

    public void setHostname( String hostname )
    {
        this.hostname = hostname;
    }

    public List<String> getErrorCodes()
    {
        return errorCodes;
    }

    public void setErrorCodes( List<String> errorCodes )
    {
        this.errorCodes = errorCodes;
    }
}
