package com.clickandeat.account.application.usecase.create_generic_account;

import com.clickandeat.account.application.service.AccountCreationService;
import com.clickandeat.account.application.usecase.command.CreateGenericAccountCommand;
import com.clickandeat.account.domain.account.Account;
import com.clickandeat.shared.account.CreateGenericAccountPort;
import com.clickandeat.shared.account.CreateGenericAccountRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateGenericAccountUseCase implements CreateGenericAccountPort {
  private final AccountCreationService accountCreationService;

  public CreateGenericAccountUseCase(AccountCreationService accountCreationService) {
    this.accountCreationService = accountCreationService;
  }

  @Override
  @Transactional
  public Long createGenericAccount(CreateGenericAccountRequest request) {
    CreateGenericAccountCommand command =
        new CreateGenericAccountCommand(
            request.credentialsId(),
            request.firstName(),
            request.lastName(),
            request.roleName(),
            request.accountBirthDate().toString());
    Account savedAccount = this.accountCreationService.createAccount(command);
    return savedAccount.getId();
  }
}
