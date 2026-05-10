package com.clickandeat.api.authentication.controllers;

import com.clickandeat.api.authentication.dto.RegisterRequestDto;
import com.clickandeat.api.authentication.mapper.RegisterRequestMapper;
import com.clickandeat.api.authentication.swagger.ErrorResponse;
import com.clickandeat.api.shared.GenericApiResponse;
import com.clickandeat.authentication.application.usecase.register.IRegisterUseCase;
import com.clickandeat.authentication.application.usecase.register.RegisterCommand;
import com.clickandeat.shared.enums.RoleName;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("public/api/v1/authentication/register/consumer")
@Tag(name = "Authentication (Registration)", description = "Registration endpoints split by role.")
public class ConsumerRegistrationController {

  private final IRegisterUseCase registerUseCase;

  public ConsumerRegistrationController(IRegisterUseCase registerUseCase) {
    this.registerUseCase = registerUseCase;
  }

  @Operation(summary = "Register a consumer", description = "Create a consumer account.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "201",
            description = "Consumer successfully registered",
            content = @Content(schema = @Schema(implementation = GenericApiResponse.class))),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request body.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
      })
  @PostMapping
  public ResponseEntity<GenericApiResponse<UUID>> register(
      @Valid @RequestBody RegisterRequestDto dto) {
    RegisterCommand command = RegisterRequestMapper.toCommand(dto, RoleName.CONSUMER);
    UUID credentialsId = this.registerUseCase.execute(command);
    return ResponseEntity.status(201)
        .body(
            new GenericApiResponse<>(
                "Registration completed successfully. Check your email and SMS to verify your account.",
                credentialsId));
  }
}
