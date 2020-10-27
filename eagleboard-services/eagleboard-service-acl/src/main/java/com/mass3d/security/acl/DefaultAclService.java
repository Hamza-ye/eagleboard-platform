package com.mass3d.security.acl;

import static org.springframework.util.CollectionUtils.containsAny;

import com.mass3d.common.BaseIdentifiableObject;
import com.mass3d.common.IdentifiableObject;
import com.mass3d.feedback.ErrorCode;
import com.mass3d.feedback.ErrorReport;
import com.mass3d.security.AuthorityType;
import com.mass3d.user.User;
import com.mass3d.user.UserAccess;
import com.mass3d.user.UserGroupAccess;
import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaService;
import com.mass3d.security.acl.AccessStringHelper.Permission;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Default ACL implementation that uses SchemaDescriptors to get authorities / sharing flags.
 *
 */

@Service("com.mass3d.security.acl.AclService")
public class DefaultAclService implements AclService {

  private final SchemaService schemaService;

  @Autowired
  public DefaultAclService(SchemaService schemaService) {
    this.schemaService = schemaService;
  }

  @Override
  public boolean isSupported(String type) {
    return schemaService.getSchemaBySingularName(type) != null;
  }

  @Override
  public boolean isSupported(Class<?> klass) {
    return schemaService.getSchema(klass) != null;
  }

  @Override
  public boolean isShareable(String type) {
    Schema schema = schemaService.getSchemaBySingularName(type);
    return schema != null && schema.isShareable();
  }

  @Override
  public boolean isShareable(Class<?> klass) {
    Schema schema = schemaService.getSchema(klass);
    return schema != null && schema.isShareable();
  }

  @Override
  public boolean isDataShareable(Class<?> klass) {
    Schema schema = schemaService.getSchema(klass);
    return schema != null && schema.isDataShareable();
  }

  /**
   * Can user read this object
   * <p/>
   * 1. Does user have ACL_OVERRIDE_AUTHORITIES authority?
   * 2. Is the user for the object null?
   * 3. Is the user of the object equal to current user?
   * 4. Is the object public read?
   * 5. Does any of the userGroupAccesses contain public read and the current user is in that group
   *
   * @param user User to check against
   * @param object Object to check
   * @return Result of test
   */
  @Override
  public boolean canRead(User user, IdentifiableObject object) {
    // 1. Does user have ACL_OVERRIDE_AUTHORITIES authority?
    if (object == null || haveOverrideAuthority(user)) {
      return true;
    }

    Schema schema = schemaService.getSchema(object.getClass());

    if (schema == null) {
      return true;
    }

    // (1) canAccess(User user, Collection<String> anyAuthorities)
    /*  -> haveOverrideAuthority(User user) ||
     *      -> user == null || user.isSuper()
     *
     *  -> anyAuthorities.isEmpty() ||
     *  -> haveAuthority(user, anyAuthorities);
     *      -> containsAny(user.getUserCredentials().getAllAuthorities(), anyAuthorities);
     ************************************
     */
    if (canAccess(user, schema.getAuthorityByType(AuthorityType.READ))) {
      // 2. Is the user for the object null?
      // 4. Is the object public read?

      // (0) is object not Shareable
            // -> (havePersistedProperty("user") && havePersistedProperty("userGroupAccesses")
            //          && havePersistedProperty("publicAccess"));
      // (1) checkUser(user, object) /////////////////////////////////
        /* checkUser(User user, IdentifiableObject object)
         * - is the user trying to access is null
         * - is the user owner of the object is null
         *    -> user == null || object.getUser() == null ||
         *
         * - is the user trying to access is same as the owner of the object
         *    -> user.getUid().equals(object.getUser().getUid())
         ************************************************************
      //(2) checkSharingPermission(user, object, Permission.READ) ////
        * - Is the object public read?
        *     -> AccessStringHelper.isEnabled(object.getPublicAccess(), permission = permission.READ)
        *
        * - Is the user allowed to read this object through group access
        *     -> for (UserGroupAccess userGroupAccess : object.getUserGroupAccesses())
        *          if (AccessStringHelper.isEnabled(userGroupAccess.getAccess(), permission)
        *             && userGroupAccess.getUserGroup().getMembers().contains(user))
        *
        * - Is the user allowed to read to this object through user access?
        *     -> for (UserAccess userAccess : object.getUserAccesses())
        *           if (AccessStringHelper.isEnabled(userAccess.getAccess(), permission)
        *             && user.equals(userAccess.getUser()))
        ************************************************************
      */
      if (!schema.isShareable() || object.getUser() == null || object.getPublicAccess() == null
          || checkUser(user, object)
          || checkSharingPermission(user, object, Permission.READ)) {
        return true;
      }
    } else {
      return false;
    }

    return false;
  }

