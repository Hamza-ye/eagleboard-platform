package com.mass3d.user;

import static com.google.common.base.Preconditions.checkNotNull;

import com.mass3d.common.GenericStore;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("com.mass3d.user.UserGroupAccessService")
public class DefaultUserGroupAccessService implements UserGroupAccessService {
  // -------------------------------------------------------------------------
  // Dependencies
  // -------------------------------------------------------------------------

  private GenericStore<UserGroupAccess> userGroupAccessStore;

  public DefaultUserGroupAccessService(
      @Qualifier("com.mass3d.user.UserGroupAccessStore") GenericStore<UserGroupAccess> userGroupAccessStore) {
    checkNotNull(userGroupAccessStore);

    this.userGroupAccessStore = userGroupAccessStore;
  }

  // -------------------------------------------------------------------------
  // UserGroupAccess
  // -------------------------------------------------------------------------

  @Override
  @Transactional
  public void addUserGroupAccess(UserGroupAccess userGroupAccess) {
    userGroupAccessStore.save(userGroupAccess);
  }

  @Override
  @Transactional
  public void updateUserGroupAccess(UserGroupAccess userGroupAccess) {
    userGroupAccessStore.update(userGroupAccess);
  }

  @Override
  @Transactional
  public void deleteUserGroupAccess(UserGroupAccess userGroupAccess) {
    userGroupAccessStore.delete(userGroupAccess);
  }

  @Override
  @Transactional(readOnly = true)
  public List<UserGroupAccess> getAllUserGroupAccesses() {
    return userGroupAccessStore.getAll();
  }
}
