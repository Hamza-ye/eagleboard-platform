package com.mass3d.user;

import com.google.common.base.MoreObjects;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Getter;

@Getter
public class UserQueryParams
{
    private String query;

    private String phoneNumber;

    private User user;

    private boolean canManage;

    private boolean authSubset;

    private boolean disjointRoles;

    private Date lastLogin;

    private Date inactiveSince;

    private Date passwordLastUpdated;

    private Integer inactiveMonths;

    private boolean selfRegistered;

    private boolean isNot2FA;

    private UserInvitationStatus invitationStatus;

//    private List<OrganisationUnit> organisationUnits = new ArrayList<>();

    private Set<UserGroup> userGroups = new HashSet<>();

    private Integer first;

    private Integer max;

    private boolean userOrgUnits;

    private boolean includeOrgUnitChildren;

    private boolean prefetchUserGroups;

    private Boolean disabled;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public UserQueryParams()
    {
    }

    public UserQueryParams( User user )
    {
        this.user = user;
    }

    // -------------------------------------------------------------------------
    // toString
    // -------------------------------------------------------------------------

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper( this )
            .add( "query", query )
            .add( "phone number", phoneNumber )
            .add( "user", user != null ? user.getUsername() : null )
            .add( "can manage", canManage )
            .add( "auth subset", authSubset )
            .add( "disjoint roles", disjointRoles )
            .add( "last login", lastLogin )
            .add( "inactive since", inactiveSince )
            .add( "passwordLastUpdated", passwordLastUpdated )
            .add( "inactive months", inactiveMonths )
            .add( "self registered", selfRegistered )
            .add( "isNot2FA", isNot2FA )
            .add( "invitation status", invitationStatus )
            .add( "first", first )
            .add( "max", max )
            .add( "includeOrgUnitChildren", includeOrgUnitChildren )
            .add( "prefetchUserGroups", prefetchUserGroups )
            .add( "disabled", disabled ).toString();
    }

    // -------------------------------------------------------------------------
    // Builder
    // -------------------------------------------------------------------------

//    public UserQueryParams addOrganisationUnit( OrganisationUnit unit )
//    {
//        this.organisationUnits.add( unit );
//        return this;
//    }

//    public boolean hashasTodoTasks()
//    {
//        return !organisationUnits.isEmpty();
//    }

    public boolean hasUserGroups()
    {
        return !userGroups.isEmpty();
    }

    public boolean hasUser()
    {
        return user != null;
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    public UserQueryParams setQuery( String query )
    {
        this.query = query;
        return this;
    }

    public UserQueryParams setPhoneNumber( String phoneNumber )
    {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public UserQueryParams setUser( User user )
    {
        this.user = user;
        return this;
    }

    public UserQueryParams setCanManage( boolean canManage )
    {
        this.canManage = canManage;
        return this;
    }

    public UserQueryParams setAuthSubset( boolean authSubset )
    {
        this.authSubset = authSubset;
        return this;
    }

    public UserQueryParams setDisjointRoles( boolean disjointRoles )
    {
        this.disjointRoles = disjointRoles;
        return this;
    }

    public UserQueryParams setLastLogin( Date lastLogin )
    {
        this.lastLogin = lastLogin;
        return this;
    }

    public UserQueryParams setInactiveSince( Date inactiveSince )
    {
        this.inactiveSince = inactiveSince;
        return this;
    }

    public UserQueryParams setInactiveMonths( Integer inactiveMonths )
    {
        this.inactiveMonths = inactiveMonths;
        return this;
    }

    public UserQueryParams setSelfRegistered( boolean selfRegistered )
    {
        this.selfRegistered = selfRegistered;
        return this;
    }

    public UserQueryParams setNot2FA( boolean isNot2FA )
    {
        this.isNot2FA = isNot2FA;
        return this;
    }

    public UserQueryParams setInvitationStatus( UserInvitationStatus invitationStatus )
    {
        this.invitationStatus = invitationStatus;
        return this;
    }

//    public UserQueryParams setTodoTasks( List<OrganisationUnit> organisationUnits )
//    {
//        this.organisationUnits = organisationUnits;
//        return this;
//    }

    public UserQueryParams setUserGroups( Set<UserGroup> userGroups )
    {
        this.userGroups = userGroups;
        return this;
    }

    public UserQueryParams setFirst( Integer first )
    {
        this.first = first;
        return this;
    }

    public UserQueryParams setMax( Integer max )
    {
        this.max = max;
        return this;
    }

    public UserQueryParams setUserOrgUnits( boolean userOrgUnits )
    {
        this.userOrgUnits = userOrgUnits;
        return this;
    }

    public UserQueryParams setIncludeOrgUnitChildren( boolean includeOrgUnitChildren )
    {
        this.includeOrgUnitChildren = includeOrgUnitChildren;
        return this;
    }

    public UserQueryParams setPrefetchUserGroups( boolean prefetchUserGroups )
    {
        this.prefetchUserGroups = prefetchUserGroups;
        return this;
    }

    public UserQueryParams setPasswordLastUpdated( Date passwordLastUpdated )
    {
        this.passwordLastUpdated = passwordLastUpdated;
        return this;
    }

    public UserQueryParams setDisabled( Boolean disabled )
    {
        this.disabled = disabled;
        return this;
    }
}
