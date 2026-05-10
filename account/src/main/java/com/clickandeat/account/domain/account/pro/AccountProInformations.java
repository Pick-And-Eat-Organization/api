package com.clickandeat.account.domain.account.pro;

public class AccountProInformations {
  private final Long id;
  private final String kbisRef;
  private final String siret;
  private final String legalName;
  private final String legalForm;
  private final Localisation localisation;

  public AccountProInformations(
      Long id,
      String kbisRef,
      String siret,
      String legalName,
      String legalForm,
      String address1,
      String address2,
      String address3,
      String city,
      String postalCode,
      String country) {
    this.id = id;
    this.kbisRef = kbisRef;
    this.siret = siret;
    this.legalName = legalName;
    this.legalForm = legalForm;
    this.localisation = new Localisation(address1, address2, address3, city, postalCode, country);
  }

  public Long getId() {
    return id;
  }

  public String getKbisRef() {
    return kbisRef;
  }

  public String getSiret() {
    return siret;
  }

  public String getLegalName() {
    return legalName;
  }

  public String getLegalForm() {
    return legalForm;
  }

  public Localisation getLocalisation() {
    return localisation;
  }
}
