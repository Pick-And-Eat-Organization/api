package com.clickandeat.authentication.application.exceptions.technical;

import com.clickandeat.shared.exceptions.AbstractTechnicalException;

public class DatabaseTechnicalException extends AbstractTechnicalException {
  public DatabaseTechnicalException(String message, Throwable cause) {
    super("database", message, cause);
  }
}
