package com.mass3d.user;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

@Component("com.mass3d.user.UserParameterValidationRule")
public class UserParameterValidationRule
    implements PasswordValidationRule {

  public static final String ERROR = "Username/Email must not be a part of password";
  private static final String I18_ERROR = "password_username_validation";

  @Override
  public boolean isRuleApplicable(CredentialsInfo credentialsInfo) {
    return true;
  }

  @Override
  public PasswordValidationResult validate(CredentialsInfo credentialsInfo) {
    String email = credentialsInfo.getEmail();
    String password = credentialsInfo.getPassword();
    String username = credentialsInfo.getUsername();

    // other parameters will be skipped in case of new user
    if (credentialsInfo.isNewUser()) {
      if (StringUtils.isBlank(password)) {
        return new PasswordValidationResult(MANDATORY_PARAMETER_MISSING,
            I18_MANDATORY_PARAMETER_MISSING, false);
      }
    } else if (StringUtils.isBlank(password) || StringUtils.isBlank(username)) {
      return new PasswordValidationResult(MANDATORY_PARAMETER_MISSING,
          I18_MANDATORY_PARAMETER_MISSING, false);
    }

    // Password should not contain part of either username or email
    if (StringUtils.containsIgnoreCase(password, StringUtils.defaultIfEmpty(username, null)) ||
        StringUtils.containsIgnoreCase(password, StringUtils.defaultIfEmpty(email, null))) {
      return new PasswordValidationResult(ERROR, I18_ERROR, false);
    }

    return new PasswordValidationResult(true);
  }
}
