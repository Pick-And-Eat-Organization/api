package com.clickandeat.api.authentication.controllers;

import com.clickandeat.api.authentication.dto.RegisterRequestDto;
import com.clickandeat.api.authentication.mapper.RegisterRequestMapper;
import com.clickandeat.api.shared.GenericApiResponse;
import com.clickandeat.authentication.application.usecase.register.IRegisterUseCase;
import com.clickandeat.authentication.application.usecase.register.RegisterCommand;
import com.clickandeat.shared.enums.RoleName;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("private/api/v1/authentication/register/admin")
public class AdminRegistrationController {

  private final IRegisterUseCase registerUseCase;

  public AdminRegistrationController(IRegisterUseCase registerUseCase) {
    this.registerUseCase = registerUseCase;
  }

  @PostMapping
  public ResponseEntity<GenericApiResponse<UUID>> register(
      @Valid @RequestBody RegisterRequestDto dto) {
    RegisterCommand command = RegisterRequestMapper.toCommand(dto, RoleName.ADMIN);
    this.registerUseCase.execute(command);
    return ResponseEntity.status(201)
        .body(new GenericApiResponse<>("Registration completed successfully.", null));
  }
}
