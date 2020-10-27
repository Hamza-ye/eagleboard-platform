package com.mass3d.user;

import static com.google.common.base.Preconditions.checkNotNull;

import com.mass3d.common.IdentifiableObjectManager;
import com.mass3d.system.deletion.DeletionHandler;
import java.util.Set;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component("com.mass3d.user.UserGroupDeletionHandler")
public class UserGroupDeletionHandler
    extends DeletionHandler {
  // -------------------------------------------------------------------------
  // Dependencies
  // -------------------------------------------------------------------------

  private final IdentifiableObjectManager idObjectManager;

  private final JdbcTemplate jdbcTemplate;

  public UserGroupDeletionHandler(IdentifiableObjectManager idObjectManager,
      JdbcTemplate jdbcTemplate) {
    checkNotNull(idObjectManager);
    checkNotNull(jdbcTemplate);

    this.idObjectManager = idObjectManager;
    this.jdbcTemplate = jdbcTemplate;
  }

  // -------------------------------------------------------------------------
  // DeletionHandler implementation
  // -------------------------------------------------------------------------

  @Override
  protected String getClassName() {
    return UserGroup.class.getSimpleName();
  }

  @Override
  public void deleteUser(User user) {
    Set<UserGroup> userGroups = user.getGroups();

    for (UserGroup group : userGroups) {
      group.getMembers().remove(user);
      idObjectManager.updateNoAcl(group);
    }
  }

  @Override
  public String allowDeleteUserGroup(UserGroup group) {
    int count = jdbcTemplate
        .queryForObject("select count(*) from usergroupaccess where usergroupid=" + group.getId(),
            Integer.class);

    return count == 0 ? null : "";
  }

  @Override
  public void deleteUserGroup(UserGroup userGroup) {
    Set<UserGroup> userGroups = userGroup.getManagedByGroups();

    for (UserGroup group : userGroups) {
      group.getManagedGroups().remove(userGroup);
      idObjectManager.updateNoAcl(group);
    }
  }
}
