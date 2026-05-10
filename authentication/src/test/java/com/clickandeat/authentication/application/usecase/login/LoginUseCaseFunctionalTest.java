package com.clickandeat.authentication.application.usecase.login;

import static org.junit.Assert.assertThrows;

import com.clickandeat.authentication.application.TokenPair;
import com.clickandeat.authentication.application.exceptions.application.EmailNotFoundException;
import com.clickandeat.authentication.application.exceptions.application.PasswordNotMatchException;
import com.clickandeat.authentication.application.exceptions.application.RoleMismatchException;
import com.clickandeat.authentication.application.usecase.register.RegisterCommand;
import com.clickandeat.authentication.application.usecase.register.RegisterUseCase;
import com.clickandeat.authentication.application.usecase.verification.ICredentialsVerificationUseCase;
import com.clickandeat.authentication.application.usecase.verification.VerifyCredentialsCodeCommand;
import com.clickandeat.authentication.domain.valueobject.Role;
import com.clickandeat.authentication.infrastructure.database.AbstractDatabaseContainersTest;
import com.clickandeat.shared.enums.RoleName;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.redis.core.StringRedisTemplate;

@Tag("functional")
public class LoginUseCaseFunctionalTest extends AbstractDatabaseContainersTest {
  @Autowired LoginUseCase loginUseCase;

  @Autowired RegisterUseCase registerUseCase;

  @Autowired ICredentialsVerificationUseCase credentialsVerificationUseCase;

  @Autowired StringRedisTemplate stringRedisTemplate;

  private String registeredEmail;

  private void createCredentials() {
    String dateString = "2025-05-24";
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    registeredEmail = "test-" + UUID.randomUUID() + "@test.com";
    RegisterCommand command =
        new RegisterCommand(
            registeredEmail,
            "MotDePasseTest06?",
            "john",
            "doe",
            "+336" + UUID.randomUUID().toString().replace("-", "").substring(0, 8),
            LocalDate.parse(dateString, formatter),
            new Role(RoleName.CONSUMER, null));
    UUID credentialsId = this.registerUseCase.execute(command);
    String emailCode =
        this.stringRedisTemplate.opsForValue().get("verification:email:" + credentialsId);
    String phoneCode =
        this.stringRedisTemplate.opsForValue().get("verification:sms:" + credentialsId);
    this.credentialsVerificationUseCase.verifyEmail(
        new VerifyCredentialsCodeCommand(credentialsId, emailCode));
    this.credentialsVerificationUseCase.verifyPhone(
        new VerifyCredentialsCodeCommand(credentialsId, phoneCode));
  }

  @BeforeEach
  void init() {
    this.createCredentials();
  }

  @Test
  void login_shouldThrowUserNotFoundException_whenEmailDoesNotExist() {
    LoginCommand loginCommand = new LoginCommand("not-a-user@email.com", "LePoissonSteve?2");

    assertThrows(
        EmailNotFoundException.class,
        () -> this.loginUseCase.execute(loginCommand, RoleName.CONSUMER));
  }

  @Test
  @Transactional
  void login_shouldThrowPasswordNotMatchException_whenPasswordIsIncorrect() {
    LoginCommand loginCommand = new LoginCommand(registeredEmail, "MauvaisMotDePasse33?");

    assertThrows(
        PasswordNotMatchException.class,
        () -> this.loginUseCase.execute(loginCommand, RoleName.CONSUMER));
  }

  @Test
  void login_shouldThrowUserNotFoundException_whenRoleDoesNotMatch() {
    LoginCommand loginCommand = new LoginCommand(registeredEmail, "MotDePasseTest06?");

    assertThrows(
        RoleMismatchException.class, () -> this.loginUseCase.execute(loginCommand, RoleName.PRO));
  }

  @Test
  @Transactional
  void login_shouldReturnToken_whenCredentialsAreValid() {
    LoginCommand loginCommand = new LoginCommand(registeredEmail, "MotDePasseTest06?");

    TokenPair resultToken = this.loginUseCase.execute(loginCommand, RoleName.CONSUMER);

    Assertions.assertFalse(resultToken.getAccessToken().isBlank());
    Assertions.assertFalse(resultToken.getRefreshToken().isBlank());
  }
}
