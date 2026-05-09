package com.clickandeat.authentication.application.usecase.credentials_management;

import com.clickandeat.authentication.application.exceptions.application.CredentialsNotFoundException;
import com.clickandeat.authentication.domain.Credentials;
import com.clickandeat.authentication.domain.repository.ICredentialsRepository;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CredentialsManagementUseCase implements ICredentialsManagementUseCase {

  private final ICredentialsRepository credentialsRepository;

  public CredentialsManagementUseCase(ICredentialsRepository credentialsRepository) {
    this.credentialsRepository = credentialsRepository;
  }

  @Override
  public void activate(UUID credentialsId) {
    Credentials credentials = this.findCredentials(credentialsId);
    credentials.activate();
    this.credentialsRepository.save(credentials);
  }

  @Override
  public void suspend(UUID credentialsId) {
    Credentials credentials = this.findCredentials(credentialsId);
    credentials.suspend();
    this.credentialsRepository.save(credentials);
  }

  @Override
  public void verifyEmail(UUID credentialsId) {
    Credentials credentials = this.findCredentials(credentialsId);
    credentials.verifyEmail();
    this.credentialsRepository.save(credentials);
  }

  @Override
  public void verifyPhone(UUID credentialsId) {
    Credentials credentials = this.findCredentials(credentialsId);
    credentials.verifyPhone();
    this.credentialsRepository.save(credentials);
  }

  private Credentials findCredentials(UUID credentialsId) {
    return this.credentialsRepository
        .findByUserId(credentialsId.toString())
        .orElseThrow(CredentialsNotFoundException::new);
  }
}
