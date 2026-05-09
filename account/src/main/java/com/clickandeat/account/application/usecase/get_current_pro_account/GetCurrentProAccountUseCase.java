package com.clickandeat.account.application.usecase.get_current_pro_account;

import com.clickandeat.account.application.exceptions.application.AccountNotFoundException;
import com.clickandeat.account.domain.account.Account;
import com.clickandeat.account.domain.account.pro.AccountProInformations;
import com.clickandeat.account.domain.repository.IAccountRepository;
import com.clickandeat.shared.account.AccountProInformationsResponse;
import com.clickandeat.shared.account.CurrentProAccountResponse;
import com.clickandeat.shared.account.GetCurrentProAccountPort;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GetCurrentProAccountUseCase implements GetCurrentProAccountPort {
  private final IAccountRepository accountRepository;

  public GetCurrentProAccountUseCase(IAccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  @Override
  @Transactional(readOnly = true)
  public CurrentProAccountResponse getCurrentProAccount(UUID credentialsId) {
    Account account =
        this.accountRepository
            .findAccountByCredentialsId(credentialsId)
            .orElseThrow(AccountNotFoundException::new);
    return new CurrentProAccountResponse(
        account.getId(),
        credentialsId,
        account.getRole(),
        account.getFirstName(),
        account.getLastName(),
        account.getAccountPhoneNumber().phoneNumber(),
        java.time.LocalDate.parse(account.getAccountBirthDate().date()),
        account.getAccountCreatedDate() == null ? null : account.getAccountCreatedDate().toInstant(),
        account.getAccountUpdatedDate() == null ? null : account.getAccountUpdatedDate().toInstant(),
        toProInformationsResponse(account.getAccountProInformations()));
  }

  private AccountProInformationsResponse toProInformationsResponse(
      AccountProInformations proInformations) {
    if (proInformations == null) {
      throw new AccountNotFoundException();
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
