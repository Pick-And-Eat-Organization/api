package com.clickandeat.notifications.infrastructure.console;

import com.clickandeat.notifications.application.port.NotificationSenderPort;
import com.clickandeat.shared.verification.VerificationChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ConsoleNotificationSenderAdapter implements NotificationSenderPort {

  private static final Logger LOGGER = LoggerFactory.getLogger(ConsoleNotificationSenderAdapter.class);

  @Override
  public void send(VerificationChannel channel, String recipient, String subject, String content) {
    LOGGER.info("Sending {} notification to {}: {} - {}", channel, recipient, subject, content);
  }
}
