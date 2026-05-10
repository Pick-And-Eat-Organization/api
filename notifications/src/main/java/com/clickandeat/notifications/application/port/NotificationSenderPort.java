package com.clickandeat.notifications.application.port;

import com.clickandeat.notifications.domain.NotificationChannel;

public interface NotificationSenderPort {
  void send(NotificationChannel channel, String recipient, String subject, String content);
}
