package com.mass3d.user;

import static com.google.common.base.Preconditions.checkNotNull;

import com.mass3d.common.GenericStore;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("com.mass3d.user.UserAccessService")
public class DefaultUserAccessService implements UserAccessService {
  // -------------------------------------------------------------------------
  // Dependencies
  // -------------------------------------------------------------------------

  private GenericStore<UserAccess> userAccessStore;

  public DefaultUserAccessService(
      @Qualifier("com.mass3d.user.UserAccessStore") GenericStore<UserAccess> userAccessStore) {

    checkNotNull(userAccessStore);

    this.userAccessStore = userAccessStore;
  }

  // -------------------------------------------------------------------------
  // UserGroupAccess
  // -------------------------------------------------------------------------

  @Override
  @Transactional
  public void addUserAccess(UserAccess userAccess) {
    userAccessStore.save(userAccess);
  }

  @Override
  @Transactional
  public void updateUserAccess(UserAccess userAccess) {
    userAccessStore.update(userAccess);
  }

  @Override
  @Transactional
  public void deleteUserAccess(UserAccess userAccess) {
    userAccessStore.delete(userAccess);
  }

  @Override
  @Transactional(readOnly = true)
  public List<UserAccess> getAllUserAccesses() {
    return userAccessStore.getAll();
  }
}
