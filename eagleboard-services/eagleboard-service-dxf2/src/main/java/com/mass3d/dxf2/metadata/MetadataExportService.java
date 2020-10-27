package com.mass3d.dxf2.metadata;

import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nonnull;
import com.mass3d.common.IdentifiableObject;
import com.mass3d.node.types.RootNode;

public interface MetadataExportService
{
    /**
     * Exports metadata using provided params.
     *
     * @param params Export parameters
     * @return Map of all exported objects
     */
    Map<Class<? extends IdentifiableObject>, List<? extends IdentifiableObject>> getMetadata(
        MetadataExportParams params);

    /**
     * Returns same result as getMetadata, but metadata is returned as Node objects instead.
     *
     * @param params Export parameters
     * @return RootNode instance with children containing all exported objects
     */
    RootNode getMetadataAsNode(MetadataExportParams params);

    /**
     * Validates the import params. Not currently implemented.
     *
     * @param params Export parameters to validate
     */
    void validate(MetadataExportParams params);

    /**
     * Parses, and creates a MetadataExportParams instance based on given map of parameters.
     *
     * @param parameters Key-Value map of wanted parameters
     * @return MetadataExportParams instance created based on input parameters
     */
    MetadataExportParams getParamsFromMap(Map<String, List<String>> parameters);

    /**
     * Exports an object including a set of selected dependencies. Only a subset of the
     * specified export parameters are used for the metadata with dependencies export.
     *
     * @param object Object to export including dependencies
     * @return Original object + selected set of dependencies
     */
    Map<Class<? extends IdentifiableObject>, Set<IdentifiableObject>> getMetadataWithDependencies(
        IdentifiableObject object);

    /**
     * Exports an object including a set of selected dependencies as RootNode. Only a subset
     * of the specified export parameters are used for the metadata with dependencies export.
     *
     * @param object Object to export including dependencies
     * @param params Parameters that affect the export.
     * @return Original object + selected set of dependencies, exported as RootNode
     */
    RootNode getMetadataWithDependenciesAsNode(IdentifiableObject object,
        @Nonnull MetadataExportParams params);


}
