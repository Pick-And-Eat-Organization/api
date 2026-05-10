package com.clickandeat.authentication.application.usecase.verification;

import com.clickandeat.authentication.application.exceptions.application.CredentialsNotFoundException;
import com.clickandeat.authentication.application.exceptions.application.InvalidVerificationCodeException;
import com.clickandeat.authentication.application.usecase.credentials_management.ICredentialsManagementUseCase;
import com.clickandeat.authentication.domain.repository.ICredentialsRepository;
import com.clickandeat.shared.verification.VerifyVerificationCodeRequest;
import com.clickandeat.shared.verification.VerificationChannel;
import com.clickandeat.shared.verification.VerificationCodePort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CredentialsVerificationUseCase implements ICredentialsVerificationUseCase {

  private final ICredentialsRepository credentialsRepository;
  private final ICredentialsManagementUseCase credentialsManagementUseCase;
  private final VerificationCodePort verificationCodePort;

  public CredentialsVerificationUseCase(
      ICredentialsRepository credentialsRepository,
      ICredentialsManagementUseCase credentialsManagementUseCase,
      VerificationCodePort verificationCodePort) {
    this.credentialsRepository = credentialsRepository;
    this.credentialsManagementUseCase = credentialsManagementUseCase;
    this.verificationCodePort = verificationCodePort;
  }

  @Override
  public void verifyEmail(VerifyCredentialsCodeCommand command) {
    ensureCredentialsExist(command.credentialsId());
    boolean verified =
        this.verificationCodePort.verifyVerificationCode(
            new VerifyVerificationCodeRequest(
                command.credentialsId(), VerificationChannel.EMAIL, command.code()));
    if (!verified) {
      throw new InvalidVerificationCodeException();
    }
    this.credentialsManagementUseCase.verifyEmail(command.credentialsId());
  }

  @Override
  public void verifyPhone(VerifyCredentialsCodeCommand command) {
    ensureCredentialsExist(command.credentialsId());
    boolean verified =
        this.verificationCodePort.verifyVerificationCode(
            new VerifyVerificationCodeRequest(
                command.credentialsId(), VerificationChannel.SMS, command.code()));
    if (!verified) {
      throw new InvalidVerificationCodeException();
    }
    this.credentialsManagementUseCase.verifyPhone(command.credentialsId());
  }

  private void ensureCredentialsExist(java.util.UUID credentialsId) {
    this.credentialsRepository
        .findByUserId(credentialsId.toString())
        .orElseThrow(CredentialsNotFoundException::new);
  }
}
