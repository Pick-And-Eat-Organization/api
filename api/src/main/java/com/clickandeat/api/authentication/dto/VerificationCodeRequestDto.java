package com.clickandeat.api.authentication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public class VerificationCodeRequestDto {

  @NotNull(message = "credentialsId must be provided.")
  @Schema(
      description = "Credentials identifier returned during registration",
      example = "11111111-1111-1111-1111-111111111111")
  private final UUID credentialsId;

  @NotBlank(message = "code must be provided.")
  @Schema(description = "Verification code received by email or SMS", example = "123456")
  private final String code;

  public VerificationCodeRequestDto(UUID credentialsId, String code) {
    this.credentialsId = credentialsId;
    this.code = code;
  }

  public UUID getCredentialsId() {
    return credentialsId;
  }

  public String getCode() {
    return code;
  }
}
