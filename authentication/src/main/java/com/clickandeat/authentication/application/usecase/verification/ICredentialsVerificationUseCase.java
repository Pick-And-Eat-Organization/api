package com.clickandeat.authentication.application.usecase.verification;

public interface ICredentialsVerificationUseCase {
  void verifyEmail(VerifyCredentialsCodeCommand command);

  void verifyPhone(VerifyCredentialsCodeCommand command);
}
