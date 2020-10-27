package com.mass3d.user;

import java.util.List;
import javax.annotation.Nullable;
import com.mass3d.common.IdentifiableObjectStore;

public interface UserStore
    extends IdentifiableObjectStore<User>
{
    String ID = UserStore.class.getName();

    /**
     * Returns a list of users based on the given query parameters.
     *
     *
     * @param params the user query parameters.
     * @return a List of users.
     */
    List<User> getUsers(UserQueryParams params);

    /**
     * Returns a list of users based on the given query parameters.
     * If the specified list of orders are empty, default order of
     * last name and first name will be applied.
     *
     * @param params the user query parameters.
     * @param orders the already validated order strings (e.g. email:asc).
     * @return a List of users.
     */
    List<User> getUsers(UserQueryParams params, @Nullable List<String> orders);

    /**
     * Returns the number of users based on the given query parameters.
     *
     * @param params the user query parameters.
     * @return number of users.
     */
    int getUserCount(UserQueryParams params);

    /**
     * Returns number of all users
     * @return number of users
     */
    int getUserCount();

    List<User> getExpiringUsers(UserQueryParams userQueryParams);

    /**
     * Returns UserCredentials for given username.
     *
     * @param username username for which the UserCredentials will be returned
     * @return UserCredentials for given username or null
     */
    UserCredentials getUserCredentialsByUsername(String username);

    /**
     * Returns User with given userId.
     *
     * @param userId UserId
     * @return User with given userId
     */
    User getUser(long userId);
}
