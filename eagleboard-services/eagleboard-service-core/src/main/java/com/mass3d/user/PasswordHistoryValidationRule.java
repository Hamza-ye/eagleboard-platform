package com.mass3d.user;

import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component("com.mass3d.user.PasswordHistoryValidationRule")
public class PasswordHistoryValidationRule implements PasswordValidationRule {

  public static final String ERROR = "Password must not be one of the previous %d passwords";
  public static final String I18_ERROR = "password_history_validation";
  private static final int HISTORY_LIMIT = 24;
  private final PasswordEncoder passwordEncoder;

  private final UserService userService;

  private final CurrentUserService currentUserService;

  @Autowired
  public PasswordHistoryValidationRule(PasswordEncoder passwordEncoder, UserService userService,
      CurrentUserService currentUserService) {
    this.passwordEncoder = passwordEncoder;
    this.userService = userService;
    this.currentUserService = currentUserService;
  }

  @Override
  public PasswordValidationResult validate(CredentialsInfo credentialsInfo) {
    if (StringUtils.isBlank(credentialsInfo.getPassword())) {
      return new PasswordValidationResult(MANDATORY_PARAMETER_MISSING,
          I18_MANDATORY_PARAMETER_MISSING, false);
    }

    UserCredentials userCredentials = userService
        .getUserCredentialsByUsername(credentialsInfo.getUsername());

    List<String> previousPasswords = userCredentials.getPreviousPasswords();

    for (String encodedPassword : previousPasswords) {
      final boolean match = passwordEncoder.matches(credentialsInfo.getPassword(), encodedPassword);

      if (match) {
        return new PasswordValidationResult(String.format(ERROR, HISTORY_LIMIT), I18_ERROR, false);
      }
    }

    // remove one item from password history if size exceeds HISTORY_LIMIT
    if (previousPasswords.size() == HISTORY_LIMIT) {
      userCredentials.getPreviousPasswords().remove(0);

      userService.updateUserCredentials(userCredentials);
    }

    return new PasswordValidationResult(true);
  }

  @Override
  public boolean isRuleApplicable(CredentialsInfo credentialsInfo) {
    UserCredentials userCredentials = userService
        .getUserCredentialsByUsername(credentialsInfo.getUsername());

    if (!userService.credentialsNonExpired(userCredentials)) {
      return true;
    }

    // no need to check password history in case of new user
    return !credentialsInfo.isNewUser() &&
        currentUserService.getCurrentUsername().equals(credentialsInfo.getUsername());
  }
}
