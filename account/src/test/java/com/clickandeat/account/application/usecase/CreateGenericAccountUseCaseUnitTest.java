package com.clickandeat.account.application.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.clickandeat.account.application.exceptions.application.ExistingAccountForCredentialsIdException;
import com.clickandeat.account.application.exceptions.application.PhoneNumberAlreadyUsedException;
import com.clickandeat.account.application.service.AccountCreationService;
import com.clickandeat.account.application.usecase.command.CreateGenericAccountCommand;
import com.clickandeat.account.application.usecase.create_generic_account.CreateGenericAccountUseCase;
import com.clickandeat.account.domain.account.Account;
import com.clickandeat.account.domain.repository.IAccountRepository;
import com.clickandeat.shared.enums.RoleName;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

@Tag("unit")
public class CreateGenericAccountUseCaseUnitTest {
  private CreateGenericAccountUseCase createGenericProfilUseCase;
  private IAccountRepository accountRepository;

  @BeforeEach
  public void setUp() {
    this.accountRepository = Mockito.mock(IAccountRepository.class);
    AccountCreationService accountCreationService =
        new AccountCreationService(this.accountRepository);
    this.createGenericProfilUseCase = new CreateGenericAccountUseCase(accountCreationService);
  }

  @Test
  public void createGenericAccount_shouldFailIfAccountAlreadyExists() {
    UUID credentialsId = UUID.randomUUID();
    CreateGenericAccountCommand command =
        new CreateGenericAccountCommand(
            credentialsId, "John", "Doe", RoleName.CONSUMER, "+33640404040", "1995-01-01");
    when(this.accountRepository.isCredentialsIdUnique(credentialsId)).thenReturn(false);
    assertThrows(
        ExistingAccountForCredentialsIdException.class,
        () -> this.createGenericProfilUseCase.execute(command));
  }

  @Test
  public void createGenericAccount_shouldFailIfPhoneNumberIsAlreadyUsed() {
    UUID credentialsId = UUID.randomUUID();
    CreateGenericAccountCommand command =
        new CreateGenericAccountCommand(
            credentialsId, "John", "Doe", RoleName.CONSUMER, "+33640404040", "1995-01-01");
    when(this.accountRepository.isCredentialsIdUnique(credentialsId)).thenReturn(true);
    when(this.accountRepository.isPhoneNumberUnique(command.accountPhoneNumber()))
        .thenReturn(false);
    assertThrows(
        PhoneNumberAlreadyUsedException.class,
        () -> this.createGenericProfilUseCase.execute(command));
  }

  @Test
  public void createGenericAccount_shouldCreateAccount() {
    UUID credentialsId = UUID.randomUUID();
    CreateGenericAccountCommand command =
        new CreateGenericAccountCommand(
            credentialsId, "John", "Doe", RoleName.CONSUMER, "+33640404040", "1995-01-01");
    Account persisted =
        new Account(
            1L,
            command.lastName(),
            command.firstName(),
            command.roleName(),
            command.accountPhoneNumber(),
            null,
            command.accountBirthDate(),
            Date.from(Instant.parse("2024-01-01T00:00:00Z")),
            null,
            null);
    when(this.accountRepository.isCredentialsIdUnique(credentialsId)).thenReturn(true);
    when(this.accountRepository.isPhoneNumberUnique(command.accountPhoneNumber())).thenReturn(true);
    when(accountRepository.saveAccount(any(Account.class), eq(credentialsId)))
        .thenReturn(persisted);
    Long result = this.createGenericProfilUseCase.execute(command);
    assertEquals(1L, result);
  }
}
