package com.clickandeat.account.application.usecase.command;

import static org.junit.jupiter.api.Assertions.*;

import com.clickandeat.shared.enums.RoleName;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

@Tag("unit")
public class CreateGenericAccountCommandUnitTest {

  private static Validator validator;

  @BeforeAll
  static void init() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  private CreateGenericAccountCommand validCommandWithPhone(String phone) {
    return new CreateGenericAccountCommand(
        UUID.randomUUID(), "Alice", "Dupont", RoleName.CONSUMER, phone, "1990-01-31");
  }

  private CreateGenericAccountCommand fullyValid() {
    return validCommandWithPhone("+33650404135");
  }

  private record TestCase(CreateGenericAccountCommand command, String expectedField) {}

  static Stream<TestCase> nullFieldProvider() {
    return Stream.of(
        new TestCase(
            new CreateGenericAccountCommand(
                null, "Alice", "Dupont", RoleName.CONSUMER, "+123-456-7890", "1990-01-31"),
            "credentialsId"),
        new TestCase(
            new CreateGenericAccountCommand(
                UUID.randomUUID(),
                null,
                "Dupont",
                RoleName.CONSUMER,
                "+123-456-7890",
                "1990-01-31"),
            "firstName"),
        new TestCase(
            new CreateGenericAccountCommand(
                UUID.randomUUID(), "Alice", null, RoleName.CONSUMER, "+123-456-7890", "1990-01-31"),
            "lastName"),
        new TestCase(
            new CreateGenericAccountCommand(
                UUID.randomUUID(), "Alice", "Dupont", null, "+123-456-7890", "1990-01-31"),
            "roleName"),
        new TestCase(
            new CreateGenericAccountCommand(
                UUID.randomUUID(), "Alice", "Dupont", RoleName.CONSUMER, "+123-456-7890", null),
            "accountBirthDate"));
  }

  @Test
  void command_shouldNotHaveAnyViolations() {
    var cmd = fullyValid();

    Set<ConstraintViolation<CreateGenericAccountCommand>> violations = validator.validate(cmd);

    assertTrue(violations.isEmpty());
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
        "123",
        "+12-3456-7890",
        "abc-def-ghij",
        "123--456-7890",
        "123 4567 890",
        "+123 456 7890123"
      })
  void command_invalidPhoneFormatShouldFailed(String phone) {
    var cmd = validCommandWithPhone(phone);

    var violations = validator.validate(cmd);

    assertFalse(violations.isEmpty());
    var v = violations.iterator().next();
    assertEquals("accountPhoneNumber", v.getPropertyPath().toString());
    assertEquals("Phone number must have a valid form", v.getMessage());
  }

  @ParameterizedTest
  @MethodSource("nullFieldProvider")
  void command_shouldFailedIfNonNullableFieldIsNull(TestCase testCase) {
    Set<ConstraintViolation<CreateGenericAccountCommand>> violations =
        validator.validate(testCase.command);

    assertFalse(violations.isEmpty(), "A violation should be present");
    assertTrue(
        violations.stream()
            .anyMatch(v -> v.getPropertyPath().toString().equals(testCase.expectedField)));
  }
}
