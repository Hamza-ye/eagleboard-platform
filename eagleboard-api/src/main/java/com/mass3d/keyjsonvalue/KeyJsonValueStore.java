package com.mass3d.keyjsonvalue;

import java.util.Date;
import java.util.List;
import com.mass3d.common.IdentifiableObjectStore;

public interface KeyJsonValueStore
    extends IdentifiableObjectStore<KeyJsonValue>
{
    /**
     * Retrieves a list of all namespaces
     * @return a list of strings representing each existing namespace
     */
    List<String> getNamespaces();

    /**
     * Retrieves a list of keys associated with a given namespace.
     * @param namespace the namespace to retrieve keys from
     * @return a list of strings representing the different keys in the namespace
     */
    List<String> getKeysInNamespace(String namespace);

    /**
     * Retrieves a list of keys associated with a given namespace which are updated after lastUpdated time.
     * @param namespace the namespace to retrieve keys from
     * @param lastUpdated the lastUpdated time to retrieve keys from
     * @return a list of strings representing the different keys in the namespace
     */
    List<String> getKeysInNamespace(String namespace, Date lastUpdated);

    /**
     * Retrieves a list of KeyJsonValue objects based on a given namespace
     * @param namespace the namespace to retrieve KeyJsonValues from
     * @return a List of KeyJsonValues
     */
    List<KeyJsonValue> getKeyJsonValueByNamespace(String namespace);

    /**
     * Retrieves a KeyJsonValue based on the associated key and namespace
     * @param namespace the namespace where the key is stored
     * @param key the key referencing the value
     * @return the KeyJsonValue retrieved
     */
    KeyJsonValue getKeyJsonValue(String namespace, String key);
}
