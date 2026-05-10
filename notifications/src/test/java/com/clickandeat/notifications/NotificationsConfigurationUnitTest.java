package com.clickandeat.notifications;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
class NotificationsConfigurationUnitTest {

  @Test
  void shouldInstantiateConfiguration() {
    assertNotNull(new NotificationsConfiguration());
  }
}
