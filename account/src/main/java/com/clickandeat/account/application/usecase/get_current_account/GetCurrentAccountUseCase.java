package com.clickandeat.account.application.usecase.get_current_account;

import com.clickandeat.account.application.exceptions.application.AccountNotFoundException;
import com.clickandeat.account.domain.account.Account;
import com.clickandeat.account.domain.account.pro.AccountProInformations;
import com.clickandeat.account.domain.repository.IAccountRepository;
import com.clickandeat.shared.account.AccountProInformationsResponse;
import com.clickandeat.shared.account.CurrentAccountResponse;
import com.clickandeat.shared.account.GetCurrentAccountPort;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GetCurrentAccountUseCase implements GetCurrentAccountPort {
  private final IAccountRepository accountRepository;

  public GetCurrentAccountUseCase(IAccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  @Override
  @Transactional(readOnly = true)
  public CurrentAccountResponse getCurrentAccount(UUID credentialsId) {
    Account account =
        this.accountRepository
            .findAccountByCredentialsId(credentialsId)
            .orElseThrow(AccountNotFoundException::new);
    return this.toResponse(account, credentialsId);
  }

  private CurrentAccountResponse toResponse(Account account, UUID credentialsId) {
    return new CurrentAccountResponse(
        account.getId(),
        credentialsId,
        account.getRole(),
        account.getFirstName(),
        account.getLastName(),
        account.getAccountPhoneNumber().phoneNumber(),
        LocalDate.parse(account.getAccountBirthDate().date()),
        toInstant(account.getAccountCreatedDate()),
        toInstant(account.getAccountUpdatedDate()),
        toProInformationsResponse(account.getAccountProInformations()));
  }

  private Instant toInstant(Date date) {
    return date == null ? null : date.toInstant();
  }

  private AccountProInformationsResponse toProInformationsResponse(
      AccountProInformations proInformations) {
    if (proInformations == null) {
      return null;
    }
    return new AccountProInformationsResponse(
        proInformations.getKbis_ref(),
        proInformations.getSiret(),
        proInformations.getLegal_name(),
        proInformations.getLegal_form(),
        proInformations.getLocalisation().address1(),
        proInformations.getLocalisation().address2(),
        proInformations.getLocalisation().address3(),
        proInformations.getLocalisation().city(),
        proInformations.getLocalisation().postalCode(),
        proInformations.getLocalisation().country());
  }
}
