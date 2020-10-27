package com.mass3d.user;

import static com.google.common.base.Preconditions.checkNotNull;

import com.mass3d.system.deletion.DeletionHandler;
import org.springframework.stereotype.Component;

@Component("com.mass3d.user.UserCredentialsDeletionHandler")
public class UserCredentialsDeletionHandler
    extends DeletionHandler {

  private final UserCredentialsStore userCredentialsStore;

  public UserCredentialsDeletionHandler(UserCredentialsStore userCredentialsStore) {
    checkNotNull(userCredentialsStore);
    this.userCredentialsStore = userCredentialsStore;
  }

  // -------------------------------------------------------------------------
  // DeletionHandler implementation
  // -------------------------------------------------------------------------

  @Override
  public String getClassName() {
    return UserCredentials.class.getSimpleName();
  }

  @Override
  public void deleteUser(User user) {
    userCredentialsStore.delete(user.getUserCredentials());
  }
}