  @Override
  public boolean canDataRead(User user, IdentifiableObject object) {
    if (object == null || haveOverrideAuthority(user)) {
      return true;
    }

    Schema schema = schemaService.getSchema(object.getClass());

    if (schema == null) {
      return true;
    }

    if (canAccess(user, schema.getAuthorityByType(AuthorityType.DATA_READ))) {
      if (schema.isDataShareable() &&
          (checkSharingPermission(user, object, Permission.DATA_READ)
              || checkSharingPermission(user, object, Permission.DATA_WRITE))) {
        return true;
      }
    }

    return false;
  }

  @Override
  public boolean canDataOrMetadataRead(User user, IdentifiableObject object) {
    Schema schema = schemaService.getSchema(object.getClass());

    return schema.isDataShareable() ? canDataRead(user, object) : canRead(user, object);
  }

  @Override
  public boolean canWrite(User user, IdentifiableObject object) {
    if (object == null || haveOverrideAuthority(user)) {
      return true;
    }

    Schema schema = schemaService.getSchema(object.getClass());

    if (schema == null) {
      return true;
    }

    List<String> anyAuthorities = new ArrayList<>(schema.getAuthorityByType(AuthorityType.CREATE));

    if (anyAuthorities.isEmpty()) {
      anyAuthorities.addAll(schema.getAuthorityByType(AuthorityType.CREATE_PRIVATE));
      anyAuthorities.addAll(schema.getAuthorityByType(AuthorityType.CREATE_PUBLIC));
    }

    // (1) canAccess(User user, Collection<String> anyAuthorities)
    /*  -> haveOverrideAuthority(User user) ||
     *      -> user == null || user.isSuper()
     *
     *  -> anyAuthorities.isEmpty() ||
     *  -> haveAuthority(user, anyAuthorities);
     *      -> containsAny(user.getUserCredentials().getAllAuthorities(), anyAuthorities);
     ************************************
     */
    if (canAccess(user, anyAuthorities)) {
      if (!schema.isShareable()) {
        return true;
      }

      // checkSharingAccess(User user, IdentifiableObject object)
        /*
         * -> Is the current user allowed to create/update the object given
         *    based on its sharing settings.
         *    -> return true only if not
         *      -> object.getPublicAccess() = ------ (default access) and
         *          !canMakePublic and !canMakePrivate --> return false
         *      -> !canMakePublic --> return false
         *      -> object.getExternalAccess() && !canMakeExternal --> return false
         */
      // (1) checkUser(user, object) /////////////////////////////////
        /* checkUser(User user, IdentifiableObject object)
         * - is the user trying to access is null
         * - is the user owner of the object is null
         *    -> user == null || object.getUser() == null ||
         *
         * - is the user trying to access is same as the owner of the object
         *    -> user.getUid().equals(object.getUser().getUid())
         ************************************************************
      //(2) checkSharingPermission(user, object, Permission.READ) ////
        * - Is the object public read?
        *     -> AccessStringHelper.isEnabled(object.getPublicAccess(), permission = permission.READ)
        *
        * - Is the user allowed to read this object through group access
        *     -> for (UserGroupAccess userGroupAccess : object.getUserGroupAccesses())
        *          if (AccessStringHelper.isEnabled(userGroupAccess.getAccess(), permission)
        *             && userGroupAccess.getUserGroup().getMembers().contains(user))
        *
        * - Is the user allowed to read to this object through user access?
        *     -> for (UserAccess userAccess : object.getUserAccesses())
        *           if (AccessStringHelper.isEnabled(userAccess.getAccess(), permission)
        *             && user.equals(userAccess.getUser()))
        ************************************************************
      */
      if (checkSharingAccess(user, object) &&
          (checkUser(user, object) || checkSharingPermission(user, object, Permission.WRITE))) {
        return true;
      }
    } else if (schema.isImplicitPrivateAuthority() && checkSharingAccess(user, object)) {
      return true;
    }

    return false;
  }

  @Override
  public boolean canDataWrite(User user, IdentifiableObject object) {
    if (object == null || haveOverrideAuthority(user)) {
      return true;
    }

    Schema schema = schemaService.getSchema(object.getClass());

    if (schema == null) {
      return true;
    }

    // returned unmodifiable list does not need to be cloned since it is not modified
    List<String> anyAuthorities = schema.getAuthorityByType(AuthorityType.DATA_CREATE);

    if (canAccess(user, anyAuthorities)) {
      if (schema.isDataShareable() && checkSharingPermission(user, object, Permission.DATA_WRITE)) {
        return true;
      }
    }

    return false;
  }

