package com.mass3d.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.mass3d.DhisSpringTest;
import com.mass3d.setting.SettingKey;
import com.mass3d.setting.SystemSettingManager;
import com.mass3d.user.User;
import com.mass3d.user.UserCredentials;
import com.mass3d.user.UserService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class SecurityServiceTest
    extends DhisSpringTest
{
    private UserCredentials credentials;

    private UserCredentials otherCredentials;

    @Autowired
    private UserService userService; 
    
    @Autowired
    private PasswordManager passwordManager;
    
    @Autowired
    private SecurityService securityService;

    @Autowired
    private SystemSettingManager systemSettingManager;
    
    @Override
    public void setUpTest()
    {        
        credentials = new UserCredentials();
        credentials.setUsername( "johndoe" );
        credentials.setPassword( "" );
        credentials.setAutoFields();

        User userA = createUser( 'A' );
        userA.setEmail( "validA@email.com" );
        userA.setUserCredentials( credentials );

        credentials.setUserInfo( userA );
        userService.addUserCredentials( credentials );

        otherCredentials = new UserCredentials();
        otherCredentials.setUsername( "janesmith" );
        otherCredentials.setPassword( "" );

        User userB = createUser( 'B' );
        userB.setEmail( "validB@email.com" );
        userB.setUserCredentials( otherCredentials );
        otherCredentials.setUserInfo( userB );
        userService.addUserCredentials( otherCredentials );
    }
    
    @Test
    public void testUserAuthenticationLockout()
    {
        systemSettingManager.saveSystemSetting( 
            SettingKey.LOCK_MULTIPLE_FAILED_LOGINS, Boolean.TRUE );
        
        String username = "dr_evil";
                
        securityService.registerFailedLogin( username );
        assertFalse( securityService.isLocked( username ) );
        
        securityService.registerFailedLogin( username );
        assertFalse( securityService.isLocked( username ) );
        
        securityService.registerFailedLogin( username );
        assertFalse( securityService.isLocked( username ) );
        
        securityService.registerFailedLogin( username );
        assertFalse( securityService.isLocked( username ) );
        
        securityService.registerFailedLogin( username );
        assertFalse( securityService.isLocked( username ) );
        
        securityService.registerFailedLogin( username );
        assertTrue( securityService.isLocked( username ) );

        securityService.registerFailedLogin( username );
        assertTrue( securityService.isLocked( username ) );
        
        securityService.registerSuccessfulLogin( username );
        assertFalse( securityService.isLocked( username ) );
        
        systemSettingManager.saveSystemSetting( 
            SettingKey.LOCK_MULTIPLE_FAILED_LOGINS, Boolean.FALSE );
    }

    @Test
    public void testRestoreRecoverPassword()
    {
        String[] result = securityService.initRestore( credentials, RestoreOptions.RECOVER_PASSWORD_OPTION );

        assertEquals( 1, result.length );

        String token = result[0];

        assertNotNull( token );
        assertNotNull( credentials.getRestoreToken() );
        assertNotNull( credentials.getRestoreExpiry() );

        RestoreOptions restoreOptions = securityService.getRestoreOptions( token );

        assertEquals( RestoreOptions.RECOVER_PASSWORD_OPTION, restoreOptions );
        assertEquals( RestoreType.RECOVER_PASSWORD, restoreOptions.getRestoreType() );
        assertEquals( false, restoreOptions.isUsernameChoice() );

        //
        // verifyToken()
        //
        assertNotNull( securityService.verifyToken( otherCredentials, token, RestoreType.RECOVER_PASSWORD ) );

        assertNotNull( securityService.verifyToken( credentials, "wrongToken", RestoreType.RECOVER_PASSWORD ) );

        assertNotNull( securityService.verifyToken( credentials, token, RestoreType.INVITE ) );

        assertNull( securityService.verifyToken( credentials, token, RestoreType.RECOVER_PASSWORD ) );

        //
        // canRestoreNow()
        //
        assertFalse( securityService.canRestore( otherCredentials, token, RestoreType.RECOVER_PASSWORD ) );

        assertFalse( securityService.canRestore( credentials, "wrongToken", RestoreType.RECOVER_PASSWORD ) );

        assertFalse( securityService.canRestore( credentials, token, RestoreType.INVITE ) );

        assertTrue( securityService.canRestore( credentials, token, RestoreType.RECOVER_PASSWORD ) );

        //
        // restore()
        //
        String password = "NewPassword1";

        assertFalse( securityService.restore( otherCredentials, token, password, RestoreType.INVITE ) );

        assertFalse( securityService.restore( credentials, "wrongToken", password, RestoreType.INVITE ) );

        assertFalse( securityService.restore( credentials, token, password, RestoreType.INVITE ) );

        assertTrue( securityService.restore( credentials, token, password, RestoreType.RECOVER_PASSWORD ) );



        //
        // check password
        //

        assertTrue( passwordManager.matches( password, credentials.getPassword() ) );
    }

    @Test
    public void testRestoreInvite()
    {
        String[] result = securityService.initRestore( credentials, RestoreOptions.INVITE_WITH_DEFINED_USERNAME );

        assertEquals( 1, result.length );

        String token = result[0];

        assertNotNull( token );
        assertNotNull( credentials.getRestoreToken() );
        assertNotNull( credentials.getRestoreExpiry() );

        RestoreOptions restoreOptions = securityService.getRestoreOptions( token );

        assertEquals( RestoreOptions.INVITE_WITH_DEFINED_USERNAME, restoreOptions );
        assertEquals( RestoreType.INVITE, restoreOptions.getRestoreType() );
        assertEquals( false, restoreOptions.isUsernameChoice() );

        //
        // verifyToken()
        //
        assertNotNull( securityService.verifyToken( otherCredentials, token, RestoreType.INVITE ) );

        assertNotNull( securityService.verifyToken( credentials, "wrongToken", RestoreType.INVITE ) );

        assertNotNull( securityService.verifyToken( credentials, token, RestoreType.RECOVER_PASSWORD ) );

        assertNull( securityService.verifyToken( credentials, token, RestoreType.INVITE ) );

        //
        // canRestoreNow()
        //
        assertFalse( securityService.canRestore( otherCredentials, token, RestoreType.INVITE ) );

        assertFalse( securityService.canRestore( credentials, "wrongToken", RestoreType.INVITE ) );

        assertFalse( securityService.canRestore( credentials, token, RestoreType.RECOVER_PASSWORD ) );

        assertTrue( securityService.canRestore( credentials, token, RestoreType.INVITE ) );

        //
        // restore()
        //
        String password = "NewPassword1";

        assertFalse( securityService.restore( otherCredentials, token, password, RestoreType.INVITE ) );

        assertFalse( securityService.restore( credentials, "wrongToken", password, RestoreType.INVITE ) );

        assertFalse( securityService.restore( credentials, token, password, RestoreType.RECOVER_PASSWORD ) );

        assertTrue( securityService.restore( credentials, token, password, RestoreType.INVITE ) );

        //
        // check password
        //

        assertTrue( passwordManager.matches( password, credentials.getPassword() ) );
    }

    @Test
    public void testRestoreInviteWithUsernameChoice()
    {
        String[] result = securityService.initRestore( credentials, RestoreOptions.INVITE_WITH_USERNAME_CHOICE );

        assertEquals( 1, result.length );

        String token = result[0];

        RestoreOptions restoreOptions = securityService.getRestoreOptions( token );

        assertEquals( RestoreOptions.INVITE_WITH_USERNAME_CHOICE, restoreOptions );
        assertEquals( RestoreType.INVITE, restoreOptions.getRestoreType() );
        assertEquals( true, restoreOptions.isUsernameChoice() );
    }
    
    @Test
    public void testIsInviteUsername()
    {
        assertTrue( securityService.isInviteUsername( "invite-johndoe@gmail.com-OsTci1JyHRU" ) );
        assertTrue( securityService.isInviteUsername( "invite-fr37@abc.gov-OsTci1JyHRU" ) );
        assertTrue( securityService.isInviteUsername( null ) );
        assertFalse( securityService.isInviteUsername( "inv1te-mark@gmail.com-OsTci1JyHRU" ) );
        assertFalse( securityService.isInviteUsername( "invite-tomjohnson@yahoo.com-OsTci1JyHRUC" ) );
        assertFalse( securityService.isInviteUsername( "invite-johnthomson@gmail.com-OsTci1yHRU" ) );
    }    
}
