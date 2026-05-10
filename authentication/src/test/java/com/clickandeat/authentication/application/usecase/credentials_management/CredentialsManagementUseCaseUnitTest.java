package com.clickandeat.authentication.application.usecase.credentials_management;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.clickandeat.authentication.application.exceptions.application.CredentialsNotFoundException;
import com.clickandeat.authentication.domain.Credentials;
import com.clickandeat.authentication.domain.repository.ICredentialsRepository;
import com.clickandeat.authentication.domain.valueobject.Role;
import com.clickandeat.shared.enums.CredentialsStatus;
import com.clickandeat.shared.enums.RoleName;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
class CredentialsManagementUseCaseUnitTest {

  private ICredentialsRepository credentialsRepository;
  private CredentialsManagementUseCase useCase;

  @BeforeEach
  void setUp() {
    this.credentialsRepository = mock(ICredentialsRepository.class);
    this.useCase = new CredentialsManagementUseCase(this.credentialsRepository);
  }

  @Test
  void activateShouldPersistActiveCredentials() {
    UUID credentialsId = UUID.randomUUID();
    Credentials credentials =
        new Credentials(
            credentialsId,
            "a@b.com",
            "+33601020310",
            "hashed",
            new Role(RoleName.CONSUMER, null),
            new Date(),
            null,
            CredentialsStatus.ACTIVE,
            false,
            false);
    when(this.credentialsRepository.findByUserId(credentialsId.toString()))
        .thenReturn(Optional.of(credentials));

    this.useCase.activate(credentialsId);

    verify(this.credentialsRepository).save(credentials);
    org.junit.jupiter.api.Assertions.assertTrue(credentials.isActive());
  }

  @Test
  void suspendShouldPersistSuspendedCredentials() {
    UUID credentialsId = UUID.randomUUID();
    Credentials credentials =
        new Credentials(
            credentialsId,
            "a@b.com",
            "+33601020311",
            "hashed",
            new Role(RoleName.CONSUMER, null),
            new Date(),
            null,
            CredentialsStatus.ACTIVE,
            false,
            false);
    when(this.credentialsRepository.findByUserId(credentialsId.toString()))
        .thenReturn(Optional.of(credentials));

    this.useCase.suspend(credentialsId);

    verify(this.credentialsRepository).save(credentials);
    org.junit.jupiter.api.Assertions.assertFalse(credentials.isActive());
  }

  @Test
  void verifyEmailShouldPersistEmailVerification() {
    UUID credentialsId = UUID.randomUUID();
    Credentials credentials =
        new Credentials(
            credentialsId,
            "a@b.com",
            "+33601020312",
            "hashed",
            new Role(RoleName.CONSUMER, null),
            new Date(),
            null,
            CredentialsStatus.ACTIVE,
            false,
            false);
    when(this.credentialsRepository.findByUserId(credentialsId.toString()))
        .thenReturn(Optional.of(credentials));

    this.useCase.verifyEmail(credentialsId);

    verify(this.credentialsRepository).save(credentials);
    org.junit.jupiter.api.Assertions.assertTrue(credentials.isEmailVerified());
  }

  @Test
  void verifyPhoneShouldPersistPhoneVerification() {
    UUID credentialsId = UUID.randomUUID();
    Credentials credentials =
        new Credentials(
            credentialsId,
            "a@b.com",
            "+33601020313",
            "hashed",
            new Role(RoleName.CONSUMER, null),
            new Date(),
            null,
            CredentialsStatus.ACTIVE,
            false,
            false);
    when(this.credentialsRepository.findByUserId(credentialsId.toString()))
        .thenReturn(Optional.of(credentials));

    this.useCase.verifyPhone(credentialsId);

    verify(this.credentialsRepository).save(credentials);
    org.junit.jupiter.api.Assertions.assertTrue(credentials.isPhoneVerified());
  }

  @Test
  void shouldThrowWhenCredentialsDoesNotExist() {
    UUID credentialsId = UUID.randomUUID();
    when(this.credentialsRepository.findByUserId(credentialsId.toString()))
        .thenReturn(Optional.empty());

    assertThrows(CredentialsNotFoundException.class, () -> this.useCase.activate(credentialsId));
  }
}
