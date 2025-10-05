package com.clickandeat.account.application.usecase.command;

public record CreateProAccountCommand(
    CreateGenericAccountCommand baseCommand,
    String kbis_ref,
    String siret,
    String legal_name,
    String legal_form,
    String address1,
    String address2,
    String address3,
    String city,
    String postalCode,
    String country) {}
