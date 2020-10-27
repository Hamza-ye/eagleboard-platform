package com.mass3d.hibernate.exception;

import org.springframework.security.access.AccessDeniedException;

public class DeleteAccessDeniedException extends AccessDeniedException {

  public DeleteAccessDeniedException(String msg) {
    super(msg);
  }
}
