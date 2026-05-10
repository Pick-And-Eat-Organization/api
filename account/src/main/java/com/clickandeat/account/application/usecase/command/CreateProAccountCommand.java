package com.clickandeat.account.application.usecase.command;

public record CreateProAccountCommand(
    CreateGenericAccountCommand baseCommand,
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
