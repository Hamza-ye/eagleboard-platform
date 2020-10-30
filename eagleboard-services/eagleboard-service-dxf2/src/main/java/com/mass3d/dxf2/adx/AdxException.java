package com.mass3d.dxf2.adx;

import com.mass3d.dxf2.importsummary.ImportConflict;

/**
 * Simple class for ADX checked exceptions which can wrap an ImportConflict.
 * 
 */
public class AdxException
    extends Exception
{
    protected String object;

    public String getObject()
    {
        return object;
    }

    public AdxException()
    {
    }
    
    public AdxException( String msg )
    {
        super( msg );
    }

    public AdxException( String object, String msg )
    {
        super( msg );
        this.object = object;
    }

    public ImportConflict getImportConflict()
    {
        return new ImportConflict( object, this.getMessage() );
    }
}
