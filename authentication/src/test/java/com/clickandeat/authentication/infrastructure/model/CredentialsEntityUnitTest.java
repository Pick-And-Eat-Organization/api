package com.clickandeat.authentication.infrastructure.model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.clickandeat.authentication.domain.Credentials;
import com.clickandeat.authentication.domain.valueobject.Role;
import com.clickandeat.authentication.domain.valueobject.Scope;
import com.clickandeat.shared.enums.CredentialsStatus;
import com.clickandeat.shared.enums.RoleName;
import java.util.Date;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
class CredentialsEntityUnitTest {

  @Test
  void shouldConvertFromDomainToEntityAndBack() {
    UUID id = UUID.randomUUID();
    String email = "john@example.com";
    String phoneNumber = "+33601020304";
    String password = "hashedPassword";
    Date createdAt = new Date();
    Date updatedAt = new Date();
    Set<Scope> scopes = Set.of(new Scope("READ", "PRODUCT"));

    Role role = new Role(RoleName.CONSUMER, scopes);
    Credentials domainWithPhone =
        new Credentials(id, email, phoneNumber, password, role, createdAt, updatedAt);
    RoleEntity roleEntity = mock(RoleEntity.class);
    when(roleEntity.getId()).thenReturn(2);
    when(roleEntity.getName()).thenReturn("CONSUMER");
    CredentialsEntity entity = CredentialsEntity.fromDomain(domainWithPhone, roleEntity);
    Credentials result = entity.toDomain();

    assertEquals(id, result.getId());
    assertEquals(email, result.getEmail());
    assertEquals(password, result.getPassword());
    assertEquals(phoneNumber, result.getPhoneNumber());
    assertEquals(CredentialsStatus.ACTIVE, entity.getStatus());
    assertFalse(entity.isEmailVerified());
    assertFalse(entity.isPhoneVerified());
    assertEquals(phoneNumber, entity.getPhoneNumber());

    assertEquals(RoleName.CONSUMER, result.getRole().name());
    assertEquals(CredentialsStatus.ACTIVE, result.getStatus());
  }

  @Test
  void shouldHandleNullUpdatedAtCorrectly() {
    UUID id = UUID.randomUUID();
    String email = "jane@example.com";
    String password = "securePass";
    Date createdAt = new Date();

    Role role = new Role(RoleName.ADMIN, Set.of());
    Credentials domainWithPhone =
        new Credentials(id, email, "+33601020305", password, role, createdAt, null);

    RoleEntity roleEntity = mock(RoleEntity.class);
    when(roleEntity.getId()).thenReturn(1);
    when(roleEntity.getName()).thenReturn("ADMIN");

    CredentialsEntity entity = CredentialsEntity.fromDomain(domainWithPhone, roleEntity);

    assertNotNull(entity);
    assertNull(entity.getUpdatedAt());
    assertEquals(CredentialsStatus.ACTIVE, entity.getStatus());
    assertEquals("+33601020305", entity.getPhoneNumber());

    Credentials result = entity.toDomain();
    assertNull(result.getUpdatedAt());
    assertEquals(CredentialsStatus.ACTIVE, result.getStatus());
    assertEquals("+33601020305", result.getPhoneNumber());
  }
}
