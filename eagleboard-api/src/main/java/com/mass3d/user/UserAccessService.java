package com.mass3d.user;

import java.util.List;

public interface UserAccessService
{
    String ID = UserAccess.class.getName();

    void addUserAccess(UserAccess userAccess);

    void updateUserAccess(UserAccess userAccess);

    void deleteUserAccess(UserAccess userAccess);

    List<UserAccess> getAllUserAccesses();
}
