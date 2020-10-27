package com.mass3d.dxf2.metadata.sync.exception;

public class MetadataSyncServiceException
    extends RuntimeException
{
    public MetadataSyncServiceException( String message )
    {
        super( message );
    }

    public MetadataSyncServiceException( Throwable cause )
    {
        super( cause );
    }

    public MetadataSyncServiceException( String message, Throwable cause )
    {
        super( message, cause );
    }
}
