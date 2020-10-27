package com.mass3d.webapi.webdomain.user;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Wrapper DTO for a list of UserLookups.
 *
 */
@Getter
@NoArgsConstructor
public class UserLookups
{
    @JsonProperty
    private List<UserLookup> users = new ArrayList<>();

    public UserLookups( List<UserLookup> users )
    {
        this.users = users;
    }
}
