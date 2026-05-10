package com.clickandeat.account.application.exceptions.application;

import com.clickandeat.shared.exceptions.AbstractApplicationException;

public class AccountNotFoundException extends AbstractApplicationException {
  public AccountNotFoundException() {
    super("ACCOUNT_NOT_FOUND", "Account does not exist.", null);
  }
}
