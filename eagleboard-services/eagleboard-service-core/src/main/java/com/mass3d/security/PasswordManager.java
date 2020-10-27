package com.mass3d.security;

/**
 * This interface provides access to the system configured password hashing method.
 * It is used for encoding passwords and tokens as well as checking the authenticity
 * of a given password or token. The underlying PasswordEncoder should be the same as
 * used by Spring Security to perform password checking on user authentication.
 *
 */
public interface PasswordManager
{
    String ID = PasswordManager.class.getName();

    /**
     * Cryptographically hash a password.
     * Salting (as well as the salt storage scheme) must be handled by the implementation.
     *
     * @param password password to encode.
     * @return the hashed password.
     */
    String encode(String password);

    /**
     * Determines whether the supplied password equals the encoded password or not.
     * Fetching and handling of any required salt value must be done by the implementation.
     *
     * @param rawPassword the raw, unencoded password.
     * @param encodedPassword the encoded password to match against.
     * @return true if the passwords match, false otherwise.
     */
    boolean matches(String rawPassword, String encodedPassword);

    /**
     * Returns the class name of the password encoder used by this instance.
     * @return the name of the password encoder class.
     */
    String getPasswordEncoderClassName();
}
