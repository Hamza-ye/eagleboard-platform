package com.mass3d.dxf2.metadata;

import java.util.List;
import java.util.Map;
import com.mass3d.dxf2.metadata.feedback.ImportReport;

public interface MetadataImportService
{
    /**
     * Import object using provided params. Takes the objects through all phases of the importer
     * from preheating to validation, and then finished with a commit (unless its validate only)
     *
     * @param params Parameters for import, including objects
     * @return Report giving status of import (and any errors)
     */
    ImportReport importMetadata(MetadataImportParams params);

    /**
     * Parses, and creates a MetadataImportParams instance based on given map of parameters.
     *
     * @param parameters Key-Value map of wanted parameters
     * @return MetadataImportParams instance created based on input parameters
     */
    MetadataImportParams getParamsFromMap(Map<String, List<String>> parameters);
}
