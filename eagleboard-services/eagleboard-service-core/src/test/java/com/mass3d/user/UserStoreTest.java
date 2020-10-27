package com.mass3d.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.mass3d.DhisSpringTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserStoreTest
    extends DhisSpringTest
{
    @Autowired
    private UserStore userStore;

    // Todo Eagle Test Add Activity, todoTask,...
    @Test
    public void testAddGetUser()
    {

        User userA = createUser( 'A' );
        User userB = createUser( 'B' );

        userStore.save( userA );
        Long idA = userA.getId();
        userStore.save( userB );
        Long idB = userB.getId();

        assertEquals( userA, userStore.get( idA ) );
        assertEquals( userB, userStore.get( idB ) );
    }

    @Test
    public void testUpdateUser()
    {
        User userA = createUser( 'A' );
        User userB = createUser( 'B' );

        userStore.save( userA );
        Long idA = userA.getId();
        userStore.save( userB );
        Long idB = userB.getId();

        assertEquals( userA, userStore.get( idA ) );
        assertEquals( userB, userStore.get( idB ) );
        
        userA.setSurname( "UpdatedSurnameA" );
        
        userStore.update( userA );
        
        assertEquals( userStore.get( idA ).getSurname(), "UpdatedSurnameA" );
    }
    
    @Test
    public void testDeleteUser()
    {
        User userA = createUser( 'A' );
        User userB = createUser( 'B' );

        userStore.save( userA );
        Long idA = userA.getId();
        userStore.save( userB );
        Long idB = userB.getId();

        assertEquals( userA, userStore.get( idA ) );
        assertEquals( userB, userStore.get( idB ) );
        
        userStore.delete( userA );
        
        assertNull( userStore.get( idA ) );
        assertNotNull( userStore.get( idB ) );
    }
}
