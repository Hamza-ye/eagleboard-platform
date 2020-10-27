package com.mass3d.common;

public class QueryTimeoutException
    extends RuntimeException {

  public QueryTimeoutException(String message) {
    super(message);
  }

  public QueryTimeoutException(String message, Throwable throwable) {
    super(message, throwable);
  }
}