  @Override
  public boolean canUpdate(User user, IdentifiableObject object) {
    if (object == null || haveOverrideAuthority(user)) {
      return true;
    }

    Schema schema = schemaService.getSchema(object.getClass());

    if (schema == null) {
      return true;
    }

    List<String> anyAuthorities = new ArrayList<>(schema.getAuthorityByType(AuthorityType.UPDATE));

    if (anyAuthorities.isEmpty()) {
      anyAuthorities.addAll(schema.getAuthorityByType(AuthorityType.CREATE));
      anyAuthorities.addAll(schema.getAuthorityByType(AuthorityType.CREATE_PRIVATE));
      anyAuthorities.addAll(schema.getAuthorityByType(AuthorityType.CREATE_PUBLIC));
    }

    if (canAccess(user, anyAuthorities)) {
      if (!schema.isShareable()) {
        return true;
      }

      if (checkSharingAccess(user, object) &&
          (checkUser(user, object) || checkSharingPermission(user, object, Permission.WRITE))) {
        return true;
      }
    } else if (schema.isImplicitPrivateAuthority() && checkSharingAccess(user, object)
        && (checkUser(user, object) || checkSharingPermission(user, object, Permission.WRITE))) {
      return true;
    }

    return false;
  }

  @Override
  public boolean canDelete(User user, IdentifiableObject object) {
    if (object == null || haveOverrideAuthority(user)) {
      return true;
    }

    Schema schema = schemaService.getSchema(object.getClass());

    if (schema == null) {
      return true;
    }

    List<String> anyAuthorities = new ArrayList<>(schema.getAuthorityByType(AuthorityType.DELETE));

    if (anyAuthorities.isEmpty()) {
      anyAuthorities.addAll(schema.getAuthorityByType(AuthorityType.CREATE));
      anyAuthorities.addAll(schema.getAuthorityByType(AuthorityType.CREATE_PRIVATE));
      anyAuthorities.addAll(schema.getAuthorityByType(AuthorityType.CREATE_PUBLIC));
    }

    if (canAccess(user, anyAuthorities)) {
      if (!schema.isShareable() || object.getPublicAccess() == null) {
        return true;
      }

      if (checkSharingAccess(user, object) &&
          (checkUser(user, object) || checkSharingPermission(user, object, Permission.WRITE))) {
        return true;
      }
    } else if (schema.isImplicitPrivateAuthority() && (checkUser(user, object)
        || checkSharingPermission(user, object, Permission.WRITE))) {
      return true;
    }

    return false;
  }

  @Override
  public boolean canManage(User user, IdentifiableObject object) {
    return canUpdate(user, object);
  }

  @Override
  public <T extends IdentifiableObject> boolean canRead(User user, Class<T> klass) {
    Schema schema = schemaService.getSchema(klass);

    return schema == null || schema.getAuthorityByType(AuthorityType.READ) == null
        || canAccess(user, schema.getAuthorityByType(AuthorityType.READ));
  }

  @Override
  public <T extends IdentifiableObject> boolean canCreate(User user, Class<T> klass) {
    Schema schema = schemaService.getSchema(klass);

    if (schema == null) {
      return false;
    }

    if (!schema.isShareable()) {
      return canAccess(user, schema.getAuthorityByType(AuthorityType.CREATE));
    }

    return canMakePublic(user, klass) || canMakePrivate(user, klass);
  }

  @Override
  public <T extends IdentifiableObject> boolean canMakePublic(User user, Class<T> klass) {
    Schema schema = schemaService.getSchema(klass);
    return !(schema == null || !schema.isShareable())
        && canAccess(user, schema.getAuthorityByType(AuthorityType.CREATE_PUBLIC));
  }

  @Override
  public <T extends IdentifiableObject> boolean canMakePrivate(User user, Class<T> klass) {
    Schema schema = schemaService.getSchema(klass);
    return !(schema == null || !schema.isShareable())
        && canAccess(user, schema.getAuthorityByType(AuthorityType.CREATE_PRIVATE));
  }

  @Override
  public <T extends IdentifiableObject> boolean canMakeExternal(User user, Class<T> klass) {
    Schema schema = schemaService.getSchema(klass);
    return !(schema == null || !schema.isShareable())
        && (
        (!schema.getAuthorityByType(AuthorityType.EXTERNALIZE).isEmpty() && haveOverrideAuthority(
            user))
            || haveAuthority(user, schema.getAuthorityByType(AuthorityType.EXTERNALIZE)));
  }

