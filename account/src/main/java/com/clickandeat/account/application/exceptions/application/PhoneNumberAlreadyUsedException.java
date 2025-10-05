package com.clickandeat.account.application.exceptions.application;

import com.clickandeat.shared.exceptions.AbstractApplicationException;

public class PhoneNumberAlreadyUsedException extends AbstractApplicationException {
  public PhoneNumberAlreadyUsedException() {
    super("PHONE_NUMBER_TAKEN", "An another account is linked to this phone number", null);
  }
}
