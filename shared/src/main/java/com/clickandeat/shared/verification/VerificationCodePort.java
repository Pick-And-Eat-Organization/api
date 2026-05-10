package com.clickandeat.shared.verification;

public interface VerificationCodePort {
  void sendVerificationCode(SendVerificationCodeRequest request);

  boolean verifyVerificationCode(VerifyVerificationCodeRequest request);
}
