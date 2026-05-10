package com.clickandeat.authentication.application.usecase.register;

import static org.junit.jupiter.api.Assertions.*;

import com.clickandeat.authentication.application.exceptions.application.EmailAlreadyUsedException;
import com.clickandeat.authentication.domain.valueobject.Role;
import com.clickandeat.authentication.infrastructure.database.AbstractDatabaseContainersTest;
import com.clickandeat.shared.enums.RoleName;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.redis.core.StringRedisTemplate;

@Tag("functional")
@Transactional
public class RegisterUseCaseFunctionalTest extends AbstractDatabaseContainersTest {
  @Autowired private RegisterUseCase registerUseCase;
  @Autowired private JdbcTemplate jdbcTemplate;
  @Autowired private StringRedisTemplate stringRedisTemplate;

  private String uniqueEmail(String prefix) {
    return prefix + "-" + UUID.randomUUID() + "@example.com";
  }

  private String uniquePhoneNumber() {
    return "+336" + String.format("%08d", ThreadLocalRandom.current().nextInt(0, 100_000_000));
  }

  private RegisterCommand getCommand(String email) {
    return getCommand(email, uniquePhoneNumber());
  }

  private RegisterCommand getCommand(String email, String phoneNumber) {
    String dateString = "2025-05-24";
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    return new RegisterCommand(
        email,
        "clearPassword",
        "jerome",
        "juda",
        phoneNumber,
        LocalDate.parse(dateString, formatter),
        new Role(RoleName.CONSUMER, null));
  }

  @Test
  void register_shouldSucceed_whenEmailIsUnique() {
    RegisterCommand command = getCommand(uniqueEmail("unique-user"));
    UUID result = registerUseCase.execute(command);
    assertNotNull(result, "Le UUID retourné ne doit pas être null");
    Integer count =
        jdbcTemplate.queryForObject(
            "select count(*) from account where credentials_id = ?", Integer.class, result);
    assertNotNull(count);
    assertEquals(1, count);
    Integer statusCount =
        jdbcTemplate.queryForObject(
            "select count(*) from credentials where credentials_id = ? and status = 'PENDING'",
            Integer.class,
            result);
    assertEquals(1, statusCount);
    String phoneNumber =
        jdbcTemplate.queryForObject(
            "select phone_number from credentials where credentials_id = ?", String.class, result);
    assertEquals(command.phoneNumber(), phoneNumber);
    assertNotNull(
        this.stringRedisTemplate.opsForValue().get("verification:email:" + result));
    assertNotNull(
        this.stringRedisTemplate.opsForValue().get("verification:sms:" + result));
  }

  @Test
  void register_shouldThrowEmailAlreadyUsedException_whenEmailAlreadyExists() {
    String email = uniqueEmail("duplicate");
    RegisterCommand first = getCommand(email);
    registerUseCase.execute(first);

    RegisterCommand second = getCommand(email);
    assertThrows(EmailAlreadyUsedException.class, () -> registerUseCase.execute(second));
  }

  @Test
  void register_shouldReturnDifferentIds_whenRegisteringTwoDifferentUsers() {
    RegisterCommand one = getCommand(uniqueEmail("user1"));
    RegisterCommand two = getCommand(uniqueEmail("user2"));

    UUID id1 = registerUseCase.execute(one);
    UUID id2 = registerUseCase.execute(two);

    assertNotEquals(id1, id2, "Deux utilisateurs différents ne doivent pas avoir le même UUID");
  }
}
