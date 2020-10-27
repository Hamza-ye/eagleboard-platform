package com.mass3d.dxf2.feedback;

import java.text.MessageFormat;

public class ErrorMessage {

  private final ErrorCode errorCode;

  private final Object[] args;

  public ErrorMessage(ErrorCode errorCode, Object... args) {
    this.errorCode = errorCode;
    this.args = args;
  }

  public ErrorCode getErrorCode() {
    return errorCode;
  }

  public String getMessage() {
    return MessageFormat.format(errorCode.getMessage(), args);
  }
}
