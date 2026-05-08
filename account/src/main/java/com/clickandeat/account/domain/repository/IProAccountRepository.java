package com.clickandeat.account.domain.repository;

import com.clickandeat.account.domain.account.pro.AccountProInformations;

public interface IProAccountRepository {
  AccountProInformations saveAccountProInformations(AccountProInformations accountProInformations);
}
