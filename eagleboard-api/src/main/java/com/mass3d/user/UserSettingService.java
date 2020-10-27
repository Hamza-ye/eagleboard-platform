package com.mass3d.user;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The main interface for working with user settings. Implementation need to get
 * the current user from {@link CurrentUserService}.
 *
 */
public interface UserSettingService
{
    String ID = UserSettingService.class.getName();

    // -------------------------------------------------------------------------
    // UserSettings
    // -------------------------------------------------------------------------

    /**
     * Saves the key/value pair as a user setting connected to the currently
     * logged in user.
     *
     * @param key the user setting key.
     * @param value the setting value.
     */
    void saveUserSetting(UserSettingKey key, Serializable value);

    /**
     * Saves the key/value pair as a user setting connected to user identified
     * by username.
     *
     * @param key the user setting key.
     * @param value the setting value.
     * @param username the username of user.
     */
    void saveUserSetting(UserSettingKey key, Serializable value, String username);

    /**
     * Saves the name/value pair as a user setting connected to user.
     *
     * @param key the user setting key.
     * @param value the setting value.
     * @param user the user.
     */
    void saveUserSetting(UserSettingKey key, Serializable value, User user);

    /**
     * Deletes a UserSetting.
     *
     * @param userSetting the UserSetting to delete.
     */
    void deleteUserSetting(UserSetting userSetting);

    /**
     * Deletes the user setting with the given name.
     *
     * @param key the user setting key.
     */
    void deleteUserSetting(UserSettingKey key);

    /**
     * Deletes the user setting with the given name for the given user.
     *
     * @param key the user setting key.
     * @param user the user.
     */
    void deleteUserSetting(UserSettingKey key, User user);

    /**
     * Returns the value of the user setting specified by the given name.
     *
     * @param key the user setting key.
     * @return the value corresponding to the named user setting, or null if
     *         there is no match.
     */
    Serializable getUserSetting(UserSettingKey key);

    /**
     * Returns the value of the user setting specified by the given name.
     *
     * @param key the user setting key.
     * @param user the user.
     * @return the value corresponding to the named user setting, or null if
     *         there is no match.
     */
    Serializable getUserSetting(UserSettingKey key, User user);

    /**
     * Retrieves UserSettings for the given User.
     *
     * @param user the User.
     * @return a List of UserSettings.
     */
    List<UserSetting> getUserSettings(User user);

    /**
     * Returns all user settings belonging to the current user.
     *
     * @return all user settings belonging to the current user.
     */
    List<UserSetting> getAllUserSettings();

    /**
     * Returns all specified user settings. If any user settings have not been set,
     * system settings will be used as a fallback.
     * @param userSettingKeys the set of user settings to retrieve
     * @return a map of setting names and their values
     */
    Map<String, Serializable> getUserSettingsWithFallbackByUserAsMap(User user,
        Set<UserSettingKey> userSettingKeys, boolean useFallback);

    /**
     * Invalidates in-memory caches.
     */
    void invalidateCache();

    /**
     * Returns all user settings for currently logged in user. Setting will not be
     * included in map if its value is null.
     * @return a map of setting names and their values
     */
    Map<String, Serializable> getUserSettingsAsMap();
}
