package com.mass3d.webapi.controller.exception;

public class MetadataSyncException
    extends Exception
{
    public MetadataSyncException( String message )
    {
        super( message );
    }

    public MetadataSyncException( Throwable cause )
    {
        super( cause );
    }

    public MetadataSyncException( String message, Throwable cause )
    {
        super( message, cause );
    }

}
