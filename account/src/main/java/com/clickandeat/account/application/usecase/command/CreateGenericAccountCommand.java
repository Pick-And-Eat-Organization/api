package com.clickandeat.account.application.usecase.command;

import com.clickandeat.shared.enums.RoleName;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.UUID;

public record CreateGenericAccountCommand(
    @NotNull UUID credentialsId,
    @NotNull String firstName,
    @NotNull String lastName,
    @NotNull RoleName roleName,
    @NotNull
        @Pattern(
            regexp = "^[+]?[(]?[0-9]{3}[)]?[-\\s.]?[0-9]{3}[-\\s.]?[0-9]{4,6}$",
            message = "Phone number must have a valid form")
        String accountPhoneNumber,
    @NotNull String accountBirthDate) {}
