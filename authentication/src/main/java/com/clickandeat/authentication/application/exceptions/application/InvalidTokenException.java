package com.clickandeat.authentication.application.exceptions.application;

import com.clickandeat.shared.exceptions.AbstractApplicationException;

public class InvalidTokenException extends AbstractApplicationException {
  public InvalidTokenException() {
    super("INVALID_TOKEN", "Token is not valid.", null);
  }
}
