package com.mass3d.user;

import static com.google.common.base.Preconditions.checkNotNull;

import com.mass3d.common.IdentifiableObjectManager;
import com.mass3d.system.deletion.DeletionHandler;
import org.springframework.stereotype.Component;

@Component("com.mass3d.user.UserDeletionHandler")
public class UserDeletionHandler
    extends DeletionHandler {

  private final IdentifiableObjectManager idObjectManager;

  public UserDeletionHandler(IdentifiableObjectManager idObjectManager) {
    checkNotNull(idObjectManager);

    this.idObjectManager = idObjectManager;
  }

  // -------------------------------------------------------------------------
  // DeletionHandler implementation
  // -------------------------------------------------------------------------

  @Override
  public String getClassName() {
    return User.class.getSimpleName();
  }

  @Override
  public void deleteUserAuthorityGroup(UserAuthorityGroup authorityGroup) {
    for (UserCredentials credentials : authorityGroup.getMembers()) {
      credentials.getUserAuthorityGroups().remove(authorityGroup);
      idObjectManager.updateNoAcl(credentials);
    }
  }

//    @Override
//    public void deleteOrganisationUnit( OrganisationUnit unit )
//    {
//        for ( User user : unit.getUsers() )
//        {
//            user.getTodoTasks().remove( unit );
//            idObjectManager.updateNoAcl( user );
//        }
//    }

  @Override
  public void deleteUserGroup(UserGroup group) {
    for (User user : group.getMembers()) {
      user.getGroups().remove(group);
      idObjectManager.updateNoAcl(user);
    }
  }

  @Override
  public String allowDeleteUserAuthorityGroup(UserAuthorityGroup authorityGroup) {
    for (UserCredentials credentials : authorityGroup.getMembers()) {
      for (UserAuthorityGroup role : credentials.getUserAuthorityGroups()) {
        if (role.equals(authorityGroup)) {
          return credentials.getName();
        }
      }
    }

    return null;
  }
}
