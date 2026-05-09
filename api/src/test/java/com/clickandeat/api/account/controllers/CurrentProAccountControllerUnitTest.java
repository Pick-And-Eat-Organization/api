package com.clickandeat.api.account.controllers;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.swagger.v3.oas.annotations.Operation;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag("unit")
class CurrentProAccountControllerUnitTest {

  @Test
  void currentProAccountControllerShouldExposePrivateRouteAndRequireProRole() throws Exception {
    RequestMapping requestMapping =
        CurrentProAccountController.class.getAnnotation(RequestMapping.class);
    Method method =
        CurrentProAccountController.class.getDeclaredMethod(
            "getCurrentProAccount", com.clickandeat.api.config.filter.CustomUserDetails.class);
    Operation operation = method.getAnnotation(Operation.class);
    PreAuthorize preAuthorize = method.getAnnotation(PreAuthorize.class);
    GetMapping getMapping = method.getAnnotation(GetMapping.class);

    assertNotNull(requestMapping);
    assertArrayEquals(new String[] {"private/api/v1/account"}, requestMapping.value());
    assertNotNull(operation);
    assertEquals("Get current pro account", operation.summary());
    assertNotNull(preAuthorize);
    assertEquals("hasRole('PRO')", preAuthorize.value());
    assertNotNull(getMapping);
    assertArrayEquals(new String[] {"/me/pro"}, getMapping.value());
  }
}
