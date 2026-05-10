package com.clickandeat.shared.account;

import java.util.UUID;

public interface GetCurrentAccountPort {
  CurrentAccountResponse getCurrentAccount(UUID credentialsId);
}
