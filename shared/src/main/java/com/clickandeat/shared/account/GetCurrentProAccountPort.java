package com.clickandeat.shared.account;

import java.util.UUID;

public interface GetCurrentProAccountPort {
  CurrentProAccountResponse getCurrentProAccount(UUID credentialsId);
}
