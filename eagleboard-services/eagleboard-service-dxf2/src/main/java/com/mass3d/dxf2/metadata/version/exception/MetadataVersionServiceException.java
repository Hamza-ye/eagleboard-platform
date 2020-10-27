package com.mass3d.dxf2.metadata.version.exception;

/**
 * Service Exception for metadata version
 *
 */
public class MetadataVersionServiceException
    extends RuntimeException
{
    public MetadataVersionServiceException( String message )
    {
        super( message );
    }

    public MetadataVersionServiceException( Throwable cause )
    {
        super( cause );
    }

    public MetadataVersionServiceException( String message, Throwable cause )
    {
        super( message, cause );
    }
}
