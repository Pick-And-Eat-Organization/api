package com.clickandeat.account.domain.repository;

import com.clickandeat.account.domain.account.Account;
import java.util.UUID;

public interface IAccountRepository {
  boolean isPhoneNumberUnique(String phoneNumber);

  boolean isCredentialsIdUnique(UUID credentialsId);

  Account saveAccount(Account account, UUID credentialsId);
}
