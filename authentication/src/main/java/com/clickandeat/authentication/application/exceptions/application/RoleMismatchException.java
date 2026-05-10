package com.clickandeat.authentication.application.exceptions.application;

import com.clickandeat.shared.exceptions.AbstractApplicationException;

public class RoleMismatchException extends AbstractApplicationException {
  public RoleMismatchException() {
    super(
        "ROLE_MISMATCH",
        "The user's role does not match the requested authentication context.",
        null);
  }
}
