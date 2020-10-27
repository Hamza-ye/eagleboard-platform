package com.mass3d.dxf2.metadata.sync.exception;

public class RemoteServerUnavailableException
    extends RuntimeException
{
    public RemoteServerUnavailableException( String message )
    {
        super( message );
    }

    public RemoteServerUnavailableException( Throwable cause )
    {
        super( cause );
    }

    public RemoteServerUnavailableException( String message, Throwable cause )
    {
        super( message, cause );
    }
}
