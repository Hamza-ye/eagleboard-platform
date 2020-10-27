package com.mass3d.setting;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface SystemSettingManager
{
    /**
     * Saves the given system setting key and value.
     *
     * @param key the system setting key.
     * @param value the system setting value.
     */
    void saveSystemSetting(SettingKey key, Serializable value);

    /**
     * Saves the translation for given setting key and locale if given setting key is translatable.
     * If the translation string contains an empty string, the translation for given locale and key is removed.
     *
     * @param key SettingKey
     * @param locale locale of the translation
     * @param translation Actual translation
     */
    void saveSystemSettingTranslation(SettingKey key, String locale, String translation);

    /**
     * Deletes the system setting with the given key.
     *
     * @param key the system setting key.
     */
    void deleteSystemSetting(SettingKey key);

    /**
     * Returns the system setting value for the given key. If no value exists, returns
     * the default value held by {@link SettingKey#getDefaultValue()}. If not,
     * returns null.
     *
     * @param key the system setting key.
     * @return the setting value.
     */
    Serializable getSystemSetting(SettingKey key);

    /**
     * Returns the system setting value for the given key. If no value exists, returns
     * the default value as defined by the given default value.
     *
     * @param key the system setting key.
     * @return the setting value.
     */
    Serializable getSystemSetting(SettingKey key, Serializable defaultValue);

    /**
     * Returns the translation for given setting key and locale or empty Optional if no translation is
     * available or setting key is not translatable.
     * @param key SettingKey
     * @param locale Locale of required translation
     * @return The Optional with the actual translation or empty Optional
     */
    Optional<String> getSystemSettingTranslation(SettingKey key, String locale);

    /**
     * Returns all system settings.
     *
     * @return a list of all system settings.
     */
    List<SystemSetting> getAllSystemSettings();

    /**
     * Returns all system settings as a mapping between the setting name and the
     * value. Includes system settings which have a default value but no explicitly
     * set value.
     */
    Map<String, Serializable> getSystemSettingsAsMap();

    /**
     * Returns system settings for the given collection of setting keys as a map,
     * where the key is string representation of the {@link SettingKey}, and the
     * value is the setting value.
     *
     * @param keys the collection of setting keys.
     * @return a map of system settings.
     */
    Map<String, Serializable> getSystemSettings(Collection<SettingKey> keys);

    /**
     * Invalidates the currently cached system settings.
     */
    void invalidateCache();

    // -------------------------------------------------------------------------
    // Specific methods
    // -------------------------------------------------------------------------

    List<String> getFlags();

    String getFlagImage();

    String getEmailHostName();

    int getEmailPort();

    String getEmailUsername();

    boolean getEmailTls();

    String getEmailSender();

    boolean accountRecoveryEnabled();

    boolean selfRegistrationNoRecaptcha();

    boolean emailConfigured();

    boolean systemNotificationEmailValid();

    boolean hideUnapprovedDataInAnalytics();

    boolean isOpenIdConfigured();

    String googleAnalyticsUA();

    Integer credentialsExpires();

    boolean isConfidential(String name);

    boolean isTranslatable(String name);
}
