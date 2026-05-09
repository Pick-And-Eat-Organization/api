package com.clickandeat.authentication.application.usecase.credentials_management;

import java.util.UUID;

public interface ICredentialsManagementUseCase {
  void activate(UUID credentialsId);

  void suspend(UUID credentialsId);

  void verifyEmail(UUID credentialsId);

  void verifyPhone(UUID credentialsId);
}
