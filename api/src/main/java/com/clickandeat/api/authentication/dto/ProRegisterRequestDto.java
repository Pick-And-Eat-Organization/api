package com.clickandeat.api.authentication.dto;

import com.clickandeat.api.authentication.dto.validators.ValidDate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ProRegisterRequestDto {
  @Email(message = "email form not valid.")
  @NotBlank(message = "email must be provided.")
  @Schema(description = "User's email", example = "example@example.com")
  private final String email;

  @NotBlank(message = "password must be provided.")
  @Size(min = 8)
  @Pattern(
      regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
      message =
          "password must contain at least 8 characters, including a lowercase letter, an uppercase letter, a digit, and a special character.")
  @Schema(description = "User's password", example = "AstrongPassw0rd!")
  private final String password;

  @NotBlank(message = "firstName must be provided.")
  @Schema(description = "User's first name", example = "John")
  private final String firstName;

  @NotBlank(message = "lastName must be provided.")
  @Schema(description = "User's last name", example = "Doe")
  private final String lastName;

  @NotBlank(message = "phoneNumber must be provided.")
  @Size(min = 10)
  @Pattern(
      regexp = "^(0[1-9]\\d{8}|\\+33[1-9]\\d{8}|0033[1-9]\\d{8})$",
      message =
          "phoneNumber must be a valid French number (e.g. 0601020304, +33601020304, or 0033601020304).")
  @Schema(
      description = "User's phone number",
      examples = {"+33601020304", "0601020304"})
  private final String phoneNumber;

  @ValidDate
  @NotBlank(message = "birthDate must be provided.")
  @Schema(description = "User's birth date", example = "1995-01-01")
  private final String birthDate;

  @NotBlank
  @Schema(description = "Kbis reference", example = "KBIS-2025-001")
  private final String kbisRef;

  @NotBlank
  @Schema(description = "SIRET number", example = "12345678900011")
  private final String siret;

  @NotBlank
  @Schema(description = "Legal name", example = "Click and Eat")
  private final String legalName;

  @NotBlank
  @Schema(description = "Legal form", example = "SAS")
  private final String legalForm;

  @NotBlank
  @Schema(description = "Address line 1", example = "1 rue de Paris")
  private final String address1;

  @Schema(description = "Address line 2", example = "Batiment A")
  private final String address2;
  @Schema(description = "Address line 3", example = "Etage 1")
  private final String address3;

  @NotBlank
  @Schema(description = "City", example = "Paris")
  private final String city;

  @NotBlank
  @Schema(description = "Postal code", example = "75001")
  private final String postalCode;

  @NotBlank
  @Schema(description = "Country", example = "France")
  private final String country;

  public ProRegisterRequestDto(
      String email,
      String password,
      String firstName,
      String lastName,
      String phoneNumber,
      String birthDate,
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
    this.email = email;
    this.password = password;
    this.firstName = firstName;
    this.lastName = lastName;
    this.phoneNumber = phoneNumber;
    this.birthDate = birthDate;
    this.kbisRef = kbisRef;
    this.siret = siret;
    this.legalName = legalName;
    this.legalForm = legalForm;
    this.address1 = address1;
    this.address2 = address2;
    this.address3 = address3;
    this.city = city;
    this.postalCode = postalCode;
    this.country = country;
  }

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public String getBirthDate() {
    return birthDate;
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

  public String getAddress1() {
    return address1;
  }

  public String getAddress2() {
    return address2;
  }

  public String getAddress3() {
    return address3;
  }

  public String getCity() {
    return city;
  }

  public String getPostalCode() {
    return postalCode;
  }

  public String getCountry() {
    return country;
  }
}
