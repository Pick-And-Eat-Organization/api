package com.clickandeat.account.infrastructure.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
  public void isPhoneNumberUnique_shouldReturnFalseIfItExistsOrTrueOtherwise() {
    String usedPhoneNumber = uniquePhoneNumber();
    AccountEntity accountEntity =
        new AccountEntity(
            null,
            "John",
            "Doe",
            UUID.randomUUID(),
            LocalDate.parse("1995-09-04"),
            usedPhoneNumber,
            Instant.now(),
            null,
            RoleName.CONSUMER);

    this.accountJpaRepository.save(accountEntity);

    String falsePhoneNumber = "+33640404040";

    assertFalse(this.accountRepository.isPhoneNumberUnique(usedPhoneNumber));
    assertTrue(this.accountRepository.isPhoneNumberUnique(falsePhoneNumber));
  }

  @Test
  public void findAccountByCredentialsId_shouldReturnTheSavedAccount() {
    UUID credentialsId = UUID.randomUUID();
    String phoneNumber = uniquePhoneNumber();
    AccountEntity accountEntity =
        new AccountEntity(
            null,
            "John",
            "Doe",
            credentialsId,
            LocalDate.parse("1995-09-04"),
            phoneNumber,
            Instant.now(),
            null,
            RoleName.PRO);

    this.accountJpaRepository.save(accountEntity);

    Optional<Account> account = this.accountRepository.findAccountByCredentialsId(credentialsId);

    assertTrue(account.isPresent());
    assertEquals("John", account.get().getFirstName());
    assertEquals(RoleName.PRO, account.get().getRole());
  }

  private String uniquePhoneNumber() {
    int suffix = Math.floorMod(UUID.randomUUID().hashCode(), 100000000);
    return String.format("+336%08d", suffix);
  }
}
