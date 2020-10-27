package com.mass3d.user;

import static com.google.common.base.Preconditions.checkNotNull;

import com.mass3d.system.deletion.DeletionHandler;
import java.util.Iterator;
import org.springframework.stereotype.Component;

@Component("com.mass3d.user.UserSettingDeletionHandler")
public class UserSettingDeletionHandler
    extends DeletionHandler {
  // -------------------------------------------------------------------------
  // Dependencies
  // -------------------------------------------------------------------------

  private final UserSettingService userSettingService;

  public UserSettingDeletionHandler(UserSettingService userSettingService) {
    checkNotNull(userSettingService);

    this.userSettingService = userSettingService;
  }

  // -------------------------------------------------------------------------
  // DeletionHandler implementation
  // -------------------------------------------------------------------------

  @Override
  public String getClassName() {
    return User.class.getSimpleName();
  }

  @Override
  public void deleteUser(User user) {
    Iterator<UserSetting> settings = userSettingService.getUserSettings(user).iterator();

    while (settings.hasNext()) {
      UserSetting setting = settings.next();
      settings.remove();
      userSettingService.deleteUserSetting(setting);
    }
  }
}
