package com.mass3d.webapi.webdomain.user;

import com.mass3d.user.User;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO for a minimal and non-sensitive representation of a user.
 *
 */
@Getter
@Setter
@NoArgsConstructor
public class UserLookup
{
    @JsonProperty
    private String id;

    @JsonProperty
    private String username;

    @JsonProperty
    private String firstName;

    @JsonProperty
    private String surname;

    @JsonProperty
    private String displayName;

    public static UserLookup fromUser( User user )
    {
        String displayName = String.format( "%s %s", user.getFirstName(), user.getSurname() );

        UserLookup lookup = new UserLookup();
        lookup.setId( user.getUid() ); // Will be changed to UUID later
        lookup.setUsername( user.getUsername() );
        lookup.setFirstName( user.getFirstName() );
        lookup.setSurname( user.getSurname() );
        lookup.setDisplayName( displayName );
        return lookup;
    }
}
