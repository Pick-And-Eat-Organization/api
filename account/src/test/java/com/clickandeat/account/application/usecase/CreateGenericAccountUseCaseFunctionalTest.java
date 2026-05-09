package com.clickandeat.account.application.usecase;

import static org.junit.jupiter.api.Assertions.*;

import com.clickandeat.account.application.exceptions.application.ExistingAccountForCredentialsIdException;
import com.clickandeat.account.application.exceptions.application.PhoneNumberAlreadyUsedException;
import com.clickandeat.account.application.usecase.command.CreateGenericAccountCommand;
import com.clickandeat.account.application.usecase.create_generic_account.CreateGenericAccountUseCase;
import com.clickandeat.account.infrastructure.database.AbstractDatabaseContainersTest;
import com.clickandeat.shared.account.CreateGenericAccountRequest;
import com.clickandeat.shared.enums.RoleName;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Tag("functional")
public class CreateGenericAccountUseCaseFunctionalTest extends AbstractDatabaseContainersTest {
  @Autowired private CreateGenericAccountUseCase createGenericAccountUseCase;
  private static final UUID FIXED_CREDENTIALS_ID =
      UUID.fromString("11111111-1111-1111-1111-111111111111");
  private static final String FIXED_PHONE_NUMBER = "+33640456534";

  private CreateGenericAccountCommand getCommand(UUID credentialsId, String phoneNumber) {
    return new CreateGenericAccountCommand(
        credentialsId, "John", "Doe", RoleName.CONSUMER, phoneNumber, "1995-01-01");
  }

  private CreateGenericAccountRequest toRequest(CreateGenericAccountCommand command) {
    return new CreateGenericAccountRequest(
        command.credentialsId(),
        command.firstName(),
        command.lastName(),
        command.roleName(),
        command.accountPhoneNumber(),
        LocalDate.parse(command.accountBirthDate()));
  }

  void saveGenericAccount() {
    this.createGenericAccountUseCase.createGenericAccount(
        toRequest(getCommand(FIXED_CREDENTIALS_ID, FIXED_PHONE_NUMBER)));
  }

  @BeforeAll
  public void init() {
    this.saveGenericAccount();
  }

  @Test
  void createGenericAccount_shouldFailIfAccountAlreadyExists() {
    assertThrows(
        ExistingAccountForCredentialsIdException.class,
        () ->
            this.createGenericAccountUseCase.createGenericAccount(
                toRequest(getCommand(FIXED_CREDENTIALS_ID, FIXED_PHONE_NUMBER))));
  }

  @Test
  void createGenericAccount_shouldFailIfPhoneNumberIsAlreadyUsed() {
    assertThrows(
        PhoneNumberAlreadyUsedException.class,
        () ->
            this.createGenericAccountUseCase.createGenericAccount(
                toRequest(getCommand(UUID.randomUUID(), FIXED_PHONE_NUMBER))));
  }

  @Test
  void createGenericAccount_shouldCreateAccount() {
    Long result =
        this.createGenericAccountUseCase.createGenericAccount(
            toRequest(getCommand(UUID.randomUUID(), "+6690909090")));
    assertNotNull(result);
  }
}
