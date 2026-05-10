package com.clickandeat.authentication.application.exceptions.application;

import com.clickandeat.shared.exceptions.AbstractApplicationException;

public class UserNotFoundException extends AbstractApplicationException {
  public UserNotFoundException() {
    super("USER_NOT_FOUND", "User does not exist.", null);
  }
}
