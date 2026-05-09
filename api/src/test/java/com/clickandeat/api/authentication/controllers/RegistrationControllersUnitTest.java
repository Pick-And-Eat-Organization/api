package com.clickandeat.api.authentication.controllers;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.swagger.v3.oas.annotations.Operation;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag("unit")
class RegistrationControllersUnitTest {

  @Test
  void consumerRegistrationControllerShouldExposePublicRouteAndSwaggerMetadata() {
    RequestMapping requestMapping =
        ConsumerRegistrationController.class.getAnnotation(RequestMapping.class);
    Operation operation;
    try {
      operation =
          ConsumerRegistrationController.class
              .getDeclaredMethod("register", com.clickandeat.api.authentication.dto.RegisterRequestDto.class)
              .getAnnotation(Operation.class);
    } catch (NoSuchMethodException exception) {
      throw new IllegalStateException(exception);
    }

    assertNotNull(requestMapping);
    assertArrayEquals(
        new String[] {"public/api/v1/authentication/register/consumer"}, requestMapping.value());
    assertNotNull(operation);
    assertEquals("Register a consumer", operation.summary());
  }

  @Test
  void proRegistrationControllerShouldExposePublicRouteAndSwaggerMetadata() {
    RequestMapping requestMapping = ProRegistrationController.class.getAnnotation(RequestMapping.class);
    Operation operation;
    try {
      operation =
          ProRegistrationController.class
              .getDeclaredMethod(
                  "register", com.clickandeat.api.authentication.dto.ProRegisterRequestDto.class)
              .getAnnotation(Operation.class);
    } catch (NoSuchMethodException exception) {
      throw new IllegalStateException(exception);
    }

    assertNotNull(requestMapping);
    assertArrayEquals(
        new String[] {"public/api/v1/authentication/register/pro"}, requestMapping.value());
    assertNotNull(operation);
    assertEquals("Register a pro", operation.summary());
  }

  @Test
  void adminRegistrationControllerShouldExposePrivateRouteAndRequireAdminRole() throws Exception {
    RequestMapping requestMapping = AdminRegistrationController.class.getAnnotation(RequestMapping.class);
    Method registerMethod =
        AdminRegistrationController.class.getDeclaredMethod(
            "register", com.clickandeat.api.authentication.dto.RegisterRequestDto.class);
    Operation operation = registerMethod.getAnnotation(Operation.class);
    PreAuthorize preAuthorize = registerMethod.getAnnotation(PreAuthorize.class);

    assertNotNull(requestMapping);
    assertArrayEquals(
        new String[] {"private/api/v1/authentication/register/admin"}, requestMapping.value());
    assertNotNull(operation);
    assertEquals("Register an admin", operation.summary());
    assertNotNull(preAuthorize);
    assertEquals("hasRole('ADMIN')", preAuthorize.value());
  }
}
