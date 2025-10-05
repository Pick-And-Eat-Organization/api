package com.clickandeat.account.application.usecase.create_generic_account;

import com.clickandeat.account.application.exceptions.application.ExistingAccountForCredentialsIdException;
import com.clickandeat.account.application.exceptions.application.PhoneNumberAlreadyUsedException;
import com.clickandeat.account.application.exceptions.technical.DatabaseTechnicalException;
import com.clickandeat.account.application.usecase.command.CreateGenericAccountCommand;
import com.clickandeat.account.domain.account.Account;
import com.clickandeat.account.domain.repository.IAccountRepository;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class CreateGenericAccountUseCase {
  private final IAccountRepository accountRepository;

  public CreateGenericAccountUseCase(IAccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  public Long execute(CreateGenericAccountCommand command) {
    this.doesCredentialsIdExist(command.credentialsId());
    this.doesPhoneNumberExist(command.accountPhoneNumber());
    Account account = this.convertCommandToAccountDomain(command);
    Account savedAccount = this.insertAccount(account, command.credentialsId());
    return savedAccount.getId();
  }

  private void doesCredentialsIdExist(UUID credentialsId) {
    if (!this.accountRepository.isCredentialsIdUnique(credentialsId)) {
      throw new ExistingAccountForCredentialsIdException();
    }
  }

  private void doesPhoneNumberExist(String phoneNumber) {
    if (!this.accountRepository.isPhoneNumberUnique(phoneNumber)) {
      throw new PhoneNumberAlreadyUsedException();
    }
  }

  private Account convertCommandToAccountDomain(CreateGenericAccountCommand command) {
    return new Account(
        null,
        command.lastName(),
        command.firstName(),
        command.roleName(),
        command.accountPhoneNumber(),
        null,
        command.accountBirthDate(),
        Date.from(Instant.now()),
        null,
        null);
  }

  private Account insertAccount(Account account, UUID credentialsId) {
    try {
      return this.accountRepository.saveAccount(account, credentialsId);
    } catch (Exception e) {
      throw new DatabaseTechnicalException(e.getMessage(), e.getCause());
    }
  }
}
