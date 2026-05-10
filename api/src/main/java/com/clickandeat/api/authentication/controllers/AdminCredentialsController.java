package com.clickandeat.api.authentication.controllers;

import com.clickandeat.api.authentication.swagger.ErrorResponse;
import com.clickandeat.api.shared.GenericApiResponse;
import com.clickandeat.authentication.application.usecase.credentials_management.ICredentialsManagementUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("private/api/v1/authentication/admin/credentials")
@Tag(name = "Authentication (Admin)", description = "Admin endpoints for credentials moderation.")
@PreAuthorize("hasRole('ADMIN')")
public class AdminCredentialsController {

  private final ICredentialsManagementUseCase credentialsManagementUseCase;

  public AdminCredentialsController(ICredentialsManagementUseCase credentialsManagementUseCase) {
    this.credentialsManagementUseCase = credentialsManagementUseCase;
  }

  @Operation(summary = "Activate credentials", description = "Activate a credentials record.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Credentials activated.",
            content = @Content(schema = @Schema(implementation = GenericApiResponse.class))),
        @ApiResponse(
            responseCode = "401",
            description = "Missing or invalid authentication.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(
            responseCode = "403",
            description = "Authenticated user is not allowed to perform this action.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(
            responseCode = "404",
            description = "Credentials not found.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
      })
  @PostMapping("/{credentialsId}/activate")
  public ResponseEntity<GenericApiResponse<String>> activate(@PathVariable UUID credentialsId) {
    this.credentialsManagementUseCase.activate(credentialsId);
    return ResponseEntity.ok(new GenericApiResponse<>("Credentials activated successfully.", null));
  }

  @Operation(summary = "Suspend credentials", description = "Suspend a credentials record.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Credentials suspended.",
            content = @Content(schema = @Schema(implementation = GenericApiResponse.class))),
        @ApiResponse(
            responseCode = "401",
            description = "Missing or invalid authentication.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(
            responseCode = "403",
            description = "Authenticated user is not allowed to perform this action.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(
            responseCode = "404",
            description = "Credentials not found.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
      })
  @PostMapping("/{credentialsId}/suspend")
  public ResponseEntity<GenericApiResponse<String>> suspend(@PathVariable UUID credentialsId) {
    this.credentialsManagementUseCase.suspend(credentialsId);
    return ResponseEntity.ok(new GenericApiResponse<>("Credentials suspended successfully.", null));
  }

  @Operation(summary = "Verify email", description = "Mark the email as verified.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Email verified.",
            content = @Content(schema = @Schema(implementation = GenericApiResponse.class))),
        @ApiResponse(
            responseCode = "401",
            description = "Missing or invalid authentication.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(
            responseCode = "403",
            description = "Authenticated user is not allowed to perform this action.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(
            responseCode = "404",
            description = "Credentials not found.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
      })
  @PostMapping("/{credentialsId}/verify-email")
  public ResponseEntity<GenericApiResponse<String>> verifyEmail(@PathVariable UUID credentialsId) {
    this.credentialsManagementUseCase.verifyEmail(credentialsId);
    return ResponseEntity.ok(new GenericApiResponse<>("Email verified successfully.", null));
  }

  @Operation(summary = "Verify phone", description = "Mark the phone number as verified.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Phone verified.",
            content = @Content(schema = @Schema(implementation = GenericApiResponse.class))),
        @ApiResponse(
            responseCode = "401",
            description = "Missing or invalid authentication.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(
            responseCode = "403",
            description = "Authenticated user is not allowed to perform this action.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(
            responseCode = "404",
            description = "Credentials not found.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
      })
  @PostMapping("/{credentialsId}/verify-phone")
  public ResponseEntity<GenericApiResponse<String>> verifyPhone(@PathVariable UUID credentialsId) {
    this.credentialsManagementUseCase.verifyPhone(credentialsId);
    return ResponseEntity.ok(new GenericApiResponse<>("Phone verified successfully.", null));
  }
}
