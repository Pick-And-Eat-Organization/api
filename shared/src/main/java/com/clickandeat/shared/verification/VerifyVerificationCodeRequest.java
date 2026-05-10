package com.clickandeat.shared.verification;

import java.util.UUID;

public record VerifyVerificationCodeRequest(
    UUID credentialsId, VerificationChannel channel, String code) {}
