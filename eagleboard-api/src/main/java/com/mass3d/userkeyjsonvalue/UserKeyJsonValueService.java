package com.mass3d.userkeyjsonvalue;

import java.util.List;
import com.mass3d.user.User;

public interface UserKeyJsonValueService {

  /**
   * Retrieves a KeyJsonValue based on a user and key
   *
   * @param user the user where the key is associated
   * @param namespace the namespace associated with the key
   * @param key the key referencing the value  @return the UserKeyJsonValue matching the key and
   * namespace
   */
  UserKeyJsonValue getUserKeyJsonValue(User user, String namespace, String key);

  /**
   * Adds a new UserKeyJsonValue
   *
   * @param userKeyJsonValue the UserKeyJsonValue to be stored
   * @return the id of the UserKeyJsonValue stored
   */
  long addUserKeyJsonValue(UserKeyJsonValue userKeyJsonValue);

  /**
   * Updates a UserKeyJsonValue
   *
   * @param userKeyJsonValue the updated UserKeyJsonValue
   */
  void updateUserKeyJsonValue(UserKeyJsonValue userKeyJsonValue);

  /**
   * Deletes a UserKeyJsonValue
   *
   * @param userKeyJsonValue the UserKeyJsonValue to be deleted.
   */
  void deleteUserKeyJsonValue(UserKeyJsonValue userKeyJsonValue);

  /**
   * Returns a list of namespaces connected to the given user
   *
   * @param user the user connected to the namespaces
   * @return List of strings representing namespaces or an empty list if no namespaces are found
   */
  List<String> getNamespacesByUser(User user);

  /**
   * Returns a list of keys in the given namespace connected to the given user
   *
   * @param user connected to keys
   * @param namespace to fetch keys from
   * @return a list of keys or an empty list if no keys are found
   */
  List<String> getKeysByUserAndNamespace(User user, String namespace);


  /**
   * Deletes all keys associated with a given user and namespace
   *
   * @param user the user associated with namespace to delete
   * @param namespace the namespace to delete
   */
  void deleteNamespaceFromUser(User user, String namespace);
}
