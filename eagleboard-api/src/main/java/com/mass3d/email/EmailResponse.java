package com.mass3d.email;

public enum EmailResponse
{
    SENT( "Email sent successfully" ),
    FAILED( "Failed to send emal" ),
    ABORTED( "Sending email aborted" ),
    NOT_CONFIGURED( "Configuration not found" ),
    HOST_CONFIG_NOT_FOUND( "Host configuration not found" );

    private String responseMessage;

    EmailResponse( String responseMessage )
    {
        this.responseMessage = responseMessage;
    }

    public String getResponseMessage()
    {
        return responseMessage;
    }

    public void setResponseMessage( String responseMessage )
    {
        this.responseMessage = responseMessage;
    }
}
