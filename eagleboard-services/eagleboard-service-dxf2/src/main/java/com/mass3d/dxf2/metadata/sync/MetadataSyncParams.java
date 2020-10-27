package com.mass3d.dxf2.metadata.sync;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.mass3d.dxf2.metadata.MetadataImportParams;
import com.mass3d.metadata.version.MetadataVersion;

/**
 * Defines the structure of metadata sync params
 *
 */
public class MetadataSyncParams
{
    private MetadataImportParams importParams;

    private MetadataVersion version;

    private Map<String, List<String>> parameters = new HashMap<String, List<String>>();

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public MetadataSyncParams()
    {
    }

    public MetadataSyncParams( MetadataImportParams importParams, MetadataVersion version )
    {
        this.importParams = importParams;
        this.version = version;
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    public MetadataImportParams getImportParams()
    {
        return importParams;
    }

    public void setImportParams( MetadataImportParams importParams )
    {
        this.importParams = importParams;
    }

    public MetadataVersion getVersion()
    {
        return version;
    }

    public void setVersion( MetadataVersion version )
    {
        this.version = version;
    }

    public Map<String, List<String>> getParameters()
    {
        return parameters;
    }

    public void setParameters( Map<String, List<String>> parameters )
    {
        this.parameters = parameters;
    }
}
