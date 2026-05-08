package com.clickandeat.account.application.usecase.create_pro_account;

import com.clickandeat.account.application.service.AccountCreationService;
import com.clickandeat.account.application.usecase.command.CreateProAccountCommand;
import com.clickandeat.account.domain.account.Account;
import com.clickandeat.account.domain.account.pro.AccountProInformations;
import com.clickandeat.account.domain.repository.IProAccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateProAccountUseCase {
  private final AccountCreationService accountCreationService;
  private final IProAccountRepository proAccountRepository;

  public CreateProAccountUseCase(
      AccountCreationService accountCreationService, IProAccountRepository proAccountRepository) {
    this.accountCreationService = accountCreationService;
    this.proAccountRepository = proAccountRepository;
  }
  @Transactional
  public void execute(CreateProAccountCommand command) {
    Account savedAccount = this.accountCreationService.createAccount(command.baseCommand());
    this.proAccountRepository.saveAccountProInformations(
        convertCommandToAccountProInformations(command, savedAccount.getId()));
  }

  private AccountProInformations convertCommandToAccountProInformations(
      CreateProAccountCommand command, Long id) {
    return new AccountProInformations(
        id,
        command.kbis_ref(),
        command.siret(),
        command.legal_name(),
        command.legal_form(),
        command.address1(),
        command.address2(),
        command.address3(),
        command.city(),
        command.postalCode(),
        command.country());
  }
}
