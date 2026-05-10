package com.clickandeat.authentication.application.exceptions.application;

import com.clickandeat.shared.exceptions.AbstractApplicationException;

public class InvalidVerificationCodeException extends AbstractApplicationException {
  public InvalidVerificationCodeException() {
    super("INVALID_VERIFICATION_CODE", "Verification code is invalid.", null);
  }
}
