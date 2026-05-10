package com.clickandeat.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(
    basePackages = {
      "com.clickandeat.api",
      "com.clickandeat.authentication",
      "com.clickandeat.account"
    })
@EnableJpaRepositories(
    basePackages = {
      "com.clickandeat.authentication.infrastructure.repository",
      "com.clickandeat.account.infrastructure.repository"
    })
@EntityScan(
    basePackages = {
      "com.clickandeat.authentication.infrastructure.model",
      "com.clickandeat.account.infrastructure.model"
    })
public class ClickAndEatApplication {
  public static void main(String[] args) {
    SpringApplication.run(ClickAndEatApplication.class, args);
  }
}
