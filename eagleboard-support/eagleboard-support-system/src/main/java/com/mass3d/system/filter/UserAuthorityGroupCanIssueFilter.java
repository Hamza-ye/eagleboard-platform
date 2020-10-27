package com.mass3d.system.filter;

import com.mass3d.commons.filter.Filter;
import com.mass3d.user.User;
import com.mass3d.user.UserAuthorityGroup;
import com.mass3d.user.UserCredentials;

public class UserAuthorityGroupCanIssueFilter
    implements Filter<UserAuthorityGroup>
{
    private UserCredentials userCredentials;
    
    private boolean canGrantOwnUserAuthorityGroups = false;
    
    protected UserAuthorityGroupCanIssueFilter()
    {
    }
    
    public UserAuthorityGroupCanIssueFilter( User user, boolean canGrantOwnUserAuthorityGroups )
    {
        if ( user != null && user.getUserCredentials() != null )
        {
            this.userCredentials = user.getUserCredentials();
            this.canGrantOwnUserAuthorityGroups = canGrantOwnUserAuthorityGroups;
        }
    }

    @Override
    public boolean retain( UserAuthorityGroup group )
    {
        return userCredentials != null && userCredentials.canIssueUserRole( group, canGrantOwnUserAuthorityGroups );
    }
}