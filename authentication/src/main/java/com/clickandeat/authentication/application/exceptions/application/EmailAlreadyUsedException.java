package com.clickandeat.authentication.application.exceptions.application;

import com.clickandeat.shared.exceptions.AbstractApplicationException;

public class EmailAlreadyUsedException extends AbstractApplicationException {

  public EmailAlreadyUsedException() {
    super("EMAIL_ALREADY_USED", "Email is already registered.", null);
  }
}
