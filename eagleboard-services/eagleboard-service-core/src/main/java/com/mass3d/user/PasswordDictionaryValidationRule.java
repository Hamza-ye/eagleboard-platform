package com.mass3d.user;

import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

@Component("com.mass3d.user.PasswordDictionaryValidationRule")
public class PasswordDictionaryValidationRule
    implements PasswordValidationRule {

  public static final String ERROR = "Password must not have any generic word";
  public static final String I18_ERROR = "password_dictionary_validation";

  private static final List<String> DICTIONARY = Arrays
      .asList("user", "admin", "system", "administrator", "username", "password", "login",
          "manager");

  @Override
  public boolean isRuleApplicable(CredentialsInfo credentialsInfo) {
    return true;
  }

  @Override
  public PasswordValidationResult validate(CredentialsInfo credentialsInfo) {
    if (StringUtils.isBlank(credentialsInfo.getPassword())) {
      return new PasswordValidationResult(MANDATORY_PARAMETER_MISSING,
          I18_MANDATORY_PARAMETER_MISSING, false);
    }

    for (String reserved : DICTIONARY) {
      if (StringUtils.containsIgnoreCase(credentialsInfo.getPassword(), reserved)) {
        return new PasswordValidationResult(ERROR, I18_ERROR, false);
      }
    }

    return new PasswordValidationResult(true);
  }
}
