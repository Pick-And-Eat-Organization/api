package com.clickandeat.account.application.usecase.create_pro_account;

import com.clickandeat.account.application.service.AccountCreationService;
import com.clickandeat.account.application.usecase.command.CreateGenericAccountCommand;
import com.clickandeat.account.application.usecase.command.CreateProAccountCommand;
import com.clickandeat.account.domain.account.Account;
import com.clickandeat.account.domain.account.pro.AccountProInformations;
import com.clickandeat.account.domain.repository.IProAccountRepository;
import com.clickandeat.shared.account.CreateProAccountPort;
import com.clickandeat.shared.account.CreateProAccountRequest;
import com.clickandeat.shared.enums.RoleName;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateProAccountUseCase implements CreateProAccountPort {
  private final AccountCreationService accountCreationService;
  private final IProAccountRepository proAccountRepository;

  public CreateProAccountUseCase(
      AccountCreationService accountCreationService, IProAccountRepository proAccountRepository) {
    this.accountCreationService = accountCreationService;
    this.proAccountRepository = proAccountRepository;
  }

  @Transactional
  @Override
  public Long createProAccount(UUID credentialsId, CreateProAccountRequest request) {
    CreateProAccountCommand command = toCommand(credentialsId, request);
    Account savedAccount = this.accountCreationService.createAccount(command.baseCommand());
    this.proAccountRepository.saveAccountProInformations(
        convertToDomain(command, savedAccount.getId()));
    return savedAccount.getId();
  }

  private CreateProAccountCommand toCommand(UUID credentialsId, CreateProAccountRequest request) {
    return new CreateProAccountCommand(
        new CreateGenericAccountCommand(
            credentialsId,
            request.firstName(),
            request.lastName(),
            RoleName.PRO,
            request.accountBirthDate().toString()),
        request.kbisRef(),
        request.siret(),
        request.legalName(),
        request.legalForm(),
        request.address1(),
        request.address2(),
        request.address3(),
        request.city(),
        request.postalCode(),
        request.country());
  }

  private AccountProInformations convertToDomain(CreateProAccountCommand command, Long id) {
    return new AccountProInformations(
        id,
        command.kbisRef(),
        command.siret(),
        command.legalName(),
        command.legalForm(),
        command.address1(),
        command.address2(),
        command.address3(),
        command.city(),
        command.postalCode(),
        command.country());
  }
}
