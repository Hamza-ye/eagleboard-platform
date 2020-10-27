package com.mass3d.user;

import com.mass3d.setting.SettingKey;
import com.mass3d.setting.SystemSettingManager;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("com.mass3d.user.PasswordLengthValidationRule")
public class PasswordLengthValidationRule implements PasswordValidationRule {

  public static final String ERROR = "Password must have at least %d, and at most %d characters";
  private static final String I18_ERROR = "password_length_validation";

  private final SystemSettingManager systemSettingManager;

  @Autowired
  public PasswordLengthValidationRule(SystemSettingManager systemSettingManager) {
    this.systemSettingManager = systemSettingManager;
  }

  @Override
  public PasswordValidationResult validate(CredentialsInfo credentialsInfo) {
    if (StringUtils.isBlank(credentialsInfo.getPassword())) {
      return new PasswordValidationResult(MANDATORY_PARAMETER_MISSING,
          I18_MANDATORY_PARAMETER_MISSING, false);
    }

    int minCharLimit = (Integer) systemSettingManager
        .getSystemSetting(SettingKey.MIN_PASSWORD_LENGTH);

    int maxCharLimit = (Integer) systemSettingManager
        .getSystemSetting(SettingKey.MAX_PASSWORD_LENGTH);

    String password = credentialsInfo.getPassword();

    if (password.trim().length() < minCharLimit || password.trim().length() > maxCharLimit) {
      return new PasswordValidationResult(String.format(ERROR, minCharLimit, maxCharLimit),
          I18_ERROR, false);
    }

    return new PasswordValidationResult(true);
  }

  @Override
  public boolean isRuleApplicable(CredentialsInfo credentialsInfo) {
    return true;
  }
}
