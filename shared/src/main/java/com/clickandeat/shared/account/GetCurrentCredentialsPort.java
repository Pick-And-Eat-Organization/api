package com.clickandeat.shared.account;

import java.util.UUID;

public interface GetCurrentCredentialsPort {
  CurrentCredentialsResponse getCurrentCredentials(UUID credentialsId);
}
