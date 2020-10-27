package com.mass3d.common;

import javassist.util.proxy.ProxyFactory;
import org.slf4j.Logger;

public class AuditLogUtil {

  public static final String ACTION_CREATE = "create";
  public static final String ACTION_READ = "read";
  public static final String ACTION_UPDATE = "update";
  public static final String ACTION_DELETE = "delete";

  public static final String ACTION_CREATE_DENIED = "create denied";
  public static final String ACTION_READ_DENIED = "read denied";
  public static final String ACTION_UPDATE_DENIED = "update denied";
  public static final String ACTION_DELETE_DENIED = "delete denied";

  public static void infoWrapper(Logger log, Object object, String action) {
    infoWrapper(log, UserContext.getUsername(), object, action);
  }

  public static void infoWrapper(Logger log, String username, Object object, String action) {
    if (log.isInfoEnabled()) {
      if (username != null && object != null && object instanceof IdentifiableObject) {
        IdentifiableObject idObject = (IdentifiableObject) object;
        StringBuilder builder = new StringBuilder();

        builder.append("'").append(username).append("' ").append(action);

        if (!ProxyFactory.isProxyClass(object.getClass())) {
          builder.append(" ").append(object.getClass().getName());
        } else {
          builder.append(" ").append(object.getClass().getSuperclass().getName());
        }

        if (idObject.getName() != null && !idObject.getName().isEmpty()) {
          builder.append(", name: ").append(idObject.getName());
        }

        if (idObject.getUid() != null && !idObject.getUid().isEmpty()) {
          builder.append(", uid: ").append(idObject.getUid());
        }

        log.info(builder.toString());
      }
    }
  }
}
