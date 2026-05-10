package com.clickandeat.shared.account;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "CurrentCredentialsResponse", description = "Current credentials contact data")
public record CurrentCredentialsResponse(
    @Schema(description = "Phone number", example = "+33601020304") String phoneNumber) {}
