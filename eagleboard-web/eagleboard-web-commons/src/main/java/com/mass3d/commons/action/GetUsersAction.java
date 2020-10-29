package com.mass3d.commons.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import org.apache.struts2.ServletActionContext;
import com.mass3d.paging.ActionPagingSupport;
import com.mass3d.user.User;
import com.mass3d.user.UserService;
import com.mass3d.user.comparator.UserComparator;
import com.mass3d.util.ContextUtils;

public class GetUsersAction
    extends ActionPagingSupport<User>
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

    private String key;

    public void setKey( String key )
    {
        this.key = key;
    }

    private List<User> users;

    public List<User> getUsers()
    {
        return users;
    }

    // -------------------------------------------------------------------------
    // Action Implementation
    // -------------------------------------------------------------------------

    @Override
    public String execute()
        throws Exception
    {
        //TODO: Allow user with F_USER_VIEW_WITHIN_MANAGED_GROUP and restrict viewing to within managed groups.

        users = new ArrayList<>( userService.getAllUsers() );

        ContextUtils.clearIfNotModified( ServletActionContext.getRequest(), ServletActionContext.getResponse(), users );
        
        if ( key != null )
        {
            filterByKey( key, true );
        }

        Collections.sort( users, new UserComparator() );

        if ( usePaging )
        {
            this.paging = createPaging( users.size() );

            users = users.subList( paging.getStartPos(), paging.getEndPos() );
        }

        return SUCCESS;
    }

    private void filterByKey( String key, boolean ignoreCase )
    {
        ListIterator<User> iterator = users.listIterator();

        if ( ignoreCase )
        {
            key = key.toLowerCase();
        }

        while ( iterator.hasNext() )
        {
            User user = iterator.next();
            String name = ignoreCase ? user.getName().toLowerCase() : user.getName();

            if ( name.indexOf( key ) == -1 )
            {
                iterator.remove();
            }
        }
    }
}
