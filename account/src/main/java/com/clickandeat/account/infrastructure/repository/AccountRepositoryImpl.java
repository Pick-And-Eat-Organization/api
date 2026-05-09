package com.clickandeat.account.infrastructure.repository;

import com.clickandeat.account.domain.account.Account;
import com.clickandeat.account.domain.repository.IAccountRepository;
import com.clickandeat.account.infrastructure.model.AccountEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class AccountRepositoryImpl implements IAccountRepository {
  private final AccountJpaRepository accountJpaRepository;

  public AccountRepositoryImpl(AccountJpaRepository accountJpaRepository) {
    this.accountJpaRepository = accountJpaRepository;
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

  @Override
  public Optional<Account> findAccountByCredentialsId(UUID credentialsId) {
    return this.accountJpaRepository.findByCredentialsId(credentialsId).map(entity -> entity.toDomain(null));
  }
}
