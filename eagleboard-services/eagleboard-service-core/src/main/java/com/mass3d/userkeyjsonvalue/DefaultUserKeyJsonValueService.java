package com.mass3d.userkeyjsonvalue;

import static com.google.common.base.Preconditions.checkNotNull;

import com.mass3d.user.User;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("com.mass3d.userkeyjsonvalue.UserKeyJsonValueService")
public class DefaultUserKeyJsonValueService
    implements UserKeyJsonValueService {

  private final UserKeyJsonValueStore userKeyJsonValueStore;

  public DefaultUserKeyJsonValueService(UserKeyJsonValueStore userKeyJsonValueStore) {
    checkNotNull(userKeyJsonValueStore);
    this.userKeyJsonValueStore = userKeyJsonValueStore;
  }

  @Override
  @Transactional(readOnly = true)
  public UserKeyJsonValue getUserKeyJsonValue(User user, String namespace, String key) {
    return userKeyJsonValueStore.getUserKeyJsonValue(user, namespace, key);
  }

  @Override
  @Transactional
  public long addUserKeyJsonValue(UserKeyJsonValue userKeyJsonValue) {
    userKeyJsonValueStore.save(userKeyJsonValue);
    return userKeyJsonValue.getId();
  }

  @Override
  @Transactional
  public void updateUserKeyJsonValue(UserKeyJsonValue userKeyJsonValue) {
    userKeyJsonValueStore.update(userKeyJsonValue);
  }

  @Override
  @Transactional
  public void deleteUserKeyJsonValue(UserKeyJsonValue userKeyJsonValue) {
    userKeyJsonValueStore.delete(userKeyJsonValue);
  }

  @Override
  @Transactional(readOnly = true)
  public List<String> getNamespacesByUser(User user) {
    return userKeyJsonValueStore.getNamespacesByUser(user);
  }

  @Override
  @Transactional(readOnly = true)
  public List<String> getKeysByUserAndNamespace(User user, String namespace) {
    return userKeyJsonValueStore.getKeysByUserAndNamespace(user, namespace);
  }

  @Override
  @Transactional
  public void deleteNamespaceFromUser(User user, String namespace) {
    userKeyJsonValueStore.getUserKeyJsonValueByUserAndNamespace(user, namespace).forEach(
        userKeyJsonValueStore::delete);
  }
}
