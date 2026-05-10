package com.clickandeat.api.authentication.controllers;

import static org.junit.jupiter.api.Assertions.*;

import io.swagger.v3.oas.annotations.Operation;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag("unit")
class AdminCredentialsControllerUnitTest {

  @Test
  void controllerShouldExposeAdminCredentialsRoutesAndSwaggerMetadata() throws Exception {
    RequestMapping requestMapping =
        AdminCredentialsController.class.getAnnotation(RequestMapping.class);

    assertNotNull(requestMapping);
    assertArrayEquals(
        new String[] {"private/api/v1/authentication/admin/credentials"}, requestMapping.value());

    Method activate =
        AdminCredentialsController.class.getDeclaredMethod("activate", java.util.UUID.class);
    Method suspend =
        AdminCredentialsController.class.getDeclaredMethod("suspend", java.util.UUID.class);
    Method verifyEmail =
        AdminCredentialsController.class.getDeclaredMethod("verifyEmail", java.util.UUID.class);
    Method verifyPhone =
        AdminCredentialsController.class.getDeclaredMethod("verifyPhone", java.util.UUID.class);

    assertRouteMetadata(activate, "/{credentialsId}/activate", "Activate credentials");
    assertRouteMetadata(suspend, "/{credentialsId}/suspend", "Suspend credentials");
    assertRouteMetadata(verifyEmail, "/{credentialsId}/verify-email", "Verify email");
    assertRouteMetadata(verifyPhone, "/{credentialsId}/verify-phone", "Verify phone");

    PreAuthorize preAuthorize = AdminCredentialsController.class.getAnnotation(PreAuthorize.class);
    assertNotNull(preAuthorize);
    assertEquals("hasRole('ADMIN')", preAuthorize.value());
  }

  private void assertRouteMetadata(Method method, String path, String summary) {
    Operation operation = method.getAnnotation(Operation.class);
    PostMapping postMapping = method.getAnnotation(PostMapping.class);
    PathVariable pathVariable = method.getParameters()[0].getAnnotation(PathVariable.class);

    assertNotNull(operation);
    assertEquals(summary, operation.summary());
    assertNotNull(postMapping);
    assertArrayEquals(new String[] {path}, postMapping.value());
    assertNotNull(pathVariable);
  }
}
