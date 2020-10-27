package com.mass3d.user;

import java.util.UUID;
import com.mass3d.common.GenericStore;

public interface UserCredentialsStore
    extends GenericStore<UserCredentials>
{
    String ID = UserCredentialsStore.class.getName();

    /**
     * Retrieves the UserCredentials associated with the User with the given
     * name.
     *
     * @param username the name of the User.
     * @return the UserCredentials.
     */
    UserCredentials getUserCredentialsByUsername(String username);

    /**
     * Retrieves the UserCredentials associated with the User with the given
     * open ID.
     *
     * @param openId open ID.
     * @return the UserCredentials.
     */
    UserCredentials getUserCredentialsByOpenId(String openId);

    /**
     * Retrieves the UserCredentials associated with the User with the given
     * LDAP ID.
     *
     * @param ldapId LDAP ID.
     * @return the UserCredentials.
     */
    UserCredentials getUserCredentialsByLdapId(String ldapId);

    /**
     * Retrieves the UserCredentials associated with the User with the given
     * UUID.
     *
     * @param uuid uid.
     * @return the UserCredentials.
     */
    UserCredentials getUserCredentialsByUuid(UUID uuid);
}