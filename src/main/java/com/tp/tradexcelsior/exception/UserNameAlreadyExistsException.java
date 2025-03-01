package com.tp.tradexcelsior.exception;

public class UserNameAlreadyExistsException extends RuntimeException {
  public UserNameAlreadyExistsException(String message) {
    super(message);
  }
}

