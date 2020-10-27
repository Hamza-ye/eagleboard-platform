package com.mass3d.userkeyjsonvalue;

import java.util.List;
import com.mass3d.common.IdentifiableObjectStore;
import com.mass3d.user.User;

public interface UserKeyJsonValueStore
    extends IdentifiableObjectStore<UserKeyJsonValue>
{
    /**
     * Retrieves a KeyJsonValue based on the associated key and user
     * @param user the user where the key is stored
     * @param namespace the namespace referencing the value
     * @param key the key referencing the value
     * @return the KeyJsonValue retrieved
     */
    UserKeyJsonValue getUserKeyJsonValue(User user, String namespace, String key);

    /**
     * Retrieves a list of namespaces associated with a user
     * @param user to search namespaces for
     * @return a list of strings representing namespaces
     */
    List<String> getNamespacesByUser(User user);

    /**
     * Retrieves a list of keys associated with a given user and namespace.
     * @param user the user to retrieve keys from
     * @param namespace the namespace to search
     * @return a list of strings representing the different keys stored on the user
     */
    List<String> getKeysByUserAndNamespace(User user, String namespace);

    /**
     * Retrieves all UserKeyJsonvalues from a given user and namespace
     * @param user to search
     * @param namespace to search
     */
    List<UserKeyJsonValue> getUserKeyJsonValueByUserAndNamespace(User user, String namespace);
}