  @Override
  public <T extends IdentifiableObject> boolean defaultPrivate(Class<T> klass) {
    Schema schema = schemaService.getSchema(klass);
    return schema != null && schema.isDefaultPrivate();
  }

  @Override
  public <T extends IdentifiableObject> boolean defaultPublic(Class<T> klass) {
    return !defaultPrivate(klass);
  }

  @Override
  @SuppressWarnings("unchecked")
  public Class<? extends IdentifiableObject> classForType(String type) {
    Schema schema = schemaService.getSchemaBySingularName(type);

    if (schema != null && schema.isIdentifiableObject()) {
      return (Class<? extends IdentifiableObject>) schema.getKlass();
    }

    return null;
  }

  @Override
  public <T extends IdentifiableObject> Access getAccess(T object, User user) {
    if (user == null || user.isSuper()) {
      Access access = new Access(true);

      if (isDataShareable(object.getClass())) {
        access.setData(new AccessData(true, true));
      }

      return access;
    }

    Access access = new Access();
    access.setManage(canManage(user, object));
    access.setExternalize(canMakeExternal(user, object.getClass()));
    access.setWrite(canWrite(user, object));
    access.setRead(canRead(user, object));
    access.setUpdate(canUpdate(user, object));
    access.setDelete(canDelete(user, object));

    if (isDataShareable(object.getClass())) {
      AccessData data = new AccessData(canDataRead(user, object), canDataWrite(user, object));

      access.setData(data);
    }

    return access;
  }

  @Override
  public <T extends IdentifiableObject> void resetSharing(T object, User user) {
    if (object == null || !isShareable(object.getClass()) || user == null) {
      return;
    }

    BaseIdentifiableObject baseIdentifiableObject = (BaseIdentifiableObject) object;
    baseIdentifiableObject.setPublicAccess(AccessStringHelper.DEFAULT);
    baseIdentifiableObject.setExternalAccess(false);

    if (object.getUser() == null) {
      baseIdentifiableObject.setUser(user);
    }

    if (canMakePublic(user, object.getClass())) {
      if (defaultPublic(object.getClass())) {
        baseIdentifiableObject.setPublicAccess(AccessStringHelper.READ_WRITE);
      }
    }

    object.getUserAccesses().clear();
    object.getUserGroupAccesses().clear();
  }

  @Override
  public <T extends IdentifiableObject> void clearSharing(T object, User user) {
    if (object == null || !isShareable(object.getClass()) || user == null) {
      return;
    }

    BaseIdentifiableObject baseIdentifiableObject = (BaseIdentifiableObject) object;
    baseIdentifiableObject.setUser(user);
    baseIdentifiableObject.setPublicAccess(AccessStringHelper.DEFAULT);
    baseIdentifiableObject.setExternalAccess(false);

    object.getUserAccesses().clear();
    object.getUserGroupAccesses().clear();
  }

  @Override
  public <T extends IdentifiableObject> List<ErrorReport> verifySharing(T object, User user) {
    List<ErrorReport> errorReports = new ArrayList<>();

    if (object == null || haveOverrideAuthority(user) || !isShareable(object.getClass())) {
      return errorReports;
    }

    if (!AccessStringHelper.isValid(object.getPublicAccess())) {
      errorReports
          .add(new ErrorReport(object.getClass(), ErrorCode.E3010, object.getPublicAccess()));
      return errorReports;
    }

    Schema schema = schemaService.getSchema(object.getClass());

    if (!schema.isDataShareable()) {
      ErrorReport errorReport = null;

      if (object.getPublicAccess() != null && AccessStringHelper
          .hasDataSharing(object.getPublicAccess())) {
        errorReport = new ErrorReport(object.getClass(), ErrorCode.E3011, object.getClass());
      } else {
        for (UserAccess userAccess : object.getUserAccesses()) {
          if (AccessStringHelper.hasDataSharing(userAccess.getAccess())) {
            errorReport = new ErrorReport(object.getClass(), ErrorCode.E3011, object.getClass());
            break;
          }
        }

        for (UserGroupAccess userGroupAccess : object.getUserGroupAccesses()) {
          if (AccessStringHelper.hasDataSharing(userGroupAccess.getAccess())) {
            errorReport = new ErrorReport(object.getClass(), ErrorCode.E3011, object.getClass());
            break;
          }
        }
      }

      if (errorReport != null) {
        errorReports.add(errorReport);
      }
    }

    boolean canMakePublic = canMakePublic(user, object.getClass());
    boolean canMakePrivate = canMakePrivate(user, object.getClass());
    boolean canMakeExternal = canMakeExternal(user, object.getClass());

    if (object.getExternalAccess()) {
      if (!canMakeExternal) {
        errorReports.add(new ErrorReport(object.getClass(), ErrorCode.E3006, user.getUsername(),
            object.getClass()));
      }
    }

    errorReports.addAll(verifyImplicitSharing(user, object));

    if (AccessStringHelper.DEFAULT.equals(object.getPublicAccess())) {
      if (canMakePublic || canMakePrivate) {
        return errorReports;
      }

      errorReports.add(new ErrorReport(object.getClass(), ErrorCode.E3009, user.getUsername(),
          object.getClass()));
    } else {
      if (canMakePublic) {
        return errorReports;
      }

      errorReports.add(new ErrorReport(object.getClass(), ErrorCode.E3008, user.getUsername(),
          object.getClass()));
    }

    return errorReports;
  }

