package com.mass3d.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.mass3d.DhisSpringTest;
import com.mass3d.common.IdentifiableObjectStore;
import javax.annotation.Resource;
import org.junit.Test;

public class UserAuthorityGroupTest
    extends DhisSpringTest
{
    @Resource(name="com.mass3d.user.UserAuthorityGroupStore")
    private IdentifiableObjectStore<UserAuthorityGroup> userAuthorityGroupStore;

    @Test
    public void testAddGetUserAuthorityGroup()
    {
        UserAuthorityGroup roleA = createUserAuthorityGroup( 'A' );
        UserAuthorityGroup roleB = createUserAuthorityGroup( 'B' );
        UserAuthorityGroup roleC = createUserAuthorityGroup( 'C' );
        
        userAuthorityGroupStore.save( roleA );
        Long idA = roleA.getId();

        userAuthorityGroupStore.save( roleB );
        Long idB = roleB.getId();

        userAuthorityGroupStore.save( roleC );
        Long idC = roleC.getId();

            assertEquals( roleA, userAuthorityGroupStore.get( idA ) );
        assertEquals( roleB, userAuthorityGroupStore.get( idB ) );
        assertEquals( roleC, userAuthorityGroupStore.get( idC ) );
    }

    @Test
    public void testDeleteUserAuthorityGroup()
    {
        UserAuthorityGroup roleA = createUserAuthorityGroup( 'A' );
        UserAuthorityGroup roleB = createUserAuthorityGroup( 'B' );
        UserAuthorityGroup roleC = createUserAuthorityGroup( 'C' );

        userAuthorityGroupStore.save( roleA );
        Long idA = roleA.getId();

        userAuthorityGroupStore.save( roleB );
        Long idB = roleB.getId();

        userAuthorityGroupStore.save( roleC );
        Long idC = roleC.getId();
        
        assertEquals( roleA, userAuthorityGroupStore.get( idA ) );
        assertEquals( roleB, userAuthorityGroupStore.get( idB ) );
        assertEquals( roleC, userAuthorityGroupStore.get( idC ) );
        
        userAuthorityGroupStore.delete( roleB );
        
        assertNotNull( userAuthorityGroupStore.get( idA ) );
        assertNull( userAuthorityGroupStore.get( idB ) );
        assertNotNull( userAuthorityGroupStore.get( idA ) );
    }
}
