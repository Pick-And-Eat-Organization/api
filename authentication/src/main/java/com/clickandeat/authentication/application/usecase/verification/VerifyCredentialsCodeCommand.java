package com.clickandeat.authentication.application.usecase.verification;

import java.util.UUID;

public record VerifyCredentialsCodeCommand(UUID credentialsId, String code) {}
