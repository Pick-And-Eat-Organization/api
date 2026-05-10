package com.clickandeat.notifications.infrastructure.redis;

import com.clickandeat.notifications.application.port.NotificationSenderPort;
import com.clickandeat.shared.verification.SendVerificationCodeRequest;
import com.clickandeat.shared.verification.VerifyVerificationCodeRequest;
import com.clickandeat.shared.verification.VerificationChannel;
import com.clickandeat.shared.verification.VerificationCodePort;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisVerificationCodeService implements VerificationCodePort {

  private static final Logger LOGGER = LoggerFactory.getLogger(RedisVerificationCodeService.class);
  private static final Duration TTL = Duration.ofMinutes(15);
  private static final SecureRandom RANDOM = new SecureRandom();

  private final StringRedisTemplate stringRedisTemplate;
  private final NotificationSenderPort notificationSenderPort;

  public RedisVerificationCodeService(
      StringRedisTemplate stringRedisTemplate, NotificationSenderPort notificationSenderPort) {
    this.stringRedisTemplate = stringRedisTemplate;
    this.notificationSenderPort = notificationSenderPort;
  }

  @Override
  public void sendVerificationCode(SendVerificationCodeRequest request) {
    String code = generateCode();
    this.stringRedisTemplate.opsForValue().set(key(request.credentialsId(), request.channel()), code, TTL);
    this.notificationSenderPort.send(
        request.channel(),
        request.recipient(),
        buildSubject(request.channel()),
        buildContent(request.channel(), code));
    LOGGER.info(
        "Verification code stored for {} on channel {}", request.credentialsId(), request.channel());
  }

  @Override
  public boolean verifyVerificationCode(VerifyVerificationCodeRequest request) {
    String key = key(request.credentialsId(), request.channel());
    String storedCode = this.stringRedisTemplate.opsForValue().get(key);
    if (storedCode == null || !storedCode.equals(request.code())) {
      return false;
    }
    this.stringRedisTemplate.delete(key);
    return true;
  }

  private String key(java.util.UUID credentialsId, VerificationChannel channel) {
    return "verification:%s:%s".formatted(channel.name().toLowerCase(Locale.ROOT), credentialsId);
  }

  private String buildSubject(VerificationChannel channel) {
    return channel == VerificationChannel.EMAIL ? "Verify your email" : "Verify your phone number";
  }

  private String buildContent(VerificationChannel channel, String code) {
    return channel == VerificationChannel.EMAIL
        ? "Your email verification code is " + code
        : "Your phone verification code is " + code;
  }

  private String generateCode() {
    return String.format("%06d", RANDOM.nextInt(1_000_000));
  }
}
