package com.clickandeat.api.authentication.mapper;

import com.clickandeat.api.authentication.dto.VerificationCodeRequestDto;
import com.clickandeat.authentication.application.usecase.verification.VerifyCredentialsCodeCommand;

public class VerificationCodeRequestMapper {
  public static VerifyCredentialsCodeCommand toCommand(VerificationCodeRequestDto dto) {
    return new VerifyCredentialsCodeCommand(dto.getCredentialsId(), dto.getCode());
  }
}
