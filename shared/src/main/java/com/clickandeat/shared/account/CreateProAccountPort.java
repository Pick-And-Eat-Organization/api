package com.clickandeat.shared.account;

import java.util.UUID;

public interface CreateProAccountPort {
  Long createProAccount(UUID credentialsId, CreateProAccountRequest request);
}
