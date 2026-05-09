package com.clickandeat.account.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.clickandeat.account.application.exceptions.application.AccountNotFoundException;
import com.clickandeat.account.application.usecase.get_current_account.GetCurrentAccountUseCase;
import com.clickandeat.account.domain.account.Account;
import com.clickandeat.account.domain.account.pro.AccountProInformations;
import com.clickandeat.account.domain.repository.IAccountRepository;
import com.clickandeat.shared.account.CurrentAccountResponse;
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
    GetCurrentAccountUseCase useCase = new GetCurrentAccountUseCase(accountRepository);
    UUID credentialsId = UUID.randomUUID();
    Account account =
        new Account(
            42L,
            "Doe",
            "John",
            RoleName.PRO,
            "+33601020304",
            null,
            "1995-01-01",
            Date.from(Instant.parse("2025-01-01T12:00:00Z")),
            Date.from(Instant.parse("2025-01-02T12:00:00Z")),
            new AccountProInformations(
                42L,
                "KBIS-2025-001",
                "12345678900011",
                "Click and Eat",
                "SAS",
                "1 rue de Paris",
                "Batiment A",
                "Etage 1",
                "Paris",
                "75001",
                "France"));

    when(accountRepository.findAccountByCredentialsId(credentialsId)).thenReturn(Optional.of(account));

    CurrentAccountResponse response = useCase.getCurrentAccount(credentialsId);

    assertEquals(42L, response.accountId());
    assertEquals(credentialsId, response.credentialsId());
    assertEquals(RoleName.PRO, response.role());
    assertEquals("John", response.firstName());
    assertEquals("KBIS-2025-001", response.proInformations().kbisRef());
    assertEquals("France", response.proInformations().country());
  }

  @Test
  void shouldThrowWhenAccountDoesNotExist() {
    IAccountRepository accountRepository = mock(IAccountRepository.class);
    GetCurrentAccountUseCase useCase = new GetCurrentAccountUseCase(accountRepository);
    UUID credentialsId = UUID.randomUUID();

    when(accountRepository.findAccountByCredentialsId(credentialsId)).thenReturn(Optional.empty());

    assertThrows(AccountNotFoundException.class, () -> useCase.getCurrentAccount(credentialsId));
  }
}
