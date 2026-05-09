package com.clickandeat.api.authentication.controllers;

import com.clickandeat.api.authentication.dto.ProRegisterRequestDto;
import com.clickandeat.api.authentication.mapper.ProRegisterRequestMapper;
import com.clickandeat.api.shared.GenericApiResponse;
import com.clickandeat.authentication.application.usecase.register.IRegisterUseCase;
import com.clickandeat.authentication.application.usecase.register.RegisterCommand;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("public/api/v1/authentication/register/pro")
public class ProRegistrationController {

  private final IRegisterUseCase registerUseCase;

  public ProRegistrationController(IRegisterUseCase registerUseCase) {
    this.registerUseCase = registerUseCase;
  }

  @PostMapping
  public ResponseEntity<GenericApiResponse<UUID>> register(
      @Valid @RequestBody ProRegisterRequestDto dto) {
    RegisterCommand command = ProRegisterRequestMapper.toRegisterCommand(dto);
    this.registerUseCase.registerPro(
        command, ProRegisterRequestMapper.toCreateProAccountRequest(dto));
    return ResponseEntity.status(201)
        .body(new GenericApiResponse<>("Registration completed successfully.", null));
  }
}
