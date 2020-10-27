package com.mass3d.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.mass3d.DhisSpringTest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserGroupServiceTest
    extends DhisSpringTest
{
    @Autowired
    private UserGroupService userGroupService;

    @Autowired
    private UserService userService;
    
    private User user1;
    private User user2;
    private User user3;

    @Override
    public void setUpTest()
        throws Exception
    {
        user1 = createUser( 'A' );
        user2 = createUser( 'B' );
        user3 = createUser( 'C' );

        userService.addUser( user1 );
        userService.addUser( user2 );
        userService.addUser( user3 );
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    private void assertEq( char uniqueCharacter, UserGroup userGroup )
    {
        assertEquals( "UserGroup" + uniqueCharacter, userGroup.getName() );
    }

    // -------------------------------------------------------------------------
    // Tests
    // -------------------------------------------------------------------------

    @Test
    public void testAddUserGroup()
    {
        Set<User> members = new HashSet<>();

        members.add( user1 );
        members.add( user2 );
        members.add( user3 );

        UserGroup userGroup = createUserGroup( 'A', members );

        userGroupService.addUserGroup( userGroup );

        assertEq( 'A', userGroup );
        assertNotNull( userGroup.getMembers() );
        assertEquals( members, userGroup.getMembers() );
    }

    @Test
    public void testDeleteUserGroup()
    {
        Set<User> members = new HashSet<>();

        members.add( user1 );
        members.add( user2 );

        UserGroup userGroup = createUserGroup( 'A', members );

        userGroupService.addUserGroup( userGroup );

        userGroup = userGroupService.getUserGroupByName( "UserGroupA" ).get( 0 );

        Long id = userGroup.getId();

        assertEq( 'A', userGroup );
        assertTrue( members.size() == userGroup.getMembers().size() );

        userGroupService.deleteUserGroup( userGroup );

        assertNull( userGroupService.getUserGroup( id ) );
    }

    @Test
    public void testUpdateUserGroup()
    {
        Set<User> members = new HashSet<>();

        members.add( user1 );
        members.add( user3 );

        UserGroup userGroup = createUserGroup( 'A', members );

        userGroupService.addUserGroup( userGroup );

        userGroup = userGroupService.getUserGroupByName( "UserGroupA" ).get( 0 );

        Long id = userGroup.getId();

        assertEq( 'A', userGroup );
        assertEquals( members, userGroup.getMembers() );

        userGroup.setName( "UserGroupB" );
        userGroup.getMembers().add( user2 );

        userGroupService.updateUserGroup( userGroup );

        userGroup = userGroupService.getUserGroup( id );

        assertEq( 'B', userGroup );

        assertEquals( 3, userGroup.getMembers().size() );

        assertTrue( userGroup.getMembers().contains( user1 ) );
        assertTrue( userGroup.getMembers().contains( user2 ) );
        assertTrue( userGroup.getMembers().contains( user3 ) );
    }

    @Test
    public void testGetAllUserGroups()
    {
        List<UserGroup> userGroups = new ArrayList<>();

        Set<User> members = new HashSet<>();

        members.add( user1 );
        members.add( user3 );

        UserGroup userGroupA = createUserGroup( 'A', members );
        userGroups.add( userGroupA );

        userGroupService.addUserGroup( userGroupA );

        members = new HashSet<>();

        members.add( user1 );
        members.add( user2 );

        UserGroup userGroupB = createUserGroup( 'B', members );
        userGroups.add( userGroupB );

        userGroupService.addUserGroup( userGroupB );

        assertEquals( userGroupService.getAllUserGroups(), userGroups );
    }

    @Test
    public void testGetUserGroupById()
    {
        Set<User> members = new HashSet<>();

        members.add( user1 );
        members.add( user2 );
        members.add( user3 );

        UserGroup userGroup = createUserGroup( 'A', members );

        userGroupService.addUserGroup( userGroup );

        Long id = userGroupService.getUserGroupByName( "UserGroupA" ).get( 0 ).getId();

        userGroup = userGroupService.getUserGroup( id );

        assertEq( 'A', userGroup );
        assertNotNull( userGroup.getMembers() );
    }

    @Test
    public void testGetUserGroupByName()
    {
        Set<User> members = new HashSet<>();

        members.add( user1 );

        UserGroup userGroup = createUserGroup( 'B', members );

        userGroupService.addUserGroup( userGroup );

        userGroup = userGroupService.getUserGroupByName( "UserGroupB" ).get( 0 );

        assertEq( 'B', userGroup );
        assertNotNull( userGroup.getMembers() );
    }
}
