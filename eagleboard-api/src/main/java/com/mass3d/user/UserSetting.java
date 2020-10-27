package com.mass3d.user;

import java.io.Serializable;

/**
 * TODO make IdentifiableObject
 *
 */
public class UserSetting
    implements Serializable
{
    /**
     * Determines if a de-serialized file is compatible with this class.
     */
    private static final long serialVersionUID = -5436090314407097851L;

    /**
     * Required. Unique together with name.
     */
    private User user;

    /**
     * Required. Unique together with user.
     */
    private String name;

    private Serializable value;

    public UserSetting()
    {
    }

    public UserSetting( User user, String name, Serializable value )
    {
        this.user = user;
        this.name = name;
        this.value = value;
    }

    // -------------------------------------------------------------------------
    // hashCode and equals
    // -------------------------------------------------------------------------

    @Override
    public boolean equals( Object o )
    {
        if ( this == o )
        {
            return true;
        }

        if ( o == null )
        {
            return false;
        }

        if ( !(o instanceof UserSetting) )
        {
            return false;
        }

        final UserSetting other = (UserSetting) o;

        return user.equals( other.getUser() ) && name.equals( other.getName() );
    }

    @Override
    public int hashCode()
    {
        int prime = 31;
        int result = 1;

        result = result * prime + user.hashCode();
        result = result * prime + name.hashCode();

        return result;
    }

    @Override
    public String toString()
    {
        return "{" +
            "\"user\":\"" + user.getUsername() + "\", " +
            "\"name:\":\"" + name + "\", " +
            "\"value\":\"" + value + "\" " +
            "}";
    }

    public boolean hasValue()
    {
        return value != null;
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser( User user )
    {
        this.user = user;
    }

    public Serializable getValue()
    {
        return value;
    }

    public void setValue( Serializable value )
    {
        this.value = value;
    }
}
