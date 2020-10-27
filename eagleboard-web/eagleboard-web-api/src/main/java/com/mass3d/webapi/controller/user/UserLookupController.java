package com.mass3d.webapi.controller.user;

import com.google.common.collect.Sets;
import com.mass3d.common.DhisApiVersion;
import com.mass3d.common.IllegalQueryException;
import com.mass3d.configuration.ConfigurationService;
import com.mass3d.feedback.ErrorCode;
import com.mass3d.feedback.ErrorMessage;
import com.mass3d.user.User;
import com.mass3d.user.UserGroup;
import com.mass3d.user.UserQueryParams;
import com.mass3d.user.UserService;
import com.mass3d.webapi.mvc.annotation.ApiVersion;
import com.mass3d.webapi.webdomain.user.UserLookup;
import com.mass3d.webapi.webdomain.user.UserLookups;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping( value = UserLookupController.API_ENDPOINT )
@ApiVersion( { DhisApiVersion.DEFAULT, DhisApiVersion.ALL } )
public class UserLookupController
{
    static final String API_ENDPOINT = "/userLookup";

    private final UserService userService;

    private final ConfigurationService config;

    public UserLookupController( UserService userService, ConfigurationService config )
    {
        this.userService = userService;
        this.config = config;
    }

    @GetMapping( value = "/{id}" )
    public UserLookup lookUpUser( @PathVariable String id )
    {
        User user = userService.getUserByIdentifier( id );

        return user != null ? UserLookup.fromUser( user ) : null;
    }

    @GetMapping
    public UserLookups lookUpUsers( @RequestParam String query )
    {
        UserQueryParams params = new UserQueryParams()
            .setQuery( query )
            .setMax( 25 );

        List<UserLookup> users = userService.getUsers( params ).stream()
            .map( UserLookup::fromUser )
            .collect( Collectors.toList() );

        return new UserLookups( users );
    }

    @GetMapping( value = "/feedbackRecipients" )
    public UserLookups lookUpFeedbackRecipients( @RequestParam String query )
    {
        UserGroup feedbackRecipients = config.getConfiguration().getFeedbackRecipients();

        if ( feedbackRecipients == null )
        {
            throw new IllegalQueryException( new ErrorMessage( ErrorCode.E6200 ) );
        }

        UserQueryParams params = new UserQueryParams()
            .setQuery( query )
            .setUserGroups( Sets.newHashSet( feedbackRecipients ) )
            .setMax( 25 );

        List<UserLookup> users = userService.getUsers( params ).stream()
            .map( UserLookup::fromUser )
            .collect( Collectors.toList() );

        return new UserLookups( users );
    }
}
