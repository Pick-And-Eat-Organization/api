package com.clickandeat.shared.account;

import java.time.LocalDate;

public record CreateProAccountRequest(
    String firstName,
    String lastName,
    String accountPhoneNumber,
    LocalDate accountBirthDate,
    String kbisRef,
    String siret,
    String legalName,
    String legalForm,
    String address1,
    String address2,
    String address3,
    String city,
    String postalCode,
    String country) {}
