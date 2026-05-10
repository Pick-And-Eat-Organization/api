package com.clickandeat.account;

import com.clickandeat.shared.account.CurrentCredentialsResponse;
import com.clickandeat.shared.account.GetCurrentCredentialsPort;
import java.util.UUID;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableAutoConfiguration
@ComponentScan(basePackages = "com.clickandeat.account")
@EntityScan("com.clickandeat.account.infrastructure.model")
@EnableJpaRepositories("com.clickandeat.account.infrastructure.repository")
public class TestConfiguration {

  @Bean
  public GetCurrentCredentialsPort getCurrentCredentialsPort() {
    return (UUID credentialsId) -> new CurrentCredentialsResponse("+33600000000");
  }
}
