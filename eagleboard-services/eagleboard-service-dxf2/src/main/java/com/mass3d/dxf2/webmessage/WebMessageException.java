package com.mass3d.dxf2.webmessage;

public class WebMessageException extends Exception
{
    private WebMessage webMessage;

    public WebMessageException( WebMessage webMessage )
    {
        this.webMessage = webMessage;
    }

    public WebMessage getWebMessage()
    {
        return webMessage;
    }
}
