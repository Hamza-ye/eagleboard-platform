package com.mass3d.user;

public interface PasswordValidationService
{
    PasswordValidationResult validate(CredentialsInfo credentialsInfo);
}
