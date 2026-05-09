package com.clickandeat.api.account.swagger;

import com.clickandeat.api.shared.GenericApiResponse;
import com.clickandeat.shared.account.AccountProInformationsResponse;
import com.clickandeat.shared.account.CurrentAccountResponse;
import com.clickandeat.shared.enums.RoleName;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public class CurrentAccountApiResponse extends GenericApiResponse<CurrentAccountResponse> {
  public CurrentAccountApiResponse() {
    super(
        "Account retrieved successfully.",
        new CurrentAccountResponse(
            1L,
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            RoleName.CONSUMER,
            "John",
            "Doe",
            "+33601020304",
            LocalDate.of(1995, 1, 1),
            Instant.parse("2025-01-01T12:00:00Z"),
            null,
            new AccountProInformationsResponse(
                "KBIS-2025-001",
                "12345678900011",
                "Click and Eat",
                "SAS",
                "1 rue de Paris",
                "Batiment A",
                "Etage 1",
                "Paris",
                "75001",
                "France")));
  }
}
