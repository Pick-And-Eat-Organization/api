package com.clickandeat.account.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.clickandeat.account.application.service.AccountCreationService;
import com.clickandeat.account.application.usecase.create_pro_account.CreateProAccountUseCase;
import com.clickandeat.account.domain.account.Account;
import com.clickandeat.account.domain.account.pro.AccountProInformations;
import com.clickandeat.account.domain.repository.IAccountRepository;
import com.clickandeat.account.domain.repository.IProAccountRepository;
import com.clickandeat.shared.account.CreateProAccountRequest;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

@Tag("unit")
public class CreateProAccountUseCaseUnitTest {
  private CreateProAccountUseCase createProAccountUseCase;
  private IAccountRepository accountRepository;
  private IProAccountRepository proAccountRepository;

  @BeforeEach
  public void setUp() {
    this.accountRepository = Mockito.mock(IAccountRepository.class);
    this.proAccountRepository = Mockito.mock(IProAccountRepository.class);
    AccountCreationService accountCreationService =
        new AccountCreationService(this.accountRepository);
    this.createProAccountUseCase =
        new CreateProAccountUseCase(accountCreationService, this.proAccountRepository);
  }

  @Test
  public void createProAccount_shouldPersistAccountAndProInformations() {
    UUID credentialsId = UUID.randomUUID();
    CreateProAccountRequest request =
        new CreateProAccountRequest(
            "John",
            "Doe",
            LocalDate.of(1995, 1, 1),
            "kbis",
            "12345678901234",
            "Legal name",
            "SARL",
            "addr1",
            null,
            null,
            "Paris",
            "75000",
            "France");

    Account persistedAccount =
        new Account(
            1L,
            "Doe",
            "John",
            com.clickandeat.shared.enums.RoleName.PRO,
            null,
            "1995-01-01",
            Date.from(Instant.parse("2024-01-01T00:00:00Z")),
            null,
            null);

    when(this.accountRepository.isCredentialsIdUnique(credentialsId)).thenReturn(true);
    when(this.accountRepository.saveAccount(any(Account.class), eq(credentialsId)))
        .thenReturn(persistedAccount);
    when(this.proAccountRepository.saveAccountProInformations(any(AccountProInformations.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    Long result = this.createProAccountUseCase.createProAccount(credentialsId, request);

    assertEquals(1L, result);
  }
}
