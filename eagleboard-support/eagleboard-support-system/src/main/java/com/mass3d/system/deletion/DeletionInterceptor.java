package com.mass3d.system.deletion;

import static com.google.common.base.Preconditions.checkNotNull;

import org.aspectj.lang.JoinPoint;
import org.springframework.stereotype.Component;

/**
 * @version $Id$
 */
@Component("deletionInterceptor")
public class DeletionInterceptor {
  // -------------------------------------------------------------------------
  // Dependencies
  // -------------------------------------------------------------------------

  private DeletionManager deletionManager;

  public DeletionInterceptor(DeletionManager deletionManager) {
    checkNotNull( deletionManager );
    this.deletionManager = deletionManager;
  }

  public void intercept(JoinPoint joinPoint) {
    if (joinPoint.getArgs() != null && joinPoint.getArgs().length > 0) {
      deletionManager.execute(joinPoint.getArgs()[0]);
    }
  }
}
