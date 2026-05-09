package com.clickandeat.account.application.usecase.command;

import com.clickandeat.shared.enums.RoleName;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record CreateGenericAccountCommand(
    @NotNull UUID credentialsId,
    @NotNull String firstName,
    @NotNull String lastName,
    @NotNull RoleName roleName,
    @NotNull String accountBirthDate) {}
