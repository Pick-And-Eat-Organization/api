package com.clickandeat.api.authentication.controllers;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.swagger.v3.oas.annotations.Operation;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag("unit")
class PublicAuthenticationControllerUnitTest {

  @Test
  void publicAuthenticationControllerShouldExposePublicBaseRoute() {
    RequestMapping requestMapping =
        PublicAuthenticationController.class.getAnnotation(RequestMapping.class);

    assertNotNull(requestMapping);
    assertArrayEquals(new String[] {"public/api/v1/authentication"}, requestMapping.value());
  }

  @Test
  void consumerLoginShouldUseLoginDtoAndSwaggerMetadata() throws Exception {
    Method method =
        PublicAuthenticationController.class.getDeclaredMethod(
            "loginForConsumer", com.clickandeat.api.authentication.dto.LoginRequestDto.class);
    Operation operation = method.getAnnotation(Operation.class);
    PostMapping postMapping = method.getAnnotation(PostMapping.class);

    assertNotNull(operation);
    assertEquals("Login a consumer", operation.summary());
    assertNotNull(postMapping);
    assertArrayEquals(new String[] {"/login/consumer"}, postMapping.value());
  }

  @Test
  void proLoginShouldUseLoginDtoAndSwaggerMetadata() throws Exception {
    Method method =
        PublicAuthenticationController.class.getDeclaredMethod(
            "loginForPro", com.clickandeat.api.authentication.dto.LoginRequestDto.class);
    Operation operation = method.getAnnotation(Operation.class);
    PostMapping postMapping = method.getAnnotation(PostMapping.class);

    assertNotNull(operation);
    assertEquals("Login a pro", operation.summary());
    assertNotNull(postMapping);
    assertArrayEquals(new String[] {"/login/pro"}, postMapping.value());
  }

  @Test
  void adminLoginShouldUseLoginDtoAndSwaggerMetadata() throws Exception {
    Method method =
        PublicAuthenticationController.class.getDeclaredMethod(
            "loginForAdmin", com.clickandeat.api.authentication.dto.LoginRequestDto.class);
    Operation operation = method.getAnnotation(Operation.class);
    PostMapping postMapping = method.getAnnotation(PostMapping.class);

    assertNotNull(operation);
    assertEquals("Login an admin", operation.summary());
    assertNotNull(postMapping);
    assertArrayEquals(new String[] {"/login/admin"}, postMapping.value());
  }
}
