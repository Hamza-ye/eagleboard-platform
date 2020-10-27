package com.mass3d.common;

public class DeleteNotAllowedException
    extends RuntimeException {

  public static final String ERROR_ASSOCIATED_BY_OTHER_OBJECTS = "Object associated by other objects";

  private String errorCode;

  public DeleteNotAllowedException(String errorCode, String message) {
    super(message);
    this.errorCode = errorCode;
  }

  public String getErrorCode() {
    return errorCode;
  }
}
