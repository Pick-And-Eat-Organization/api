package com.clickandeat.account.infrastructure.repository;

import com.clickandeat.account.domain.account.Account;
import com.clickandeat.account.domain.repository.IAccountRepository;
import com.clickandeat.account.infrastructure.model.AccountEntity;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class AccountRepositoryImpl implements IAccountRepository {
  private final AccountJpaRepository accountJpaRepository;

  public AccountRepositoryImpl(AccountJpaRepository accountJpaRepository) {
    this.accountJpaRepository = accountJpaRepository;
  }

  @Override
  public boolean isPhoneNumberUnique(String phoneNumber) {
    return !this.accountJpaRepository.existsAccountEntityByPhoneNumber(phoneNumber);
  }

  @Override
  public boolean isCredentialsIdUnique(UUID credentialsId) {
    return !this.accountJpaRepository.existsAccountEntityByCredentialsId(credentialsId);
  }

  @Override
  public Account saveAccount(Account account, UUID credentialsId) {
    try {
      return this.accountJpaRepository
          .save(AccountEntity.fromDomain(account, credentialsId))
          .toDomain(null);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
