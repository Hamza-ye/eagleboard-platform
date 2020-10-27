package com.mass3d.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.ArrayList;
import java.util.List;
import com.mass3d.common.DxfNamespaces;

/**
 * User collection wrapper. Mainly used for doing bulk invites.
 *
 */
@JacksonXmlRootElement( localName = "users", namespace = DxfNamespaces.DXF_2_0 )
public class Users
{
    private List<User> users = new ArrayList<>();

    public Users()
    {
    }

    @JsonProperty
    @JacksonXmlElementWrapper( useWrapping = false, namespace = DxfNamespaces.DXF_2_0 )
    @JacksonXmlProperty( localName = "user", namespace = DxfNamespaces.DXF_2_0 )
    public List<User> getUsers()
    {
        return users;
    }

    public void setUsers( List<User> users )
    {
        this.users = users;
    }
}
