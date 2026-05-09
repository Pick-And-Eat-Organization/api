package com.clickandeat.authentication.application.usecase.register;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.clickandeat.authentication.application.exceptions.application.EmailAlreadyUsedException;
import com.clickandeat.authentication.application.exceptions.technical.CannotHashPasswordException;
import com.clickandeat.authentication.application.exceptions.technical.DatabaseTechnicalException;
import com.clickandeat.authentication.domain.Credentials;
import com.clickandeat.authentication.domain.repository.ICredentialsRepository;
import com.clickandeat.authentication.domain.service.IPasswordService;
import com.clickandeat.authentication.domain.valueobject.Role;
import com.clickandeat.shared.account.CreateGenericAccountPort;
import com.clickandeat.shared.account.CreateGenericAccountRequest;
import com.clickandeat.shared.account.CreateProAccountPort;
import com.clickandeat.shared.account.CreateProAccountRequest;
import com.clickandeat.shared.enums.CredentialsStatus;
import com.clickandeat.shared.enums.RoleName;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;

@Tag("unit")
public class RegisterUseCaseUnitTest {
  private ICredentialsRepository credentialsRepository;

  private RegisterUseCase registerUseCase;

  private IPasswordService passwordService;

  private CreateGenericAccountPort createGenericAccountPort;
  private CreateProAccountPort createProAccountPort;

  private RegisterCommand getCommand() {
    String dateString = "2025-05-24";
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    return new RegisterCommand(
        "test@gmail.com",
        "clearPassword",
        "jerome",
        "juda",
        "+33650333340",
        LocalDate.parse(dateString, formatter),
        new Role(RoleName.CONSUMER, null));
  }

  @BeforeEach
  void init() {
    this.credentialsRepository = mock(ICredentialsRepository.class);
    this.passwordService = mock(IPasswordService.class);
    this.createGenericAccountPort = mock(CreateGenericAccountPort.class);
    this.createProAccountPort = mock(CreateProAccountPort.class);
    this.registerUseCase =
        new RegisterUseCase(
            credentialsRepository,
            passwordService,
            createGenericAccountPort,
            createProAccountPort);
  }

  @Test
  public void register_shouldThrowEmailAlreadyUsedException_whenEmailIsAlreadyTaken() {
    RegisterCommand command = getCommand();

    Credentials existingCredentials = mock(Credentials.class);
    when(credentialsRepository.findByEmail(command.email()))
        .thenReturn(Optional.of(existingCredentials));

    assertThrows(EmailAlreadyUsedException.class, () -> registerUseCase.execute(command));
  }

  @Test
  public void register_shouldThrowCannotHashPasswordException_whenPasswordHashingFails() {
    RegisterCommand command = getCommand();

    when(credentialsRepository.findByEmail(command.email())).thenReturn(Optional.empty());

    RuntimeException rootCause = new RuntimeException("Hashing failed");

    when(passwordService.hashPassword(command.password())).thenThrow(rootCause);

    assertThrows(CannotHashPasswordException.class, () -> registerUseCase.execute(command));
  }

  @Test
  public void register_shouldThrowRegistrationTechnicalException_whenDatabaseSaveFails() {
    RegisterCommand command = getCommand();

    when(credentialsRepository.findByEmail(command.email())).thenReturn(Optional.empty());

    String hashPassword = "hashPassword";

    when(passwordService.hashPassword(command.password())).thenReturn(hashPassword);

    when(credentialsRepository.save(any(Credentials.class)))
        .thenThrow(DataIntegrityViolationException.class);

    assertThrows(DatabaseTechnicalException.class, () -> registerUseCase.execute(command));
  }

  @Test
  public void register_shouldReturnUserId_whenRegistrationSucceeds() {
    RegisterCommand command = getCommand();

    when(credentialsRepository.findByEmail(command.email())).thenReturn(Optional.empty());

    String hashPassword = "hashPassword";

    when(passwordService.hashPassword(command.password())).thenReturn(hashPassword);

    UUID credentialsId = UUID.randomUUID();

    when(credentialsRepository.save(any(Credentials.class))).thenReturn(credentialsId);
    when(createGenericAccountPort.createGenericAccount(any(CreateGenericAccountRequest.class)))
        .thenReturn(1L);

    UUID result = registerUseCase.execute(command);

    assertEquals(credentialsId, result);
    verify(createGenericAccountPort)
        .createGenericAccount(
            eq(
                new CreateGenericAccountRequest(
                    credentialsId,
                    command.firstName(),
                    command.lastName(),
                    command.role().name(),
                    command.phoneNumber(),
                    command.birthDate())));
  }

  @Test
  public void register_shouldCreateActiveCredentialsByDefault() {
    RegisterCommand command = getCommand();

    when(credentialsRepository.findByEmail(command.email())).thenReturn(Optional.empty());
    when(passwordService.hashPassword(command.password())).thenReturn("hashPassword");
    when(credentialsRepository.save(any(Credentials.class))).thenReturn(UUID.randomUUID());

    registerUseCase.execute(command);

    org.mockito.ArgumentCaptor<Credentials> captor =
        org.mockito.ArgumentCaptor.forClass(Credentials.class);
    verify(credentialsRepository).save(captor.capture());
    Credentials savedCredentials = captor.getValue();

    assertEquals(CredentialsStatus.ACTIVE, savedCredentials.getStatus());
    assertFalse(savedCredentials.isEmailVerified());
    assertFalse(savedCredentials.isPhoneVerified());
  }

  @Test
  public void registerPro_shouldThrowIfRoleIsNotPro() {
    RegisterCommand command = getCommand();

    assertThrows(
        IllegalArgumentException.class,
        () ->
            registerUseCase.registerPro(
                new RegisterCommand(
                    command.email(),
                    command.password(),
                    command.firstName(),
                    command.lastName(),
                    command.phoneNumber(),
                    command.birthDate(),
                    new Role(RoleName.CONSUMER, null)),
                new CreateProAccountRequest(
                    command.firstName(),
                    command.lastName(),
                    command.phoneNumber(),
                    command.birthDate(),
                    "kb",
                    "12345678901234",
                    "legal",
                    "SARL",
                    "addr1",
                    null,
                    null,
                    "city",
                    "75000",
                    "France")));
  }
}
