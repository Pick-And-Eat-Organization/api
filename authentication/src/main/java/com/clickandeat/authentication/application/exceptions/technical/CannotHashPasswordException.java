package com.clickandeat.authentication.application.exceptions.technical;

import com.clickandeat.shared.exceptions.AbstractTechnicalException;

public class CannotHashPasswordException extends AbstractTechnicalException {
  public CannotHashPasswordException(Throwable throwable) {
    super("password-service", "Cannot hash the given password.", throwable);
  }
}
