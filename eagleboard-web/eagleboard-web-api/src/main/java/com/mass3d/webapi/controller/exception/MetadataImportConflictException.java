package com.mass3d.webapi.controller.exception;

import com.mass3d.dxf2.metadata.sync.MetadataSyncSummary;

/**
 * This exception can be used for handling Metadata Import related conflict exceptions
 * to return 409.
 */
public class MetadataImportConflictException
    extends Exception
{
    private MetadataSyncSummary metadataSyncSummary = null;

    public MetadataImportConflictException( MetadataSyncSummary metadataSyncSummary )
    {
        this.metadataSyncSummary = metadataSyncSummary;
    }

    public MetadataImportConflictException( String message )
    {
        super( message );
    }

    public MetadataImportConflictException( Throwable cause )
    {
        super( cause );
    }

    public MetadataImportConflictException( String message, Throwable cause )
    {
        super( message, cause );
    }

    public MetadataSyncSummary getMetadataSyncSummary()
    {
        return metadataSyncSummary;
    }
}
