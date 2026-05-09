package com.clickandeat.authentication.application.usecase.get_current_credentials;

import com.clickandeat.authentication.application.exceptions.application.CredentialsNotFoundException;
import com.clickandeat.authentication.domain.Credentials;
import com.clickandeat.authentication.domain.repository.ICredentialsRepository;
import com.clickandeat.shared.account.CurrentCredentialsResponse;
import com.clickandeat.shared.account.GetCurrentCredentialsPort;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GetCurrentCredentialsUseCase implements GetCurrentCredentialsPort {

  private final ICredentialsRepository credentialsRepository;

  public GetCurrentCredentialsUseCase(ICredentialsRepository credentialsRepository) {
    this.credentialsRepository = credentialsRepository;
  }

  @Override
  @Transactional(readOnly = true)
  public CurrentCredentialsResponse getCurrentCredentials(UUID credentialsId) {
    Credentials credentials =
        this.credentialsRepository
            .findByUserId(credentialsId.toString())
            .orElseThrow(CredentialsNotFoundException::new);
    return new CurrentCredentialsResponse(credentials.getPhoneNumber());
  }
}
