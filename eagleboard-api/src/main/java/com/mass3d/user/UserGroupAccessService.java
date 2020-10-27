package com.mass3d.user;

import java.util.List;

public interface UserGroupAccessService
{
    String ID = UserGroupAccessService.class.getName();

    void addUserGroupAccess(UserGroupAccess userGroupAccess);

    void updateUserGroupAccess(UserGroupAccess userGroupAccess);

    void deleteUserGroupAccess(UserGroupAccess userGroupAccess);

    List<UserGroupAccess> getAllUserGroupAccesses();
}
