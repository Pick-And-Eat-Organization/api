package com.clickandeat.authentication.application.exceptions.application;

import com.clickandeat.shared.exceptions.AbstractApplicationException;

public class CredentialsNotFoundException extends AbstractApplicationException {
  public CredentialsNotFoundException() {
    super("CREDENTIALS_NOT_FOUND", "Credentials not found in database.", null);
  }
}
