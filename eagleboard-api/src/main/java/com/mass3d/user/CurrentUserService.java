package com.mass3d.user;

/**
 * This interface defined methods for getting access to the currently logged in
 * user and clearing the logged in state. If no user is logged in or the auto
 * access admin is active, all user access methods will return null.
 *
 * @version $Id: CurrentUserService.java 5708 2008-09-16 14:28:32Z larshelg $
 */
public interface CurrentUserService
{
    String ID = CurrentUserService.class.getName();

    /**
     * @return the username of the currently logged in user. If no user is
     *          logged in or the auto access admin is active, null is returned.
     */
    String getCurrentUsername();

    /**
     * @return the currently logged in user. If no user is logged in or the auto
     *          access admin is active, null is returned.
     */
    User getCurrentUser();

    /**
     * @return the user info for the currently logged in user. If no user is
     *          logged in or the auto access admin is active, null is returned.
     */
    UserInfo getCurrentUserInfo();

//    /**
//     * @return the data capture organisation units of the current user, empty set
//     *          if no current user.
//     */
//    Set<OrganisationUnit> getCurrentUserOrganisationUnits();

    /**
     * @return true if the current logged in user has the ALL privileges set, false
     *          otherwise.
     */
    boolean currentUserIsSuper();

    /**
     * Indicates whether the current user has been granted the given authority.
     */
    boolean currentUserIsAuthorized(String auth);

    /**
     * Return UserCredentials of current User
     *
     * @return UserCredentials of current User
     */
    UserCredentials getCurrentUserCredentials();
}
