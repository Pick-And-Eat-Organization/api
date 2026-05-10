package com.clickandeat.api.authentication.controllers;

import static org.junit.jupiter.api.Assertions.*;

import io.swagger.v3.oas.annotations.Operation;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag("unit")
class PublicVerificationControllerUnitTest {

  @Test
  void publicVerificationControllerShouldExposeRoutesAndSwaggerMetadata() throws Exception {
    RequestMapping requestMapping =
        PublicVerificationController.class.getAnnotation(RequestMapping.class);

    assertNotNull(requestMapping);
    assertArrayEquals(
        new String[] {"public/api/v1/authentication/verification"}, requestMapping.value());

    Method verifyEmail =
        PublicVerificationController.class.getDeclaredMethod(
            "verifyEmail", com.clickandeat.api.authentication.dto.VerificationCodeRequestDto.class);
    Method verifyPhone =
        PublicVerificationController.class.getDeclaredMethod(
            "verifyPhone", com.clickandeat.api.authentication.dto.VerificationCodeRequestDto.class);

    assertOperation(verifyEmail, "/email", "Verify email");
    assertOperation(verifyPhone, "/phone", "Verify phone");
  }

  private void assertOperation(Method method, String path, String summary) {
    Operation operation = method.getAnnotation(Operation.class);
    PostMapping postMapping = method.getAnnotation(PostMapping.class);

    assertNotNull(operation);
    assertEquals(summary, operation.summary());
    assertNotNull(postMapping);
    assertArrayEquals(new String[] {path}, postMapping.value());
  }
}
