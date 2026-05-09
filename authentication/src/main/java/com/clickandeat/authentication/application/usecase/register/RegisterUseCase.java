package com.clickandeat.authentication.application.usecase.register;

import com.clickandeat.authentication.application.exceptions.application.EmailAlreadyUsedException;
import com.clickandeat.authentication.application.exceptions.technical.CannotHashPasswordException;
import com.clickandeat.authentication.application.exceptions.technical.DatabaseTechnicalException;
import com.clickandeat.authentication.domain.Credentials;
import com.clickandeat.authentication.domain.repository.ICredentialsRepository;
import com.clickandeat.authentication.domain.service.IPasswordService;
import com.clickandeat.shared.account.CreateGenericAccountPort;
import com.clickandeat.shared.account.CreateGenericAccountRequest;
import com.clickandeat.shared.account.CreateProAccountPort;
import com.clickandeat.shared.account.CreateProAccountRequest;
import java.util.Date;
import java.util.UUID;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RegisterUseCase implements IRegisterUseCase {

  private final ICredentialsRepository credentialsRepository;
  private final IPasswordService passwordService;
  private final CreateGenericAccountPort createGenericAccountPort;
  private final CreateProAccountPort createProAccountPort;

  public RegisterUseCase(
      ICredentialsRepository respository,
      IPasswordService service,
      CreateGenericAccountPort createGenericAccountPort,
      CreateProAccountPort createProAccountPort) {
    this.credentialsRepository = respository;
    this.passwordService = service;
    this.createGenericAccountPort = createGenericAccountPort;
    this.createProAccountPort = createProAccountPort;
  }

  @Override
  public UUID execute(RegisterCommand command) {
    if (command.role().isPro()) {
      throw new IllegalArgumentException("Use the dedicated pro register endpoint");
    }
    return registerGeneric(command);
  }

  @Override
  public UUID registerPro(RegisterCommand command, CreateProAccountRequest proAccountRequest) {
    if (!command.role().isPro()) {
      throw new IllegalArgumentException("Pro registration requires a PRO role");
    }
    return persistAndProvisionProAccount(command, proAccountRequest);
  }

  private UUID registerGeneric(RegisterCommand command) {
    return persistAndProvisionGenericAccount(command);
  }

  private UUID persistAndProvisionGenericAccount(RegisterCommand command) {
    ensureEmailIsUnique(command.email());

    String hashedPassword = hashPassword(command.password());
    Credentials credentials = createCredentials(command, hashedPassword);

    UUID credentialsId = persistCredentials(credentials);
    this.createGenericAccount(credentialsId, command);
    return credentialsId;
  }

  private UUID persistAndProvisionProAccount(
      RegisterCommand command, CreateProAccountRequest proAccountRequest) {
    ensureEmailIsUnique(command.email());

    String hashedPassword = hashPassword(command.password());
    Credentials credentials = createCredentials(command, hashedPassword);

    UUID credentialsId = persistCredentials(credentials);
    this.createProAccount(credentialsId, proAccountRequest);
    return credentialsId;
  }

  private void ensureEmailIsUnique(String email) {
    if (credentialsRepository.findByEmail(email).isPresent()) {
      throw new EmailAlreadyUsedException();
    }
  }

  private String hashPassword(String plainPassword) {
    try {
      return passwordService.hashPassword(plainPassword);
    } catch (Exception e) {
      throw new CannotHashPasswordException(e);
    }
  }

  private Credentials createCredentials(RegisterCommand command, String hashedPassword) {
    return new Credentials(null, command.email(), hashedPassword, command.role(), new Date(), null);
  }

  private UUID persistCredentials(Credentials credentials) {
    try {
      return credentialsRepository.save(credentials);
    } catch (DataIntegrityViolationException e) {
      throw new DatabaseTechnicalException("An error occurred while inserting the user", e);
    }
  }

  private void createGenericAccount(UUID credentialsId, RegisterCommand command) {
    CreateGenericAccountRequest request =
        new CreateGenericAccountRequest(
            credentialsId,
            command.firstName(),
            command.lastName(),
            command.role().name(),
            command.phoneNumber(),
            command.birthDate());
    this.createGenericAccountPort.createGenericAccount(request);
  }

  private void createProAccount(UUID credentialsId, CreateProAccountRequest request) {
    this.createProAccountPort.createProAccount(credentialsId, request);
  }
}
