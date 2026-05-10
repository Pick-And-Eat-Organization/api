package com.clickandeat.api.authentication.controllers;

import com.clickandeat.api.authentication.dto.VerificationCodeRequestDto;
import com.clickandeat.api.authentication.mapper.VerificationCodeRequestMapper;
import com.clickandeat.api.authentication.swagger.ErrorResponse;
import com.clickandeat.api.shared.GenericApiResponse;
import com.clickandeat.authentication.application.usecase.verification.ICredentialsVerificationUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("public/api/v1/authentication/verification")
@Tag(name = "Authentication (Verification)", description = "Public verification endpoints.")
public class PublicVerificationController {

  private final ICredentialsVerificationUseCase credentialsVerificationUseCase;

  public PublicVerificationController(
      ICredentialsVerificationUseCase credentialsVerificationUseCase) {
    this.credentialsVerificationUseCase = credentialsVerificationUseCase;
  }

  @Operation(summary = "Verify email", description = "Confirm the user's email with a code.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Email verified.",
            content = @Content(schema = @Schema(implementation = GenericApiResponse.class))),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid verification code.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(
            responseCode = "404",
            description = "Credentials not found.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
      })
  @PostMapping("/email")
  public ResponseEntity<GenericApiResponse<String>> verifyEmail(
      @Valid @RequestBody VerificationCodeRequestDto dto) {
    this.credentialsVerificationUseCase.verifyEmail(
        VerificationCodeRequestMapper.toCommand(dto));
    return ResponseEntity.ok(new GenericApiResponse<>("Email verified successfully.", null));
  }

  @Operation(summary = "Verify phone", description = "Confirm the user's phone with a code.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Phone verified.",
            content = @Content(schema = @Schema(implementation = GenericApiResponse.class))),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid verification code.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(
            responseCode = "404",
            description = "Credentials not found.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
      })
  @PostMapping("/phone")
  public ResponseEntity<GenericApiResponse<String>> verifyPhone(
      @Valid @RequestBody VerificationCodeRequestDto dto) {
    this.credentialsVerificationUseCase.verifyPhone(
        VerificationCodeRequestMapper.toCommand(dto));
    return ResponseEntity.ok(new GenericApiResponse<>("Phone verified successfully.", null));
  }
}
