package com.mass3d.common.exception;

public class InvalidIdentifierReferenceException
    extends RuntimeException {

  public InvalidIdentifierReferenceException(String message) {
    super(message);
  }

  public InvalidIdentifierReferenceException(String message, Throwable cause) {
    super(message, cause);
  }
}