  private <T extends IdentifiableObject> Collection<? extends ErrorReport> verifyImplicitSharing(
      User user, T object) {
    List<ErrorReport> errorReports = new ArrayList<>();
    Schema schema = schemaService.getSchema(object.getClass());

    if (!schema.isImplicitPrivateAuthority() || checkUser(user, object) || checkSharingPermission(
        user, object, Permission.WRITE)) {
      return errorReports;
    }

    if (AccessStringHelper.DEFAULT.equals(object.getPublicAccess())) {
      errorReports.add(new ErrorReport(object.getClass(), ErrorCode.E3001, user.getUsername(),
          object.getClass()));
    }

    return errorReports;
  }

  private boolean haveOverrideAuthority(User user) {
    return user == null || user.isSuper();
  }

  private boolean canAccess(User user, Collection<String> anyAuthorities) {
    return haveOverrideAuthority(user) || anyAuthorities.isEmpty() || haveAuthority(user,
        anyAuthorities);
  }

  private boolean haveAuthority(User user, Collection<String> anyAuthorities) {
    return containsAny(user.getUserCredentials().getAllAuthorities(), anyAuthorities);
  }

  /**
   * Should user be allowed access to this object.
   *
   * @param user User to check against
   * @param object Object to check against
   * @return true/false depending on if access should be allowed
   */
  private boolean checkUser(User user, IdentifiableObject object) {
    return user == null || object.getUser() == null || user.getUid()
        .equals(object.getUser().getUid());
  }

  /**
   * Is the current user allowed to create/update the object given based on its sharing settings.
   *
   * @param user User to check against
   * @param object Object to check against
   * @return true/false depending on if sharing settings are allowed for given user
   */
  private boolean checkSharingAccess(User user, IdentifiableObject object) {
    boolean canMakePublic = canMakePublic(user, object.getClass());
    boolean canMakePrivate = canMakePrivate(user, object.getClass());
    boolean canMakeExternal = canMakeExternal(user, object.getClass());

    if (AccessStringHelper.DEFAULT.equals(object.getPublicAccess())) {
      if (!(canMakePublic || canMakePrivate)) {
        return false;
      }
    } else {
      if (!canMakePublic) {
        return false;
      }
    }

    if (object.getExternalAccess() && !canMakeExternal) {
      return false;
    }

    return true;
  }

  /**
   * If the given user allowed to access the given object using the permissions given.
   *
   * @param user User to check against
   * @param object Object to check against
   * @param permission Permission to check against
   * @return true if user can access object, false otherwise
   */
  private boolean checkSharingPermission(User user, IdentifiableObject object,
      Permission permission) {
    if (AccessStringHelper.isEnabled(object.getPublicAccess(), permission)) {
      return true;
    }

    for (UserGroupAccess userGroupAccess : object.getUserGroupAccesses()) {
      /**
       * Is the user allowed to read this object through group access?
       *
       */
      if (AccessStringHelper.isEnabled(userGroupAccess.getAccess(), permission)
          && userGroupAccess.getUserGroup().getMembers().contains(user)) {
        return true;
      }
    }

    for (UserAccess userAccess : object.getUserAccesses()) {
      /**
       * Is the user allowed to read to this object through user access?
       *
       */
      if (AccessStringHelper.isEnabled(userAccess.getAccess(), permission)
          && user.equals(userAccess.getUser())) {
        return true;
      }
    }

    return false;
  }
}