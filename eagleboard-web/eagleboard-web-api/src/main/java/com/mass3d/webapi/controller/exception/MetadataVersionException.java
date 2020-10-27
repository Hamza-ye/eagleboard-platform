package com.mass3d.webapi.controller.exception;

public class MetadataVersionException
    extends Exception
{
    public MetadataVersionException( String message )
    {
        super( message );
    }

    public MetadataVersionException( Throwable cause )
    {
        super( cause );
    }

    public MetadataVersionException( String message, Throwable cause )
    {
        super( message, cause );
    }
}
