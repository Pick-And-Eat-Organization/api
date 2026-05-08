package com.clickandeat.account.application.usecase;

import com.clickandeat.account.application.service.AccountCreationService;
import com.clickandeat.account.application.usecase.command.CreateGenericAccountCommand;
import com.clickandeat.account.application.usecase.create_generic_account.CreateGenericAccountUseCase;
import com.clickandeat.account.application.usecase.create_pro_account.CreateProAccountUseCase;
import com.clickandeat.account.domain.repository.IAccountRepository;
import com.clickandeat.account.domain.repository.IProAccountRepository;
import com.clickandeat.shared.enums.RoleName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.UUID;

@Tag("unit")
public class CreateProAccountUseCaseUnitTest {

    private CreateProAccountUseCase createProAccountUseCase;
    private IProAccountRepository proAccountRepository;
    private IAccountRepository accountRepository;

    @BeforeEach
    public void setUp() {
        this.accountRepository = Mockito.mock(IAccountRepository.class);
        this.proAccountRepository = Mockito.mock(IProAccountRepository.class);
        AccountCreationService accountCreationService =
                new AccountCreationService(this.accountRepository);
        this.createProAccountUseCase = new CreateProAccountUseCase(accountCreationService, this.proAccountRepository);
    }

    @Test
    public void createProAccount() {
        CreateGenericAccountCommand createGenericAccountCommand = new CreateGenericAccountCommand(
                UUID.fromString("9c295f77-bdec-4e76-85f4-4b5db06c2381"),
                "John", "Doe", RoleName.PRO, "+33640404040", "1995-01-01"
        );
    }

//    public record CreateProAccountCommand(
//            CreateGenericAccountCommand baseCommand,
//            String kbis_ref,
//            String siret,
//            String legal_name,
//            String legal_form,
//            String address1,
//            String address2,
//            String address3,
//            String city,
//            String postalCode,
//            String country) {}

}
