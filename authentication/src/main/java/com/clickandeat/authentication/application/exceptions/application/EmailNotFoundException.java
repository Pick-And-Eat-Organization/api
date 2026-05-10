package com.clickandeat.authentication.application.exceptions.application;

import com.clickandeat.shared.exceptions.AbstractApplicationException;

public class EmailNotFoundException extends AbstractApplicationException {
  public EmailNotFoundException() {
    super("INVALID_CREDENTIALS", "Email not registered in database.", null);
  }
}
