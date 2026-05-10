package com.clickandeat.api.account.controllers;

import com.clickandeat.api.account.swagger.CurrentAccountApiResponse;
import com.clickandeat.api.config.filter.CustomUserDetails;
import com.clickandeat.api.shared.GenericApiResponse;
import com.clickandeat.shared.account.CurrentAccountResponse;
import com.clickandeat.shared.account.GetCurrentAccountPort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("private/api/v1/account")
@Tag(name = "Account", description = "Private account endpoints.")
public class CurrentAccountController {

  private final GetCurrentAccountPort getCurrentAccountPort;

  public CurrentAccountController(GetCurrentAccountPort getCurrentAccountPort) {
    this.getCurrentAccountPort = getCurrentAccountPort;
  }

  @Operation(
      summary = "Get current account",
      description = "Return the authenticated account using the credentials id from the token.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Account successfully retrieved",
            content =
                @Content(schema = @Schema(implementation = CurrentAccountApiResponse.class))),
        @ApiResponse(
            responseCode = "401",
            description = "Missing or invalid authentication.",
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
  @GetMapping("/me")
  public ResponseEntity<GenericApiResponse<CurrentAccountResponse>> getCurrentAccount(
      @AuthenticationPrincipal CustomUserDetails userDetails) {
    UUID credentialsId = userDetails.getCredentialsId();
    CurrentAccountResponse account = this.getCurrentAccountPort.getCurrentAccount(credentialsId);
    return ResponseEntity.ok(new GenericApiResponse<>("Account retrieved successfully.", account));
  }
}
