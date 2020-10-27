package com.mass3d.user;

import java.util.List;

public interface UserSettingStore
{
    /**
     * Adds a UserSetting.
     *
     * @param userSetting the UserSetting to add.
     */
    void addUserSetting(UserSetting userSetting);

    /**
     * Updates a UserSetting.
     *
     * @param userSetting the UserSetting to update.
     */
    void updateUserSetting(UserSetting userSetting);

    /**
     * Retrieves the UserSetting associated with the given User for the given
     * UserSetting name.
     * <p>
     * Note: This method invocation will occur within a transaction.
     *
     * @param user the User.
     * @param name the name of the UserSetting.
     * @return the UserSetting.
     */
    UserSetting getUserSettingTx(User user, String name);

    /**
     * Retrieves the UserSetting associated with the given User for the given
     * UserSetting name.
     *
     * @param user the User.
     * @param name the name of the UserSetting.
     * @return the UserSetting.
     */
    UserSetting getUserSetting(User user, String name);

    /**
     * Retrieves all UserSettings for the given User.
     *
     * @param user the User.
     * @return a List of UserSettings.
     */
    List<UserSetting> getAllUserSettings(User user);

    /**
     * Deletes a UserSetting.
     *
     * @param userSetting the UserSetting to delete.
     */
    void deleteUserSetting(UserSetting userSetting);

    /**
     * Removes all user settings associated with the given user.
     *
     * @param user the user.
     */
    void removeUserSettings(User user);
}
