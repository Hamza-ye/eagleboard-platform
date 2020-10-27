package com.mass3d.user;

import static com.google.common.base.Preconditions.checkNotNull;

import com.mass3d.system.deletion.DeletionHandler;
import org.springframework.stereotype.Component;

@Component("com.mass3d.user.UserAuthorityGroupDeletionHandler")
public class UserAuthorityGroupDeletionHandler
    extends DeletionHandler {
  // -------------------------------------------------------------------------
  // Dependencies
  // -------------------------------------------------------------------------

  private final UserService userService;

  public UserAuthorityGroupDeletionHandler(UserService userService) {
    checkNotNull(userService);

    this.userService = userService;
  }

// -------------------------------------------------------------------------
  // DeletionHandler implementation
  // -------------------------------------------------------------------------

  @Override
  public String getClassName() {
    return UserAuthorityGroup.class.getSimpleName();
  }

  @Override
  public void deleteUser(User user) {
    UserCredentials credentials = user.getUserCredentials();

    for (UserAuthorityGroup group : credentials.getUserAuthorityGroups()) {
      group.getMembers().remove(credentials);
      userService.updateUserAuthorityGroup(group);
    }
  }
}
