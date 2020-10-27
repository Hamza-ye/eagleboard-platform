package com.mass3d.dxf2.metadata.sync;

import java.util.List;
import java.util.Map;
import com.mass3d.dxf2.metadata.sync.exception.DhisVersionMismatchException;

/**
 * MetadataSyncService defines the methods available for initiating sync related methods
 *
 */
public interface MetadataSyncService
{
    /**
     * Gets the MetadataSyncParams from the map of parameters in the incoming request.
     *
     * @param parameters
     * @return MetadataSyncParams
     */
    MetadataSyncParams getParamsFromMap(Map<String, List<String>> parameters);

    /**
     * Checks whether metadata sync needs to be be done or not.
     * If version already exists in system it does do the sync
     * @param syncParams
     * @return
     */
    public boolean isSyncRequired(MetadataSyncParams syncParams);
    /**
     * Does the actual metadata sync logic. Calls the underlying importer to import the relevant
     * MetadataVersion snapshot downloaded from the remote server.
     *
     * @param syncParams
     * @return
     */
    MetadataSyncSummary doMetadataSync(MetadataSyncParams syncParams) throws DhisVersionMismatchException;
}
