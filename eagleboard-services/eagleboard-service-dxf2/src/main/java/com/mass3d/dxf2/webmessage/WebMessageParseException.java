package com.mass3d.dxf2.webmessage;

import java.io.IOException;

public class WebMessageParseException
    extends IOException
{
    public WebMessageParseException( String message )
    {
        super( message );
    }

    public WebMessageParseException( Throwable cause )
    {
        super( cause );
    }

    public WebMessageParseException( String message, Throwable cause )
    {
        super( message, cause );
    }

}
