package com.mass3d.user;

import java.util.Collection;
import java.util.List;

public interface UserGroupService
{
    String ID = UserGroupService.class.getName();

    long addUserGroup(UserGroup userGroup);

    void updateUserGroup(UserGroup userGroup);

    void deleteUserGroup(UserGroup userGroup);

    UserGroup getUserGroup(long userGroupId);

    UserGroup getUserGroup(String uid);

    /**
     * Indicates whether the current user can add or remove members for the user
     * group with the given UID. To to so the current user must have write access
     * to the group or have read access as well as the F_USER_GROUPS_READ_ONLY_ADD_MEMBERS
     * authority.
     *
     * @param uid the user group UID.
     * @return true if the current user can add or remove members of the user group.
     */
    boolean canAddOrRemoveMember(String uid);

    boolean canAddOrRemoveMember(String uid, User currentUser);

    void addUserToGroups(User user, Collection<String> uids);

    void addUserToGroups(User user, Collection<String> uids, User currentUser);

    void removeUserFromGroups(User user, Collection<String> uids);

    void updateUserGroups(User user, Collection<String> uids);

    void updateUserGroups(User user, Collection<String> uids, User currentUser);

    List<UserGroup> getAllUserGroups();

    List<UserGroup> getUserGroupByName(String name);

    List<UserGroup> getUserGroupsBetween(int first, int max);

    List<UserGroup> getUserGroupsBetweenByName(String name, int first, int max);

    int getUserGroupCount();

    int getUserGroupCountByName(String name);
}
