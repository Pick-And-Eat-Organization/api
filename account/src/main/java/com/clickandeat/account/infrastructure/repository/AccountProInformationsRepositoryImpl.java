package com.clickandeat.account.infrastructure.repository;

import com.clickandeat.account.domain.account.pro.AccountProInformations;
import com.clickandeat.account.domain.repository.IProAccountRepository;
import com.clickandeat.account.infrastructure.model.AccountEntity;
import com.clickandeat.account.infrastructure.model.AccountProInformationsEntity;
import org.springframework.stereotype.Component;

@Component
public class AccountProInformationsRepositoryImpl implements IProAccountRepository {

  private final AccountJpaRepository accountJpaRepository;
  private final AccountProInformationsJpaRepository accountProInformationsJpaRepository;

  public AccountProInformationsRepositoryImpl(
      AccountJpaRepository accountJpaRepository,
      AccountProInformationsJpaRepository accountProInformationsJpaRepository) {
    this.accountJpaRepository = accountJpaRepository;
    this.accountProInformationsJpaRepository = accountProInformationsJpaRepository;
  }

  @Override
  public AccountProInformations saveAccountProInformations(
      AccountProInformations accountProInformations) {
    AccountEntity accountEntity =
        this.accountJpaRepository.getReferenceById(accountProInformations.getId());
    AccountProInformationsEntity entity =
        AccountProInformationsEntity.fromDomain(accountProInformations, accountEntity);
    return this.accountProInformationsJpaRepository.save(entity).toDomain();
  }
}
