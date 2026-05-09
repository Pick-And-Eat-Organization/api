package com.clickandeat.api.config.filter;

import com.clickandeat.authentication.domain.valueobject.Scope;
import java.util.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails implements UserDetails {

  private final UUID credentialsId;
  private final String role;
  private final Set<Scope> scopes;

  public CustomUserDetails(UUID credentialsId, String role, Set<Scope> scopes) {
    this.credentialsId = credentialsId;
    this.role = role;
    this.scopes = scopes;
  }

  public UUID getCredentialsId() {
    return credentialsId;
  }

  public UUID getUserId() {
    return credentialsId;
  }

  public String getRole() {
    return role;
  }

  public Set<Scope> getScopes() {
    return scopes;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    List<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(() -> "ROLE_" + role);
    scopes.forEach(
        scope ->
            authorities.add(
                () ->
                    "SCOPE_" + scope.action().toUpperCase() + ":" + scope.target().toUpperCase()));
    return authorities;
  }

  @Override
  public String getPassword() {
    return null;
  }

  @Override
  public String getUsername() {
    return credentialsId.toString();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
