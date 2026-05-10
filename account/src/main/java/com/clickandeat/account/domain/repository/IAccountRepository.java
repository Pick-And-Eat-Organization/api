package com.clickandeat.account.domain.repository;

import com.clickandeat.account.domain.account.Account;
import java.util.Optional;
import java.util.UUID;

public interface IAccountRepository {
  boolean isCredentialsIdUnique(UUID credentialsId);

  Account saveAccount(Account account, UUID credentialsId);

  Optional<Account> findAccountByCredentialsId(UUID credentialsId);
}
