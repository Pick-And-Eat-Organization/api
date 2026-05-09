package com.clickandeat.authentication.application.exceptions.application;

import com.clickandeat.shared.exceptions.AbstractApplicationException;

public class CredentialsNotActiveException extends AbstractApplicationException {
  public CredentialsNotActiveException() {
    super(
        "CREDENTIALS_NOT_ACTIVE",
        "This account is suspended or not yet activated.",
        null);
  }
}
