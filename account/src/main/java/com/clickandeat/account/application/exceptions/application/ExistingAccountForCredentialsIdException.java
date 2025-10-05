package com.clickandeat.account.application.exceptions.application;

import com.clickandeat.shared.exceptions.AbstractApplicationException;

public class ExistingAccountForCredentialsIdException extends AbstractApplicationException {
  public ExistingAccountForCredentialsIdException() {
    super("PROFIL_ALREADY_EXISTS", "An account is linked to the credentials ID", null);
  }
}
