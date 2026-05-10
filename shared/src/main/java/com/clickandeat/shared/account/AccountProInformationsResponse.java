package com.clickandeat.shared.account;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "AccountProInformationsResponse", description = "Pro account information")
public record AccountProInformationsResponse(
    @Schema(description = "Kbis reference", example = "KBIS-2025-001") String kbisRef,
    @Schema(description = "SIRET number", example = "12345678900011") String siret,
    @Schema(description = "Legal name", example = "Click and Eat") String legalName,
    @Schema(description = "Legal form", example = "SAS") String legalForm,
    @Schema(description = "Address line 1", example = "1 rue de Paris") String address1,
    @Schema(description = "Address line 2", example = "Batiment A") String address2,
    @Schema(description = "Address line 3", example = "Etage 1") String address3,
    @Schema(description = "City", example = "Paris") String city,
    @Schema(description = "Postal code", example = "75001") String postalCode,
    @Schema(description = "Country", example = "France") String country) {}
