package com.clickandeat.account.application.usecase.get_current_account;

import com.clickandeat.account.application.exceptions.application.AccountNotFoundException;
import com.clickandeat.account.domain.account.Account;
import com.clickandeat.account.domain.repository.IAccountRepository;
import com.clickandeat.shared.account.GetCurrentCredentialsPort;
import com.clickandeat.shared.account.CurrentAccountResponse;
import com.clickandeat.shared.account.GetCurrentAccountPort;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GetCurrentAccountUseCase implements GetCurrentAccountPort {
  private final IAccountRepository accountRepository;
  private final GetCurrentCredentialsPort getCurrentCredentialsPort;

  public GetCurrentAccountUseCase(
      IAccountRepository accountRepository,
      GetCurrentCredentialsPort getCurrentCredentialsPort) {
    this.accountRepository = accountRepository;
    this.getCurrentCredentialsPort = getCurrentCredentialsPort;
  }

  @Override
  @Transactional(readOnly = true)
  public CurrentAccountResponse getCurrentAccount(UUID credentialsId) {
    Account account =
        this.accountRepository
            .findAccountByCredentialsId(credentialsId)
            .orElseThrow(AccountNotFoundException::new);
    String phoneNumber =
        this.getCurrentCredentialsPort.getCurrentCredentials(credentialsId).phoneNumber();
    return new CurrentAccountResponse(
        account.getId(),
        credentialsId,
        account.getRole(),
        account.getFirstName(),
        account.getLastName(),
        phoneNumber,
        java.time.LocalDate.parse(account.getAccountBirthDate().date()),
        account.getAccountCreatedDate() == null ? null : account.getAccountCreatedDate().toInstant(),
        account.getAccountUpdatedDate() == null ? null : account.getAccountUpdatedDate().toInstant());
  }
}
