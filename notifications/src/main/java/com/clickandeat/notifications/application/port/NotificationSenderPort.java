package com.clickandeat.notifications.application.port;

import com.clickandeat.shared.verification.VerificationChannel;

public interface NotificationSenderPort {
  void send(VerificationChannel channel, String recipient, String subject, String content);
}
