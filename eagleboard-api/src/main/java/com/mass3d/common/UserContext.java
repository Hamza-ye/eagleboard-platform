package com.mass3d.common;

import com.mass3d.user.User;
import com.mass3d.user.UserSetting;
import com.mass3d.user.UserSettingKey;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.util.StringUtils;

public final class UserContext {

  private static final ThreadLocal<User> threadUser = new ThreadLocal<>();

  private static final ThreadLocal<Map<String, Serializable>> threadUserSettings = new ThreadLocal<>();

  public static void reset() {
    threadUser.remove();
    threadUserSettings.remove();
  }

  public static User getUser() {
    return threadUser.get();
  }

  public static void setUser(User user) {
    threadUser.set(user);
  }

  public static String getUsername() {
    User user = getUser();

    return user != null ? user.getUsername() : "system-process";
  }

  public static boolean haveUser() {
    return getUser() != null;
  }

  // TODO Needs synchronized?

  public static void setUserSetting(UserSettingKey key, Serializable value) {
    UserContext.setUserSetting(key.getName(), value);
  }

  public static void setUserSetting(String key, Serializable value) {
    if (threadUserSettings.get() == null) {
      threadUserSettings.set(new HashMap<>());
    }

    if (value != null) {
      threadUserSettings.get().put(key, value);
    } else {
      threadUserSettings.get().remove(key);
    }
  }

  public static Serializable getUserSetting(UserSettingKey key) {
    return threadUserSettings.get() != null ? threadUserSettings.get().get(key.getName()) : null;
  }

  @SuppressWarnings("unchecked")
  public static <T> T getUserSetting(UserSettingKey key, Class<T> klass) {
    return threadUserSettings.get() != null ? (T) threadUserSettings.get().get(key.getName())
        : null;
  }

  public static boolean haveUserSetting(UserSettingKey key) {
    return getUserSetting(key) != null;
  }

  public static void setUserSettings(List<UserSetting> userSettings) {
    userSettings.stream()
        .filter(userSetting -> !StringUtils.isEmpty(userSetting.getName()))
        .forEach(userSetting -> UserContext
            .setUserSetting(userSetting.getName(), userSetting.getValue()));
  }
}
