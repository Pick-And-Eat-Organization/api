package com.clickandeat.account.application.usecase.create_generic_account;

import com.clickandeat.account.application.service.AccountCreationService;
import com.clickandeat.account.application.usecase.command.CreateGenericAccountCommand;
import com.clickandeat.account.domain.account.Account;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateGenericAccountUseCase {
  private final AccountCreationService accountCreationService;

  public CreateGenericAccountUseCase(AccountCreationService accountCreationService) {
    this.accountCreationService = accountCreationService;
  }

  @Transactional
  public Long execute(CreateGenericAccountCommand command) {
    Account savedAccount = this.accountCreationService.createAccount(command);
    return savedAccount.getId();
  }

}
