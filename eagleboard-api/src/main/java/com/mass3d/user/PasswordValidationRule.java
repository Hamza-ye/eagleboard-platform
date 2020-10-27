package com.mass3d.user;

public interface PasswordValidationRule
{
    String MANDATORY_PARAMETER_MISSING = "Username or Password is missing";
    String I18_MANDATORY_PARAMETER_MISSING = "mandatory_parameter_missing";

    /**
     * Validates user password to make sure it comply with requirements related to
     * password strength.
     *
     * @param credentialsInfo
     * @return {@link PasswordValidationResult}
     */
    PasswordValidationResult validate(CredentialsInfo credentialsInfo);

    /**
     * All rules are not applicable all the time so this will check if this rule should be validated
     * against the password or be skipped.
     *
     * @param credentialsInfo
     * @return true if rule is application, false otherwise
     */
    boolean isRuleApplicable(CredentialsInfo credentialsInfo);
}
