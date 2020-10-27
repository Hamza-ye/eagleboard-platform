package com.mass3d.encryption;

public enum EncryptionStatus {
  OK("Encryption enabled"),
  MISSING_JCE_POLICY("Missing the required JCE policy files for strong encryption"),
  MISSING_ENCRYPTION_PASSWORD("Missing encryption.password in dhis.conf"),
  ENCRYPTION_PASSWORD_TOO_SHORT(
      "encryption.password in dhis.conf too short, minimum 24 characters required");

  private final String key;

  EncryptionStatus(String key) {
    this.key = key;
  }

  public boolean isOk() {
    return this == OK;
  }

  public String getKey() {
    return key;
  }
}
