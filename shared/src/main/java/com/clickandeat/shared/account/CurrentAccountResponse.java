package com.clickandeat.shared.account;

import com.clickandeat.shared.enums.RoleName;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Schema(name = "CurrentAccountResponse", description = "Authenticated account information")
public record CurrentAccountResponse(
    @Schema(description = "Account identifier", example = "1") Long accountId,
    @Schema(description = "Credentials identifier from JWT", example = "11111111-1111-1111-1111-111111111111")
        UUID credentialsId,
    @Schema(description = "User role", example = "CONSUMER") RoleName role,
    @Schema(description = "First name", example = "John") String firstName,
    @Schema(description = "Last name", example = "Doe") String lastName,
    @Schema(description = "Phone number", example = "+33601020304") String phoneNumber,
    @Schema(description = "Birth date", example = "1995-01-01") LocalDate birthDate,
    @Schema(description = "Creation timestamp", example = "2025-01-01T12:00:00Z")
        Instant createdAt,
    @Schema(description = "Last update timestamp", example = "2025-01-02T12:00:00Z")
        Instant updatedAt,
    @Schema(description = "Pro informations when the role is PRO") AccountProInformationsResponse
        proInformations) {}
