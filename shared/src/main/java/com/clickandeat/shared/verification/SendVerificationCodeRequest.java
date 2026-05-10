package com.clickandeat.shared.verification;

import java.util.UUID;

public record SendVerificationCodeRequest(
    UUID credentialsId, VerificationChannel channel, String recipient) {}
