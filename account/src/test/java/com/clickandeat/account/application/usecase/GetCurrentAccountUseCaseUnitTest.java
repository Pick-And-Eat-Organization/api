package com.clickandeat.account.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.clickandeat.account.application.exceptions.application.AccountNotFoundException;
import com.clickandeat.account.application.usecase.get_current_account.GetCurrentAccountUseCase;
import com.clickandeat.account.domain.account.Account;
import com.clickandeat.account.domain.repository.IAccountRepository;
import com.clickandeat.shared.account.CurrentAccountResponse;
import com.clickandeat.shared.account.CurrentCredentialsResponse;
import com.clickandeat.shared.account.GetCurrentCredentialsPort;
import com.clickandeat.shared.enums.RoleName;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class GetCurrentAccountUseCaseUnitTest {

  @Test
  void shouldReturnCurrentAccountResponse() {
    IAccountRepository accountRepository = mock(IAccountRepository.class);
    GetCurrentCredentialsPort credentialsPort = mock(GetCurrentCredentialsPort.class);
    GetCurrentAccountUseCase useCase =
        new GetCurrentAccountUseCase(accountRepository, credentialsPort);
    UUID credentialsId = UUID.randomUUID();
    Account account =
        new Account(
            42L,
            "Doe",
            "John",
            RoleName.PRO,
            null,
            "1995-01-01",
            Date.from(Instant.parse("2025-01-01T12:00:00Z")),
            Date.from(Instant.parse("2025-01-02T12:00:00Z")),
            null);

    when(accountRepository.findAccountByCredentialsId(credentialsId))
        .thenReturn(Optional.of(account));
    when(credentialsPort.getCurrentCredentials(credentialsId))
        .thenReturn(new CurrentCredentialsResponse("+33601020304"));

    CurrentAccountResponse response = useCase.getCurrentAccount(credentialsId);

    assertEquals(42L, response.accountId());
    assertEquals(credentialsId, response.credentialsId());
    assertEquals(RoleName.PRO, response.role());
    assertEquals("John", response.firstName());
    assertEquals("Doe", response.lastName());
    assertEquals("+33601020304", response.phoneNumber());
  }

  @Test
  void shouldThrowWhenAccountDoesNotExist() {
    IAccountRepository accountRepository = mock(IAccountRepository.class);
    GetCurrentCredentialsPort credentialsPort = mock(GetCurrentCredentialsPort.class);
    GetCurrentAccountUseCase useCase =
        new GetCurrentAccountUseCase(accountRepository, credentialsPort);
    UUID credentialsId = UUID.randomUUID();

    when(accountRepository.findAccountByCredentialsId(credentialsId)).thenReturn(Optional.empty());

    assertThrows(AccountNotFoundException.class, () -> useCase.getCurrentAccount(credentialsId));
  }
}
