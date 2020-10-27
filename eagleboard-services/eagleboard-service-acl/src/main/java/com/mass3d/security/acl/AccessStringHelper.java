package com.mass3d.security.acl;

import com.mass3d.common.BaseIdentifiableObject;
import com.mass3d.user.UserAccess;
import com.mass3d.user.UserGroupAccess;
import org.springframework.util.Assert;

/**
 * Currently only the two first positions in the access string are used - rw.
 *
 */
public class AccessStringHelper {

  public static final String DEFAULT = "--------";
  //This should be used only when creating a default CategoryOption
  public static final String CATEGORY_OPTION_DEFAULT = "rwrw----";
  //This should be used only when creating a default Category, CategoryCombo and CategoryOptionCombo
  public static final String CATEGORY_NO_DATA_SHARING_DEFAULT = "rw------";
  public static final String READ = AccessStringHelper.newInstance()
      .enable(Permission.READ)
      .build();
  public static final String DATA_READ = AccessStringHelper.newInstance()
      .enable(Permission.DATA_READ)
      .build();
  public static final String WRITE = AccessStringHelper.newInstance()
      .enable(Permission.WRITE)
      .build();
  public static final String DATA_WRITE = AccessStringHelper.newInstance()
      .enable(Permission.DATA_WRITE)
      .build();
  public static final String READ_WRITE = AccessStringHelper.newInstance()
      .enable(Permission.READ)
      .enable(Permission.WRITE)
      .build();
  public static final String DATA_READ_WRITE = AccessStringHelper.newInstance()
      .enable(Permission.DATA_READ)
      .enable(Permission.DATA_WRITE)
      .build();
  public static final String FULL = AccessStringHelper.newInstance()
      .enable(Permission.READ)
      .enable(Permission.WRITE)
      .enable(Permission.DATA_READ)
      .enable(Permission.DATA_WRITE)
      .build();
  private char[] access = DEFAULT.toCharArray();

  public AccessStringHelper() {
  }

  public AccessStringHelper(char[] access) {
    this.access = access;
  }

  public AccessStringHelper(String access) {
    this.access = access.toCharArray();
  }

  public static AccessStringHelper newInstance() {
    return new AccessStringHelper();
  }

  public static AccessStringHelper newInstance(char[] access) {
    return new AccessStringHelper(access);
  }

  public static boolean canRead(String access) {
    return isEnabled(access, Permission.READ);
  }

  public static boolean canDataRead(String access) {
    return isEnabled(access, Permission.DATA_READ);
  }

  public static boolean canWrite(String access) {
    return isEnabled(access, Permission.WRITE);
  }

  public static boolean canDataWrite(String access) {
    return isEnabled(access, Permission.DATA_WRITE);
  }

  public static boolean canReadAndWrite(String access) {
    return isEnabled(access, Permission.READ) && isEnabled(access, Permission.WRITE);
  }

  public static boolean canDataReadAndWrite(String access) {
    return isEnabled(access, Permission.DATA_READ) && isEnabled(access, Permission.DATA_WRITE);
  }

  public static boolean canReadOrWrite(String access) {
    return isEnabled(access, Permission.READ) || isEnabled(access, Permission.WRITE);
  }

  public static boolean canDataReadOrWrite(String access) {
    return isEnabled(access, Permission.DATA_READ) || isEnabled(access, Permission.DATA_WRITE);
  }

  public static boolean isEnabled(String access, Permission permission) {
    return access == null || (validateAccessString(access)
        && access.charAt(permission.getPosition()) == permission.getValue());
  }

  public static boolean isValid(String access) {
    return access == null || validateAccessString(access);
  }

  private static boolean validateAccessString(String access) {
    Assert.notNull(access, "access can not be null");

    if (access.length() != 8 || !access.endsWith("----")) {
      return false;
    }

    byte[] bytes = access.getBytes();

    return (bytes[0] == '-' || bytes[0] == 'r')
        && (bytes[1] == '-' || bytes[1] == 'w')
        && (bytes[2] == '-' || bytes[2] == 'r')
        && (bytes[3] == '-' || bytes[3] == 'w');
  }

  public static boolean hasDataSharing(String access) {
    return AccessStringHelper.isEnabled(access, AccessStringHelper.Permission.DATA_READ)
        || AccessStringHelper.isEnabled(access, AccessStringHelper.Permission.DATA_WRITE);
  }

  public static <T extends BaseIdentifiableObject> void copySharing(T source, T target) {
    target.setPublicAccess(source.getPublicAccess());
    target.setExternalAccess(source.getExternalAccess());

    source.getUserAccesses().forEach(
        ua -> target.getUserAccesses().add(new UserAccess(ua.getUser(), ua.getAccess())));
    source.getUserGroupAccesses().forEach(
        uga -> target.getUserGroupAccesses()
            .add(new UserGroupAccess(uga.getUserGroup(), uga.getAccess())));
  }

  public AccessStringHelper enable(Permission permission) {
    access[permission.getPosition()] = permission.getValue();

    return this;
  }

  public AccessStringHelper disable(Permission permission) {
    access[permission.getPosition()] = '-';

    return this;
  }

  public String build() {
    return new String(access);
  }

  public String toString() {
    return build();
  }

  public enum Permission {
    READ('r', 0), WRITE('w', 1),
    DATA_READ('r', 2), DATA_WRITE('w', 3);

    private char value;

    private int position;

    Permission(char value, int position) {
      this.value = value;
      this.position = position;
    }

    public char getValue() {
      return value;
    }

    public int getPosition() {
      return position;
    }
  }
}
