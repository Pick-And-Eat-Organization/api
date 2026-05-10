package com.clickandeat.shared.enums;

import java.util.Arrays;

public enum LegalForm {
  SARL,
  SAS,
  SASU,
  EI,
  EURL,
  SA,
  SNC,
  SCOP;

  public static LegalForm fromString(String value) {
    return Arrays.stream(values())
        .filter(legalForm -> legalForm.name().equalsIgnoreCase(value))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Unknown legal form: " + value));
  }
}
