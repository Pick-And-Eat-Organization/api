package com.clickandeat.api.account.controllers;

import com.clickandeat.api.account.swagger.CurrentProAccountApiResponse;
import com.clickandeat.api.config.filter.CustomUserDetails;
import com.clickandeat.api.shared.GenericApiResponse;
import com.clickandeat.shared.account.CurrentProAccountResponse;
import com.clickandeat.shared.account.GetCurrentProAccountPort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("private/api/v1/account")
@Tag(name = "Account", description = "Private account endpoints.")
public class CurrentProAccountController {

  private final GetCurrentProAccountPort getCurrentProAccountPort;

  public CurrentProAccountController(GetCurrentProAccountPort getCurrentProAccountPort) {
    this.getCurrentProAccountPort = getCurrentProAccountPort;
  }

  @PreAuthorize("hasRole('PRO')")
  @Operation(
      summary = "Get current pro account",
      description = "Return the authenticated pro account with pro informations.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Account successfully retrieved",
            content =
                @Content(schema = @Schema(implementation = CurrentProAccountApiResponse.class))),
        @ApiResponse(
            responseCode = "401",
            description = "Missing or invalid authentication.",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = com.clickandeat.api.authentication.swagger.ErrorResponse.class))),
        @ApiResponse(
            responseCode = "403",
            description = "Authenticated user is not a pro.",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = com.clickandeat.api.authentication.swagger.ErrorResponse.class))),
        @ApiResponse(
            responseCode = "404",
            description = "Account not found.",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = com.clickandeat.api.authentication.swagger.ErrorResponse.class))),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error.",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = com.clickandeat.api.authentication.swagger.ErrorResponse.class)))
      })
  @GetMapping("/me/pro")
  public ResponseEntity<GenericApiResponse<CurrentProAccountResponse>> getCurrentProAccount(
      @AuthenticationPrincipal CustomUserDetails userDetails) {
    UUID credentialsId = userDetails.getCredentialsId();
    CurrentProAccountResponse account =
        this.getCurrentProAccountPort.getCurrentProAccount(credentialsId);
    return ResponseEntity.ok(new GenericApiResponse<>("Account retrieved successfully.", account));
  }
}
