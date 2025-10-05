package com.clickandeat.authentication.application.exceptions.application;

import com.clickandeat.shared.exceptions.AbstractApplicationException;

public class PasswordNotMatchException extends AbstractApplicationException {
  public PasswordNotMatchException() {
    super("INVALID_CREDENTIALS", "Given password does not match with user password.", null);
  }
}
