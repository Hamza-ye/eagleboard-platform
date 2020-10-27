package com.mass3d.security;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

import com.mass3d.DhisSpringTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PasswordManagerTest
    extends DhisSpringTest
{
    @Autowired
    private PasswordManager passwordManager;

    @Test
    public void testEncodeValidatePassword()
    {
        String password = "district";

        String encodedPassword1 = passwordManager.encode( password );
        String encodedPassword2 = passwordManager.encode( password );

        assertFalse( encodedPassword1.equals( encodedPassword2 ) );
        assertFalse( password.equals( encodedPassword1 ) );

        assertTrue( passwordManager.matches( password, encodedPassword1 ));
        assertTrue( passwordManager.matches( password, encodedPassword2 ));

        assertFalse( passwordManager.matches( password, "anotherPassword" ) );
    }
}
