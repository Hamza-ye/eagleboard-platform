package com.mass3d.user;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("com.mass3d.user.PasswordValidationService")
public class DefaultPasswordValidationService
    implements PasswordValidationService {

  @Autowired
  private List<PasswordValidationRule> rules;

  @Override
  public PasswordValidationResult validate(CredentialsInfo credentialsInfo) {
    PasswordValidationResult result;

    for (PasswordValidationRule rule : rules) {
      if (rule.isRuleApplicable(credentialsInfo)) {
        result = rule.validate(credentialsInfo);

        if (!result.isValid()) {
          return result;
        }
      }
    }

    return new PasswordValidationResult(true);
  }
}
