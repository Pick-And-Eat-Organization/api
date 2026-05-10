package com.clickandeat.account.infrastructure.model;

import com.clickandeat.account.domain.account.pro.AccountProInformations;
import com.clickandeat.shared.enums.LegalForm;
import jakarta.persistence.*;

@Entity()
@Table(
    name = "pro_informations",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"siret"})})
public class AccountProInformationsEntity {
  @Id
  @Column(name = "account_id", updatable = false, nullable = false)
  private Long id;

  @OneToOne(optional = false)
  @MapsId
  @JoinColumn(name = "account_id")
  private AccountEntity account;

  @Column(name = "kbis_ref", updatable = true, nullable = true)
  private String kbisRef;

  @Column(name = "siret", updatable = true, nullable = false)
  private String siret;

  @Column(name = "address1", updatable = true, nullable = false)
  private String address1;

  @Column(name = "address2", updatable = true, nullable = true)
  private String address2;

  @Column(name = "address3", updatable = true, nullable = true)
  private String address3;

  @Column(name = "city", updatable = true, nullable = false)
  private String city;

  @Column(name = "cp", updatable = true, nullable = false)
  private String cp;

  @Column(name = "country", updatable = true, nullable = false)
  private String country;

  @Enumerated(EnumType.STRING)
  @org.hibernate.annotations.JdbcType(org.hibernate.dialect.PostgreSQLEnumJdbcType.class)
  @Column(name = "legal_form", updatable = true, nullable = false)
  private LegalForm legalForm;

  @Column(name = "legal_name", updatable = true, nullable = false)
  private String legalName;

  public AccountProInformationsEntity(
      Long id,
      AccountEntity account,
      String kbisRef,
      String siret,
      String address1,
      String address2,
      String address3,
      String city,
      String cp,
      String country,
      LegalForm legalForm,
      String legalName) {
    this.id = id;
    this.account = account;
    this.kbisRef = kbisRef;
    this.siret = siret;
    this.address1 = address1;
    this.address2 = address2;
    this.address3 = address3;
    this.city = city;
    this.cp = cp;
    this.country = country;
    this.legalForm = legalForm;
    this.legalName = legalName;
  }

  public AccountProInformationsEntity() {}

  public Long getId() {
    return id;
  }

  public AccountProInformations toDomain() {
    return new AccountProInformations(
        this.account.getId(),
        this.kbisRef,
        this.siret,
        this.legalName,
        this.legalForm.name(),
        this.address1,
        this.address2,
        this.address3,
        this.city,
        this.cp,
        this.country);
  }

  public static AccountProInformationsEntity fromDomain(
      AccountProInformations accountProInformations, AccountEntity accountEntity) {
    return new AccountProInformationsEntity(
        null,
        accountEntity,
        accountProInformations.getKbisRef(),
        accountProInformations.getSiret(),
        accountProInformations.getLocalisation().address1(),
        accountProInformations.getLocalisation().address2(),
        accountProInformations.getLocalisation().address3(),
        accountProInformations.getLocalisation().city(),
        accountProInformations.getLocalisation().postalCode(),
        accountProInformations.getLocalisation().country(),
        LegalForm.fromString(accountProInformations.getLegalForm()),
        accountProInformations.getLegalName());
  }
}
