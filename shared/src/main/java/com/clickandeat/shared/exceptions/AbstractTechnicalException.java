package com.clickandeat.shared.exceptions;

public abstract class AbstractTechnicalException extends RuntimeException {
  private final String component;

  public AbstractTechnicalException(String component, String message, Throwable cause) {
    super(message, cause);
    this.component = component;
  }
}
