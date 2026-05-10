package com.clickandeat.account.infrastructure.repository;

import com.clickandeat.account.infrastructure.model.AccountEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountJpaRepository extends JpaRepository<AccountEntity, Long> {
  boolean existsAccountEntityByCredentialsId(UUID credentialsId);

  Optional<AccountEntity> findByCredentialsId(UUID credentialsId);
}
