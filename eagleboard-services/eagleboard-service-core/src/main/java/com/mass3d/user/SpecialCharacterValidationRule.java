package com.mass3d.user;

import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

@Component("com.mass3d.user.SpecialCharacterValidationRule")
public class SpecialCharacterValidationRule
    implements PasswordValidationRule {

  public static final String ERROR = "Password must have at least one special character";
  public static final String I18_ERROR = "password_specialcharacter_validation";
  private static final Pattern SPECIAL_CHARACTER = Pattern.compile(".*[^A-Za-z0-9].*");

  @Override
  public PasswordValidationResult validate(CredentialsInfo credentialsInfo) {
    if (StringUtils.isBlank(credentialsInfo.getPassword())) {
      return new PasswordValidationResult(MANDATORY_PARAMETER_MISSING,
          I18_MANDATORY_PARAMETER_MISSING, false);
    }

    if (!SPECIAL_CHARACTER.matcher(credentialsInfo.getPassword()).matches()) {
      return new PasswordValidationResult(ERROR, I18_ERROR, false);
    }

    return new PasswordValidationResult(true);
  }

  @Override
  public boolean isRuleApplicable(CredentialsInfo credentialsInfo) {
    return true;
  }
}
