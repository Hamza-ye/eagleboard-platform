package com.mass3d.userkeyjsonvalue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import com.mass3d.DhisSpringTest;
import com.mass3d.user.User;
import com.mass3d.user.UserService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserKeyJsonValueStoreTest
    extends DhisSpringTest
{
    @Autowired
    private UserKeyJsonValueStore userKeyJsonValueStore;

    @Autowired
    private UserService injectUserService;

    private User user;

    @Override
    public void setUpTest() {
        this.userService = injectUserService;
        user = createUserAndInjectSecurityContext( true );
    }

    @Test
    public void testAddUserKeyJsonValue()
    {
        UserKeyJsonValue userKeyJsonValue = new UserKeyJsonValue();

        userKeyJsonValue.setValue( "{}" );
        userKeyJsonValue.setKey( "test" );
        userKeyJsonValue.setNamespace( "a" );
        userKeyJsonValue.setUser( user );

        userKeyJsonValueStore.save( userKeyJsonValue );
        long id = userKeyJsonValue.getId();

        assertNotNull( userKeyJsonValue );
        assertEquals( userKeyJsonValue, userKeyJsonValueStore.get( id ) );
    }

    @Test
    public void testAddUserKeyJsonValuesAndGetNamespacesByUser()
    {
        UserKeyJsonValue userKeyJsonValueA = new UserKeyJsonValue();

        userKeyJsonValueA.setValue( "{}" );
        userKeyJsonValueA.setNamespace( "test_a" );
        userKeyJsonValueA.setKey( "a" );
        userKeyJsonValueA.setUser( user );

        userKeyJsonValueStore.save(userKeyJsonValueA);

        UserKeyJsonValue userKeyJsonValueB = new UserKeyJsonValue();

        userKeyJsonValueB.setValue( "{}" );
        userKeyJsonValueB.setNamespace( "test_b" );
        userKeyJsonValueB.setKey( "b" );
        userKeyJsonValueB.setUser( user );

        userKeyJsonValueStore.save(userKeyJsonValueB);

        List<String> list = userKeyJsonValueStore.getNamespacesByUser( user );

        assertTrue( list.contains( "test_a" ) );
        assertTrue( list.contains( "test_b" ) );
    }

    @Test
    public void testAddUserKeyJsonValuesAndGetUserKeyJsonValuesByUser()
    {
        UserKeyJsonValue userKeyJsonValueA = new UserKeyJsonValue();

        userKeyJsonValueA.setValue( "{}" );
        userKeyJsonValueA.setNamespace( "a" );
        userKeyJsonValueA.setKey( "test_a" );
        userKeyJsonValueA.setUser( user );

        userKeyJsonValueStore.save(userKeyJsonValueA);

        UserKeyJsonValue userKeyJsonValueB = new UserKeyJsonValue();

        userKeyJsonValueB.setValue( "{}" );
        userKeyJsonValueB.setNamespace( "a" );
        userKeyJsonValueB.setKey( "test_b" );
        userKeyJsonValueB.setUser( user );

        userKeyJsonValueStore.save(userKeyJsonValueB);

        List<UserKeyJsonValue> list = userKeyJsonValueStore.getUserKeyJsonValueByUserAndNamespace( user, "a" );

        assertTrue( list.contains( userKeyJsonValueA ) );
        assertTrue( list.contains( userKeyJsonValueB ) );
    }
}
