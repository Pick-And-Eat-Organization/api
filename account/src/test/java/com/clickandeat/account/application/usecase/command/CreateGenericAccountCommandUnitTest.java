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

@Tag("unit")
public class CreateGenericAccountCommandUnitTest {

  private static Validator validator;

  @BeforeAll
  static void init() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  private CreateGenericAccountCommand validCommand() {
    return new CreateGenericAccountCommand(
        UUID.randomUUID(), "Alice", "Dupont", RoleName.CONSUMER, "1990-01-31");
  }

  private CreateGenericAccountCommand fullyValid() {
    return validCommand();
  }

  private record TestCase(CreateGenericAccountCommand command, String expectedField) {}

  static Stream<TestCase> nullFieldProvider() {
    return Stream.of(
        new TestCase(
            new CreateGenericAccountCommand(
                null, "Alice", "Dupont", RoleName.CONSUMER, "1990-01-31"),
            "credentialsId"),
        new TestCase(
            new CreateGenericAccountCommand(
                UUID.randomUUID(), null, "Dupont", RoleName.CONSUMER, "1990-01-31"),
            "firstName"),
        new TestCase(
            new CreateGenericAccountCommand(
                UUID.randomUUID(), "Alice", null, RoleName.CONSUMER, "1990-01-31"),
            "lastName"),
        new TestCase(
            new CreateGenericAccountCommand(
                UUID.randomUUID(), "Alice", "Dupont", null, "1990-01-31"),
            "roleName"),
        new TestCase(
            new CreateGenericAccountCommand(
                UUID.randomUUID(), "Alice", "Dupont", RoleName.CONSUMER, null),
            "accountBirthDate"));
  }

  @Test
  void command_shouldNotHaveAnyViolations() {
    var cmd = fullyValid();

    Set<ConstraintViolation<CreateGenericAccountCommand>> violations = validator.validate(cmd);

    assertTrue(violations.isEmpty());
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
