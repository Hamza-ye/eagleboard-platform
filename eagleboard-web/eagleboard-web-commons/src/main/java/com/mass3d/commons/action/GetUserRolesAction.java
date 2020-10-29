package com.mass3d.commons.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.mass3d.paging.ActionPagingSupport;
import com.mass3d.user.UserAuthorityGroup;
import com.mass3d.user.UserService;

public class GetUserRolesAction
    extends ActionPagingSupport<UserAuthorityGroup>
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private UserService userService;

    public void setUserService( UserService userService )
    {
        this.userService = userService;
    }

    // -------------------------------------------------------------------------
    // Input & Output
    // -------------------------------------------------------------------------

    private List<UserAuthorityGroup> userRoles;

    public List<UserAuthorityGroup> getUserRoles()
    {
        return this.userRoles;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    @Override
    public String execute()
        throws Exception
    {
        userRoles = new ArrayList<>( userService.getAllUserAuthorityGroups() );

        userService.canIssueFilter( userRoles );
        
        Collections.sort( userRoles );

        if ( usePaging )
        {
            this.paging = createPaging( userRoles.size() );

            userRoles = userRoles.subList( paging.getStartPos(), paging.getEndPos() );
        }

        return SUCCESS;
    }

}
