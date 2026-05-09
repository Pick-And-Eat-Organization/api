package com.clickandeat.authentication.application.usecase.register;

import com.clickandeat.shared.account.CreateProAccountRequest;
import java.util.UUID;

public interface IRegisterUseCase {
  UUID execute(RegisterCommand command);

  UUID registerPro(RegisterCommand command, CreateProAccountRequest proAccountRequest);
}
