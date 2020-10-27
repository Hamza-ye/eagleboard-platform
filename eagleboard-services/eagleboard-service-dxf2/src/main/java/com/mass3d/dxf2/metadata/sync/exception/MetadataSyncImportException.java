package com.mass3d.dxf2.metadata.sync.exception;

public class MetadataSyncImportException extends
    RuntimeException
{
    public MetadataSyncImportException( String message )
    {
        super( message );
    }

    public MetadataSyncImportException( Throwable cause )
    {
        super( cause );
    }

    public MetadataSyncImportException( String message, Throwable cause )
    {
        super( message, cause );
    }
}
