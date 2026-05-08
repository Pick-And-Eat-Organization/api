package com.clickandeat.authentication.infrastructure.config.jwt;

import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@ConfigurationProperties("api.security.jwt")
@EnableConfigurationProperties
public class SecurityJwtConfig {
  private String secret;
  private Duration accessExpirationMs;

  public String getSecret() {
    return secret;
  }

  public Duration getAccessExpirationMs() {
    return accessExpirationMs;
  }

  public void setSecret(String secret) {
    this.secret = secret;
  }

  public void setAccessExpirationMs(Duration accessExpirationMs) {
    this.accessExpirationMs = accessExpirationMs;
  }
}
