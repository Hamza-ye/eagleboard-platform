package com.mass3d.mock;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import com.mass3d.user.CurrentUserService;
import com.mass3d.user.User;
import com.mass3d.user.UserAuthorityGroup;
import com.mass3d.user.UserCredentials;
import com.mass3d.user.UserInfo;

public class MockCurrentUserService
    implements CurrentUserService
{
    private User currentUser;

    private boolean superUserFlag;

    public MockCurrentUserService( User currentUser )
    {
        this.currentUser = currentUser;
    }

    public MockCurrentUserService( /* Set<OrganisationUnit> organisationUnits, Set<OrganisationUnit> dataViewOrganisationUnits,*/ String... auths )
    {
        this( true, /* organisationUnits, dataViewOrganisationUnits, */ auths );
    }

    public MockCurrentUserService( boolean superUserFlag, /* Set<OrganisationUnit> organisationUnits, Set<OrganisationUnit> dataViewOrganisationUnits, */ String... auths )
    {
        UserAuthorityGroup userRole = new UserAuthorityGroup();
        userRole.setAutoFields();
        userRole.getAuthorities().addAll( Arrays.asList( auths ) );

        this.superUserFlag = superUserFlag;
        UserCredentials credentials = new UserCredentials();
        credentials.setUsername( "currentUser" );
        credentials.getUserAuthorityGroups().add( userRole );
        credentials.setAutoFields();

        User user = new User();
        user.setFirstName( "Current" );
        user.setSurname( "User" );
//        user.setTodoTasks( organisationUnits );
//        user.setDataViewOrganisationUnits( dataViewOrganisationUnits );
        user.setUserCredentials( credentials );
        user.setAutoFields();
        credentials.setUserInfo( user );
        credentials.setUser( user );

        this.currentUser = user;
    }

    @Override
    public String getCurrentUsername()
    {
        return currentUser.getUsername();
    }

    @Override
    public User getCurrentUser()
    {
        return currentUser;
    }

    @Override
    public UserInfo getCurrentUserInfo()
    {
        return new UserInfo( currentUser.getId(),
            currentUser.getUsername(), currentUser.getUserCredentials().getAllAuthorities() );
    }

//    @Override
//    public Set<OrganisationUnit> getCurrentUserOrganisationUnits()
//    {
//        return currentUser != null ? currentUser.getTodoTasks() : new HashSet<>();
//    }

    @Override
    public boolean currentUserIsSuper()
    {
        return superUserFlag;
    }

    @Override
    public boolean currentUserIsAuthorized( String auth )
    {
        return true;
    }

    @Override
    public UserCredentials getCurrentUserCredentials()
    {
        return currentUser.getUserCredentials();
    }
}
