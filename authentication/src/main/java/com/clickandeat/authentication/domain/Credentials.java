package com.clickandeat.authentication.domain;

import com.clickandeat.authentication.domain.valueobject.Role;
import com.clickandeat.shared.enums.CredentialsStatus;
import java.util.Date;
import java.util.UUID;

public class Credentials {
  private final UUID id;
  private final String email;
  private final String phoneNumber;
  private String password;
  private final Role role;
  private final Date createdAt;
  private Date updatedAt;
  private CredentialsStatus status;
  private boolean emailVerified;
  private boolean phoneVerified;

  public Credentials(
      UUID id, String email, String password, Role role, Date createdAt, Date updatedAt) {
    this(
        id,
        email,
        null,
        password,
        role,
        createdAt,
        updatedAt,
        CredentialsStatus.ACTIVE,
        false,
        false);
  }

  public Credentials(
      UUID id,
      String email,
      String phoneNumber,
      String password,
      Role role,
      Date createdAt,
      Date updatedAt) {
    this(id, email, phoneNumber, password, role, createdAt, updatedAt, CredentialsStatus.ACTIVE, false, false);
  }

  public Credentials(
      UUID id,
      String email,
      String phoneNumber,
      String password,
      Role role,
      Date createdAt,
      Date updatedAt,
      CredentialsStatus status,
      boolean emailVerified,
      boolean phoneVerified) {
    this.id = id;
    this.email = email;
    this.phoneNumber = phoneNumber;
    this.password = password;
    this.role = role;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.status = status;
    this.emailVerified = emailVerified;
    this.phoneVerified = phoneVerified;
  }

  public UUID getId() {
    return id;
  }

  public String getEmail() {
    return email;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public String getPassword() {
    return password;
  }

  public Role getRole() {
    return role;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public boolean hasAdminRole() {
    return role.isAdmin();
  }

  public boolean hasConsummerRole() {
    return role.isConsumer();
  }

  public boolean hasProRole() {
    return role.isPro();
  }

  public Date getUpdatedAt() {
    return updatedAt;
  }

  public CredentialsStatus getStatus() {
    return status;
  }

  public boolean isEmailVerified() {
    return emailVerified;
  }

  public boolean isPhoneVerified() {
    return phoneVerified;
  }

  public boolean isActive() {
    return this.status == CredentialsStatus.ACTIVE;
  }

  public void activate() {
    this.status = CredentialsStatus.ACTIVE;
    this.updatedAt = new Date();
  }

  public void suspend() {
    this.status = CredentialsStatus.SUSPENDED;
    this.updatedAt = new Date();
  }

  public void verifyEmail() {
    this.emailVerified = true;
    this.updatedAt = new Date();
  }

  public void verifyPhone() {
    this.phoneVerified = true;
    this.updatedAt = new Date();
  }

  public void changePassword(String password) {
    this.password = password;
    this.updatedAt = new Date();
  }

  public boolean canAccess(String action, String target) {
    return role.hasWildcardScope() || role.hasScope(action, target);
  }
}
