package com.clickandeat.shared.account;

import com.clickandeat.shared.enums.RoleName;
import java.time.LocalDate;
import java.util.UUID;

public record CreateGenericAccountRequest(
    UUID credentialsId,
    String firstName,
    String lastName,
    RoleName roleName,
    LocalDate accountBirthDate) {}
