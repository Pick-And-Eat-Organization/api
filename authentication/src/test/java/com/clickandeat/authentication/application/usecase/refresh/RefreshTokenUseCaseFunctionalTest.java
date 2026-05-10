package com.clickandeat.authentication.application.usecase.refresh;

import com.clickandeat.authentication.application.ITokenRepository;
import com.clickandeat.authentication.application.TokenPair;
import com.clickandeat.authentication.application.exceptions.application.InvalidTokenException;
import com.clickandeat.authentication.application.exceptions.application.JtiNotFoundInCacheException;
import com.clickandeat.authentication.application.exceptions.application.UserNotFoundException;
import com.clickandeat.authentication.application.usecase.login.LoginCommand;
import com.clickandeat.authentication.application.usecase.login.LoginUseCase;
import com.clickandeat.authentication.application.usecase.refresh_token.RefreshTokenUseCase;
import com.clickandeat.authentication.application.usecase.register.RegisterCommand;
import com.clickandeat.authentication.application.usecase.register.RegisterUseCase;
import com.clickandeat.authentication.application.usecase.verification.ICredentialsVerificationUseCase;
import com.clickandeat.authentication.application.usecase.verification.VerifyCredentialsCodeCommand;
import com.clickandeat.authentication.domain.valueobject.Role;
import com.clickandeat.authentication.infrastructure.database.AbstractDatabaseContainersTest;
import com.clickandeat.shared.enums.RoleName;
import com.clickandeat.shared.token.TokenService;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Tag("functional")
public class RefreshTokenUseCaseFunctionalTest extends AbstractDatabaseContainersTest {

  @Autowired LoginUseCase loginUseCase;

  @Autowired RegisterUseCase registerUseCase;

  @Autowired ICredentialsVerificationUseCase credentialsVerificationUseCase;

  @Autowired RefreshTokenUseCase refreshUseCase;

  @Autowired TokenService tokenService;

  @Autowired ITokenRepository tokenRepository;

  @Autowired StringRedisTemplate stringRedisTemplate;

  TokenPair token;

  public TokenPair createCredentials() {
    String dateString = "2025-05-24";
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    String email = "test-refresh-" + UUID.randomUUID() + "@test.com";
    RegisterCommand command =
        new RegisterCommand(
            email,
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
    return this.loginUseCase.execute(
        new LoginCommand(command.email(), command.password()), RoleName.CONSUMER);
  }

  @BeforeEach
  void init() {
    this.token = this.createCredentials();
  }

  @Test
  void refreshAccessToken_shouldThrowInvalidTokenException_whenTokenIsMalformed() {
    Assertions.assertThrows(
        InvalidTokenException.class, () -> this.refreshUseCase.execute("invalid-token"));
  }

  @Test
  void refreshAccessToken_shouldThrowJtiNotFoundInCacheException_whenJtiMissing() {
    String refreshToken =
        this.tokenService.createRefreshToken(UUID.randomUUID(), "CONSUMER", Duration.ofDays(1));
    Assertions.assertThrows(
        JtiNotFoundInCacheException.class, () -> this.refreshUseCase.execute(refreshToken));
  }

  @Test
  void refreshAccessToken_shouldThrowInvalidUserIdInRefreshTokenException_whenUserIdDoesNotMatch() {
    UUID expectedUserId = UUID.randomUUID();
    String refreshToken =
        this.tokenService.createRefreshToken(expectedUserId, "CONSUMER", Duration.ofDays(3));
    String expectedJti = this.tokenService.extractJti(refreshToken);
    this.tokenRepository.storeRefreshToken(
        expectedJti, expectedUserId.toString(), Duration.ofDays(3));

    Assertions.assertThrows(
        UserNotFoundException.class, () -> this.refreshUseCase.execute(refreshToken));
  }

  @Test
  void refreshAccessToken_shouldReturnNewTokenPair_whenTokenIsValid() {
    String oldRefreshToken = this.token.getRefreshToken();

    TokenPair newTokens = this.refreshUseCase.execute(oldRefreshToken);

    Assertions.assertNotNull(newTokens);
    Assertions.assertNotNull(newTokens.getAccessToken());
    Assertions.assertNotNull(newTokens.getRefreshToken());
    Assertions.assertNotEquals(oldRefreshToken, newTokens.getRefreshToken());
    Assertions.assertNotEquals(this.token.getAccessToken(), newTokens.getAccessToken());
  }

  @Test
  void refreshAccessToken_shouldSupportRotationMultipleTimes() {
    String firstRefresh = token.getRefreshToken();

    TokenPair firstRotation = refreshUseCase.execute(firstRefresh);
    TokenPair secondRotation = refreshUseCase.execute(firstRotation.getRefreshToken());

    Assertions.assertNotEquals(firstRotation.getAccessToken(), secondRotation.getAccessToken());
    Assertions.assertNotEquals(firstRotation.getRefreshToken(), secondRotation.getRefreshToken());
  }
}
