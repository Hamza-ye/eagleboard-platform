package com.mass3d.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.UUID;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.schema.annotation.PropertyTransformer;
import com.mass3d.schema.transformer.UserPropertyTransformer;
import com.mass3d.user.User;

@JacksonXmlRootElement( localName = "userMessage", namespace = DxfNamespaces.DXF_2_0 )
public class UserMessage
{
    private int id;

    private String key;

    private User user;

    private boolean read;

    private boolean followUp;

    private transient String lastRecipientSurname;

    private transient String lastRecipientFirstname;

    public String getLastRecipientSurname()
    {
        return lastRecipientSurname;
    }

    public void setLastRecipientSurname( String lastRecipientSurname )
    {
        this.lastRecipientSurname = lastRecipientSurname;
    }

    public String getLastRecipientFirstname()
    {
        return lastRecipientFirstname;
    }

    public void setLastRecipientFirstname( String lastRecipientFirstname )
    {
        this.lastRecipientFirstname = lastRecipientFirstname;
    }

    public String getLastRecipientName()
    {
        return lastRecipientFirstname + " " + lastRecipientSurname;
    }

    public UserMessage()
    {
        this.key = UUID.randomUUID().toString();
    }

    public UserMessage( User user )
    {
        this.key = UUID.randomUUID().toString();
        this.user = user;
        this.read = false;
    }

    public UserMessage( User user, boolean read )
    {
        this.key = UUID.randomUUID().toString();
        this.user = user;
        this.read = read;
    }

    public int getId()
    {
        return id;
    }

    public void setId( int id )
    {
        this.id = id;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getKey()
    {
        return key;
    }

    public void setKey( String key )
    {
        this.key = key;
    }

    @JsonProperty
    @JsonSerialize( using = UserPropertyTransformer.JacksonSerialize.class )
    @JsonDeserialize( using = UserPropertyTransformer.JacksonDeserialize.class )
    @PropertyTransformer( UserPropertyTransformer.class )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public User getUser()
    {
        return user;
    }

    public void setUser( User user )
    {
        this.user = user;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public boolean isRead()
    {
        return read;
    }

    public void setRead( boolean read )
    {
        this.read = read;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public boolean isFollowUp()
    {
        return followUp;
    }

    public void setFollowUp( boolean followUp )
    {
        this.followUp = followUp;
    }

    @Override
    public int hashCode()
    {
        return key.hashCode();
    }

    @Override
    public boolean equals( Object object )
    {
        if ( this == object )
        {
            return true;
        }

        if ( object == null )
        {
            return false;
        }

        if ( getClass() != object.getClass() )
        {
            return false;
        }

        final UserMessage other = (UserMessage) object;

        return key.equals( other.key );
    }

    @Override
    public String toString()
    {
        return "[User: " + user + ", read: " + read + "]";
    }
}
