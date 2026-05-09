package com.clickandeat.account.infrastructure.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.clickandeat.account.infrastructure.database.AbstractDatabaseContainersTest;
import com.clickandeat.account.infrastructure.model.AccountEntity;
import com.clickandeat.account.domain.account.Account;
import com.clickandeat.shared.enums.RoleName;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Tag("integration")
public class AccountRepositoryImplIntegrationTest extends AbstractDatabaseContainersTest {
  @Autowired private AccountJpaRepository accountJpaRepository;
  @Autowired private AccountRepositoryImpl accountRepository;

  @Test
  public void findAccountByCredentialsId_shouldReturnTheSavedAccount() {
    UUID credentialsId = UUID.randomUUID();
    AccountEntity accountEntity =
        new AccountEntity(
            null,
            "John",
            "Doe",
            credentialsId,
            LocalDate.parse("1995-09-04"),
            Instant.now(),
            null,
            RoleName.PRO);

    this.accountJpaRepository.save(accountEntity);

    Optional<Account> account = this.accountRepository.findAccountByCredentialsId(credentialsId);

    assertTrue(account.isPresent());
    assertEquals("John", account.get().getFirstName());
    assertEquals(RoleName.PRO, account.get().getRole());
  }
}
