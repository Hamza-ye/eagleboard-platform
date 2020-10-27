package com.mass3d.system.deletion;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import javassist.util.proxy.ProxyObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.mass3d.common.DeleteNotAllowedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * TODO: Add support for failed allow tests on "transitive" deletion handlers which are called as
 * part of delete methods.
 *
 */
@Component( "deletionManager" )
public class DefaultDeletionManager
    implements DeletionManager {

  private static final Log log = LogFactory.getLog(DefaultDeletionManager.class);

  private static final String DELETE_METHOD_PREFIX = "delete";
  private static final String ALLOW_METHOD_PREFIX = "allowDelete";

  /**
   * Deletion handlers registered in context are subscribed to deletion notifications through
   * auto-wiring.
   */
  @Autowired(required = false)
  private List<DeletionHandler> deletionHandlers;

  // -------------------------------------------------------------------------
  // DeletionManager implementation
  // -------------------------------------------------------------------------

  @Override
  public void execute(Object object) {
    if (deletionHandlers == null || deletionHandlers.isEmpty()) {
      log.info("No deletion handlers registered, aborting deletion handling");
      return;
    }

    log.debug("Deletion handlers detected: " + deletionHandlers.size());

    Class<?> clazz = getClazz(object);

    String className = clazz.getSimpleName();

    // ---------------------------------------------------------------------
    // Verify that object is allowed to be deleted
    // ---------------------------------------------------------------------

    String allowMethodName = ALLOW_METHOD_PREFIX + className;

    String currentHandler = null;

    try {
      Method allowMethod = DeletionHandler.class.getMethod(allowMethodName, clazz);

      for (DeletionHandler handler : deletionHandlers) {
        currentHandler = handler.getClass().getSimpleName();

        log.debug("Check if allowed using " + currentHandler + " for class " + className);

        Object allow = allowMethod.invoke(handler, object);

        if (allow != null) {
          String hint = String.valueOf(allow);

          String message = "Could not delete due to association with another object: " +
              handler.getClassName() + (hint.isEmpty() ? hint : (" (" + hint + ")"));

          log.info("Delete was not allowed by " + currentHandler + ": " + message);

          throw new DeleteNotAllowedException(
              DeleteNotAllowedException.ERROR_ASSOCIATED_BY_OTHER_OBJECTS, message);
        }
      }
    } catch (NoSuchMethodException e) {
      log.error("Method '" + allowMethodName + "' does not exist on class '" + clazz + "'", e);
      return;
    } catch (IllegalAccessException ex) {
      log.error("Method '" + allowMethodName + "' can not be invoked on DeletionHandler '"
          + currentHandler + "'", ex);
      return;
    } catch (InvocationTargetException ex) {
      log.error(
          "Method '" + allowMethodName + "' threw exception on DeletionHandler '" + currentHandler
              + "'", ex);
      return;
    }

    // ---------------------------------------------------------------------
    // Delete associated objects
    // ---------------------------------------------------------------------

    String deleteMethodName = DELETE_METHOD_PREFIX + className;

    try {
      Method deleteMethod = DeletionHandler.class.getMethod(deleteMethodName, clazz);

      for (DeletionHandler handler : deletionHandlers) {
        currentHandler = handler.getClass().getSimpleName();

        log.debug("Deleting object using " + currentHandler + " for class " + className);

        deleteMethod.invoke(handler, object);
      }
    } catch (Exception ex) {
      log.error(
          "Failed to invoke method " + deleteMethodName + " on DeletionHandler '" + currentHandler
              + "'", ex);
      return;
    }

    log.info("Deleted objects associated with object of type " + className);
  }

  private Class<?> getClazz(Object object) {
    Class<?> clazz = object.getClass();

    while (ProxyObject.class.isAssignableFrom(clazz)) {
      clazz = clazz.getSuperclass();
    }

    return clazz;
  }
}
