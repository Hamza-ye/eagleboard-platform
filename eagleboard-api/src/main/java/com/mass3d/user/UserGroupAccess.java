package com.mass3d.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.google.common.base.MoreObjects;
import java.io.Serializable;
import java.util.Objects;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.common.EmbeddedObject;

@JacksonXmlRootElement( localName = "userGroupAccess", namespace = DxfNamespaces.DXF_2_0 )
public class UserGroupAccess
    implements Serializable, EmbeddedObject
{
    private int id;

    private String access;

    private UserGroup userGroup;

    private transient String uid;

    public UserGroupAccess()
    {
    }

    public UserGroupAccess( UserGroup userGroup, String access )
    {
        this.userGroup = userGroup;
        this.access = access;
    }

    public int getId()
    {
        return id;
    }

    @JsonIgnore
    public void setId( int id )
    {
        this.id = id;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getAccess()
    {
        return access;
    }

    public void setAccess( String access )
    {
        this.access = access;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getUserGroupUid()
    {
        return userGroup != null ? userGroup.getUid() : null;
    }

    @JsonProperty( "id" )
    @JacksonXmlProperty( localName = "id", namespace = DxfNamespaces.DXF_2_0 )
    public String getUid()
    {
        return uid != null ? uid : (userGroup != null ? userGroup.getUid() : null);
    }

    public void setUid( String uid )
    {
        this.uid = uid;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String displayName()
    {
        return userGroup != null ? userGroup.getDisplayName() : null;
    }

    @JsonIgnore
    public UserGroup getUserGroup()
    {
        if ( userGroup == null )
        {
            UserGroup userGroup = new UserGroup();
            userGroup.setUid( uid );
            return userGroup;
        }

        return userGroup;
    }

    /**
     * Check if the given {@link User} is contained in the {@link UserGroup}.
     *
     * @param user a {@link User}.
     * @return true if the {@link User} is part of this UserGroup members list.
     */
    public boolean userGroupContainsUser( User user )
    {
        if ( userGroup != null )
        {
            return userGroup.getMembers().stream().anyMatch( u -> u.getId() == user.getId() );
        }

        return false;
    }

    @JsonProperty
    public void setUserGroup( UserGroup userGroup )
    {
        this.userGroup = userGroup;
    }

    @Override
    public boolean equals( Object o )
    {
        if ( this == o )
        {
            return true;
        }

        if ( o == null || getClass() != o.getClass() )
        {
            return false;
        }

        UserGroupAccess that = (UserGroupAccess) o;

        return Objects.equals( access, that.access ) && Objects.equals( getUid(), that.getUid() );
    }

    @Override
    public int hashCode()
    {
        return Objects.hash( access, getUid() );
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper( this )
            .add( "uid", getUid() )
            .add( "access", access )
            .toString();
    }
}
