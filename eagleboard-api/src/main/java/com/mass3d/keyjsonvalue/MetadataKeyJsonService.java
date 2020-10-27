package com.mass3d.keyjsonvalue;

import java.util.List;

public interface MetadataKeyJsonService
{
    /**
     * Retrieves a KeyJsonValue based on a namespace and key.
     *
     * @param key       the key referencing the value.
     * @return the KeyJsonValue matching the key and namespace.
     */
    KeyJsonValue getMetaDataVersion(String key);

    /**
     * Deletes a keyJsonValue.
     *
     * @param keyJsonValue the KeyJsonValue to be deleted.
     */
    void deleteMetaDataKeyJsonValue(KeyJsonValue keyJsonValue);

    /**
     * Adds a new KeyJsonValue.
     *
     * @param keyJsonValue the KeyJsonValue to be stored.
     * @return the id of the KeyJsonValue stored.
     */
    long addMetaDataKeyJsonValue(KeyJsonValue keyJsonValue);

    List<String> getAllVersions();
}