package com.mass3d.user;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents minimal user information.
 * 
 */
public class UserInfo
{
    private long id;
    
    private String username;
    
    private Set<String> authorities = new HashSet<>();
    
    protected UserInfo()
    {
    }
    
    public UserInfo( long id, String username, Set<String> authorities )
    {
        this.id = id;
        this.username = username;
        this.authorities = authorities;
    }

    // -------------------------------------------------------------------------
    // Logic
    // -------------------------------------------------------------------------
    
    public boolean isSuper()
    {
        return authorities.contains( UserAuthorityGroup.AUTHORITY_ALL );
    }
    
    public static UserInfo fromUser( User user )
    {
        if ( user == null )
        {
            return null;
        }
        
        UserCredentials credentials = user.getUserCredentials();
        
        return new UserInfo( credentials.getId(), credentials.getUsername(), credentials.getAllAuthorities() );
    }
    
    // -------------------------------------------------------------------------
    // Get methods
    // -------------------------------------------------------------------------

    public long getId()
    {
        return id;
    }

    public String getUsername()
    {
        return username;
    }

    public Set<String> getAuthorities()
    {
        return authorities;
    }
}
