package com.clickandeat.api.account.controllers;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.swagger.v3.oas.annotations.Operation;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag("unit")
class CurrentAccountControllerUnitTest {

  @Test
  void currentAccountControllerShouldExposePrivateRouteAndSwaggerMetadata() throws Exception {
    RequestMapping requestMapping =
        CurrentAccountController.class.getAnnotation(RequestMapping.class);
    Method method =
        CurrentAccountController.class.getDeclaredMethod(
            "getCurrentAccount", com.clickandeat.api.config.filter.CustomUserDetails.class);
    Operation operation = method.getAnnotation(Operation.class);
    GetMapping getMapping = method.getAnnotation(GetMapping.class);

    assertNotNull(requestMapping);
    assertArrayEquals(new String[] {"private/api/v1/account"}, requestMapping.value());
    assertNotNull(operation);
    assertEquals("Get current account", operation.summary());
    assertNotNull(getMapping);
    assertArrayEquals(new String[] {"/me"}, getMapping.value());
  }
}
