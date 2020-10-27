package com.mass3d.user;

import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

@Component("com.mass3d.user.DigitPatternValidationRule")
public class DigitPatternValidationRule implements PasswordValidationRule {

  public static final String ERROR = "Password must have at least one digit";
  private static final Pattern DIGIT_PATTERN = Pattern.compile(".*\\d.*");
  private static final String I18_ERROR = "password_digit_validation";

  @Override
  public PasswordValidationResult validate(CredentialsInfo credentialsInfo) {
    if (StringUtils.isBlank(credentialsInfo.getPassword())) {
      return new PasswordValidationResult(MANDATORY_PARAMETER_MISSING,
          I18_MANDATORY_PARAMETER_MISSING, false);
    }

    if (!DIGIT_PATTERN.matcher(credentialsInfo.getPassword()).matches()) {
      return new PasswordValidationResult(ERROR, I18_ERROR, false);
    }

    return new PasswordValidationResult(true);
  }

  @Override
  public boolean isRuleApplicable(CredentialsInfo credentialsInfo) {
    return true;
  }
}
