package com.mass3d.hibernate.exception;

import org.springframework.security.access.AccessDeniedException;

public class ReadAccessDeniedException extends AccessDeniedException {

  public ReadAccessDeniedException(String msg) {
    super(msg);
  }
}
