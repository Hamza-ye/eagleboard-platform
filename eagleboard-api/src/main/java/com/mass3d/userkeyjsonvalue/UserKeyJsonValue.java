package com.mass3d.userkeyjsonvalue;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mass3d.common.BaseIdentifiableObject;
import com.mass3d.schema.annotation.PropertyTransformer;
import com.mass3d.schema.transformer.UserPropertyTransformer;
import com.mass3d.user.User;

public class UserKeyJsonValue
    extends BaseIdentifiableObject
{
    /**
     * The user which owns this UserKeyJsonValue
     */
    private User user;

    /**
     * A namespace is a collection of keys for a given user
     */
    private String namespace;

    /**
     * A key belongs to a namespace and user, and represent a value
     */
    private String key;

    /**
     * A value referenced by a key, namespace and user, JSON-formatted data stored as a jsonb in db
     */
    private String jbPlainValue;

    /**
     * The encrypted value of the object, if encrypted is true
     */
    private String encryptedValue;

    /**
     * Indicates whether the value should be encrypted or not.
     */
    private Boolean encrypted = false;

    /**
     * Temporary variable to hold any new values set during session. Will be made into the correct type
     * when being persisted by the persistence layer (encrypted or plain).
     */
    private String value;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public UserKeyJsonValue()
    {
    }

    public UserKeyJsonValue( User user, String namespace, String key, String value, Boolean encrypted )
    {
        this.user = user;
        this.namespace = namespace;
        this.key = key;
        this.value = value;
        this.encrypted = encrypted;
    }

    // -------------------------------------------------------------------------
    // Get and set methods
    // -------------------------------------------------------------------------

    @JsonProperty
    @JsonSerialize( using = UserPropertyTransformer.JacksonSerialize.class )
    @JsonDeserialize( using = UserPropertyTransformer.JacksonDeserialize.class )
    @PropertyTransformer( UserPropertyTransformer.class )
    public User getUser()
    {
        return user;
    }

    public void setUser( User user )
    {
        this.user = user;
    }

    @JsonProperty
    public String getNamespace()
    {
        return namespace;
    }

    public void setNamespace( String namespace )
    {
        this.namespace = namespace;
    }

    @JsonProperty
    public String getKey()
    {
        return key;
    }

    public void setKey( String key )
    {
        this.key = key;
    }

    @JsonProperty
    public String getValue()
    {
        return encrypted ? encryptedValue : jbPlainValue;
    }

    public void setValue( String value )
    {
        this.value = value;
    }

    public String getJbPlainValue()
    {
        return !encrypted && value != null ? value : jbPlainValue;
    }

    public void setJbPlainValue( String jbPlainValue )
    {
        this.jbPlainValue = jbPlainValue;
    }

    public String getEncryptedValue()
    {
        return encrypted && value != null ? value : encryptedValue;
    }

    public void setEncryptedValue( String encryptedValue )
    {
        this.encryptedValue = encryptedValue;
    }

    public boolean getEncrypted()
    {
        return encrypted;
    }

    public void setEncrypted( boolean encrypted )
    {
        this.encrypted = encrypted;
    }
}
