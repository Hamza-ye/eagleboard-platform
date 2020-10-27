package com.mass3d.configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.mass3d.DhisSpringTest;
import com.mass3d.user.User;
import com.mass3d.user.UserGroup;
import com.mass3d.user.UserGroupService;
import com.mass3d.user.UserService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ConfigurationServiceTest
    extends DhisSpringTest
{
    @Autowired
    private UserService userService;
    
    @Autowired
    private UserGroupService userGroupService;
    
    @Autowired
    private ConfigurationService configurationService;
    
    @Test
    public void testConfiguration()
    {
        User userA = createUser( 'A' );
        User userB = createUser( 'B' );
        
        UserGroup group = new UserGroup( "UserGroupA" );
        group.getMembers().add( userA );
        group.getMembers().add(  userB );
        
        userService.addUser( userA );
        userService.addUser( userB );
        userGroupService.addUserGroup( group );
        
        Configuration config = configurationService.getConfiguration();
        
        assertNull( config.getFeedbackRecipients() );
        
        config.setFeedbackRecipients( group );
        
        configurationService.setConfiguration( config );
        
        config = configurationService.getConfiguration();
        
        assertNotNull( config.getFeedbackRecipients() );
        assertEquals( group, config.getFeedbackRecipients() );
    }
}
