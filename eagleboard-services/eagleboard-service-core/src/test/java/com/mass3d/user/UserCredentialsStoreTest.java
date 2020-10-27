package com.mass3d.user;

import static org.junit.Assert.assertEquals;

import com.mass3d.DhisSpringTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserCredentialsStoreTest
    extends DhisSpringTest
{
    @Autowired
    private UserCredentialsStore userCredentialsStore;
    
    @Autowired
    private UserService userService;

    private UserAuthorityGroup roleA;
    private UserAuthorityGroup roleB;
    private UserAuthorityGroup roleC;
    
    @Override
    public void setUpTest()
        throws Exception
    {
        roleA = createUserAuthorityGroup( 'A' );
        roleB = createUserAuthorityGroup( 'B' );
        roleC = createUserAuthorityGroup( 'C' );
        
        roleA.getAuthorities().add( "AuthA" );
        roleA.getAuthorities().add( "AuthB" );
        roleA.getAuthorities().add( "AuthC" );
        roleA.getAuthorities().add( "AuthD" );
        
        roleB.getAuthorities().add( "AuthA" );
        roleB.getAuthorities().add( "AuthB" );
        
        roleC.getAuthorities().add( "AuthC" );
        
        userService.addUserAuthorityGroup( roleA );
        userService.addUserAuthorityGroup( roleB );
        userService.addUserAuthorityGroup( roleC );
    }
    
    @Test
    public void testAddGetUserCredentials()
    {
        User userA = createUser( 'A' );
        User userB = createUser( 'B' );
        
        UserCredentials credentialsA = createUserCredentials( 'A', userA );
        UserCredentials credentialsB = createUserCredentials( 'B', userB );
        
        userCredentialsStore.save( credentialsA );
        Long idA = credentialsA.getId();
        userCredentialsStore.save( credentialsB );
        Long idB = credentialsB.getId();

        assertEquals( credentialsA, userCredentialsStore.get( idA ) );
        assertEquals( credentialsB, userCredentialsStore.get( idB ) );
    }
}