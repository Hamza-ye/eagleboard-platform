package com.mass3d.user;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.Sets;
import com.mass3d.cache.Cache;
import com.mass3d.cache.CacheProvider;
import com.mass3d.common.DimensionalObject;
import com.mass3d.commons.util.SystemUtils;
import com.mass3d.setting.SettingKey;
import com.mass3d.setting.SystemSettingManager;
import com.mass3d.system.util.SerializableOptional;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Declare transactions on individual methods. The get-methods do not have transactions declared,
 * instead a programmatic transaction is initiated on cache miss in order to reduce the number of
 * transactions to improve performance.
 */
@Service("com.mass3d.user.UserSettingService")
public class DefaultUserSettingService
    implements UserSettingService {

  private static final Map<String, SettingKey> NAME_SETTING_KEY_MAP = Sets.newHashSet(
      SettingKey.values()).stream().collect(Collectors.toMap(SettingKey::getName, s -> s));
  private final Environment env;

  // -------------------------------------------------------------------------
  // Dependencies
  // -------------------------------------------------------------------------
  private final CacheProvider cacheProvider;
  private final CurrentUserService currentUserService;
  private final UserSettingStore userSettingStore;
  private final UserService userService;
  private final SystemSettingManager systemSettingManager;
  /**
   * Cache for user settings. Does not accept nulls. Disabled during test phase.
   */
  private Cache<SerializableOptional> userSettingCache;

  public DefaultUserSettingService(Environment env, CacheProvider cacheProvider,
      CurrentUserService currentUserService,
      UserSettingStore userSettingStore, UserService userService,
      SystemSettingManager systemSettingManager) {
    checkNotNull(env);
    checkNotNull(cacheProvider);
    checkNotNull(currentUserService);
    checkNotNull(userSettingStore);
    checkNotNull(userService);
    checkNotNull(systemSettingManager);

    this.env = env;
    this.cacheProvider = cacheProvider;
    this.currentUserService = currentUserService;
    this.userSettingStore = userSettingStore;
    this.userService = userService;
    this.systemSettingManager = systemSettingManager;
  }

  // -------------------------------------------------------------------------
  // Initialization
  // -------------------------------------------------------------------------

  @PostConstruct
  public void init() {
    userSettingCache = cacheProvider.newCacheBuilder(SerializableOptional.class)
        .forRegion("userSetting")
        .expireAfterWrite(12, TimeUnit.HOURS)
        .withMaximumSize(SystemUtils.isTestRun(env.getActiveProfiles()) ? 0 : 10000).build();
  }

  // -------------------------------------------------------------------------
  // UserSettingService implementation
  // -------------------------------------------------------------------------

  @Override
  @Transactional
  public void saveUserSetting(UserSettingKey key, Serializable value, String username) {
    UserCredentials credentials = userService.getUserCredentialsByUsername(username);

    if (credentials != null) {
      saveUserSetting(key, value, credentials.getUserInfo());
    }
  }

  @Override
  @Transactional
  public void saveUserSetting(UserSettingKey key, Serializable value) {
    User currentUser = currentUserService.getCurrentUser();

    saveUserSetting(key, value, currentUser);
  }

  @Override
  @Transactional
  public void saveUserSetting(UserSettingKey key, Serializable value, User user) {
    if (user == null) {
      return;
    }

    userSettingCache.invalidate(getCacheKey(key.getName(), user.getUsername()));

    UserSetting userSetting = userSettingStore.getUserSetting(user, key.getName());

    if (userSetting == null) {
      userSetting = new UserSetting(user, key.getName(), value);

      userSettingStore.addUserSetting(userSetting);
    } else {
      userSetting.setValue(value);

      userSettingStore.updateUserSetting(userSetting);
    }
  }

  @Override
  @Transactional
  public void deleteUserSetting(UserSetting userSetting) {
    userSettingCache
        .invalidate(getCacheKey(userSetting.getName(), userSetting.getUser().getUsername()));

    userSettingStore.deleteUserSetting(userSetting);
  }

  @Override
  @Transactional
  public void deleteUserSetting(UserSettingKey key) {
    User currentUser = currentUserService.getCurrentUser();

    if (currentUser != null) {
      UserSetting setting = userSettingStore.getUserSetting(currentUser, key.getName());

      if (setting != null) {
        deleteUserSetting(setting);
      }
    }
  }

  @Override
  @Transactional
  public void deleteUserSetting(UserSettingKey key, User user) {
    UserSetting setting = userSettingStore.getUserSetting(user, key.getName());

    if (setting != null) {
      deleteUserSetting(setting);
    }
  }

  /**
   * Note: No transaction for this method, transaction is instead initiated at the store level
   * behind the cache to avoid the transaction overhead for cache hits.
   */
  @Override
  public Serializable getUserSetting(UserSettingKey key) {
    return getUserSetting(key, Optional.empty()).get();
  }

  /**
   * Note: No transaction for this method, transaction is instead initiated at the store level
   * behind the cache to avoid the transaction overhead for cache hits.
   */
  @Override
  public Serializable getUserSetting(UserSettingKey key, User user) {
    return getUserSetting(key, Optional.ofNullable(user)).get();
  }

  @Override
  @Transactional
  public List<UserSetting> getAllUserSettings() {
    User currentUser = currentUserService.getCurrentUser();

    return getUserSettings(currentUser);
  }

  @Override
  public Map<String, Serializable> getUserSettingsWithFallbackByUserAsMap(User user,
      Set<UserSettingKey> userSettingKeys,
      boolean useFallback) {
    Map<String, Serializable> result = Sets.newHashSet(getUserSettings(user)).stream()
        .filter(userSetting -> userSetting != null && userSetting.getName() != null
            && userSetting.getValue() != null)
        .collect(Collectors.toMap(UserSetting::getName, UserSetting::getValue));

    userSettingKeys.forEach(userSettingKey -> {
      if (!result.containsKey(userSettingKey.getName())) {
        Optional<SettingKey> systemSettingKey = SettingKey.getByName(userSettingKey.getName());

        if (useFallback && systemSettingKey.isPresent()) {
          result.put(userSettingKey.getName(),
              systemSettingManager.getSystemSetting(systemSettingKey.get()));
        } else {
          result.put(userSettingKey.getName(), null);
        }
      }
    });

    return result;
  }

  @Override
  @Transactional
  public List<UserSetting> getUserSettings(User user) {
    if (user == null) {
      return new ArrayList<>();
    }

    List<UserSetting> userSettings = userSettingStore.getAllUserSettings(user);
    Set<UserSetting> defaultUserSettings = UserSettingKey.getDefaultUserSettings(user);

    userSettings.addAll(
        defaultUserSettings.stream().filter(x -> !userSettings.contains(x)).collect(Collectors
            .toList()));

    return userSettings;
  }

  @Override
  public void invalidateCache() {
    userSettingCache.invalidateAll();
  }

  @Override
  public Map<String, Serializable> getUserSettingsAsMap() {
    Set<UserSettingKey> userSettingKeys = Stream
        .of(UserSettingKey.values()).collect(Collectors.toSet());

    return getUserSettingsWithFallbackByUserAsMap(currentUserService.getCurrentUser(),
        userSettingKeys, false);
  }

  // -------------------------------------------------------------------------
  // Private methods
  // -------------------------------------------------------------------------

  /**
   * Returns a user setting optional. If the user settings does not have a value or default value, a
   * corresponding system setting will be looked up.
   *
   * @param key the user setting key.
   * @param user an optional {@link User}.
   * @return an optional user setting value.
   */
  private SerializableOptional getUserSetting(UserSettingKey key, Optional<User> user) {
    if (key == null) {
      return SerializableOptional.empty();
    }

    String username =
        user.isPresent() ? user.get().getUsername() : currentUserService.getCurrentUsername();

    String cacheKey = getCacheKey(key.getName(), username);

    SerializableOptional result = userSettingCache
        .get(cacheKey, c -> getUserSettingOptional(key, username)).get();

    if (!result.isPresent() && NAME_SETTING_KEY_MAP.containsKey(key.getName())) {
      return SerializableOptional.of(
          systemSettingManager.getSystemSetting(NAME_SETTING_KEY_MAP.get(key.getName())));
    } else {
      return result;
    }
  }

  /**
   * Get user setting optional. If the user setting exists and has a value, the value is returned.
   * If not, the default value for the key is returned, if not present, an empty optional is
   * returned. The return object is never null in order to cache requests for system settings which
   * have no value or default value.
   *
   * @param key the user setting key.
   * @param username the username of the user.
   * @return an optional user setting value.
   */
  private SerializableOptional getUserSettingOptional(UserSettingKey key, String username) {
    UserCredentials userCredentials = userService.getUserCredentialsByUsername(username);

    if (userCredentials == null) {
      return SerializableOptional.empty();
    }

    UserSetting setting = userSettingStore
        .getUserSettingTx(userCredentials.getUserInfo(), key.getName());

    Serializable value =
        setting != null && setting.hasValue() ? setting.getValue() : key.getDefaultValue();

    return SerializableOptional.of(value);
  }

  /**
   * Returns the cache key for the given setting name and username.
   *
   * @param settingName the setting name.
   * @param username the username.
   * @return the cache key.
   */
  private String getCacheKey(String settingName, String username) {
    return settingName + DimensionalObject.ITEM_SEP + username;
  }
}
