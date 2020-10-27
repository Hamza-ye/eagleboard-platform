package com.mass3d.keyjsonvalue;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.mass3d.common.BaseIdentifiableObject;

public class KeyJsonValue
    extends BaseIdentifiableObject
{
    /**
     * A namespace represents a collection of keys
     */
    private String namespace;

    /**
     * A key belongs to a namespace, and represent a value
     */
    private String key;

    /**
     * A value referenced by a key and namespace, JSON-formatted data stored as a string but in a jsonb column.
     */
    private String jbPlainValue;

    /**
     * Whether or not this KeyJsonValue is encrypted or not. Default is false.
     */
    private Boolean encrypted = false;

    /**
     * Encrypted value if encrypted is set to true
     */
    private String encryptedValue;

    /**
     * Temporary variable to hold any new values set during session. Will be made into the correct type
     * when being persisted by the persistence layer (encrypted or plain).
     */
    private String value;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public KeyJsonValue()
    {
    }
    
    public KeyJsonValue( String namespace, String key, String value, Boolean encrypted )
    {
        this.namespace = namespace;
        this.key = key;
        this.value = value;
        this.encrypted = encrypted;
    }

    // -------------------------------------------------------------------------
    // toString
    // -------------------------------------------------------------------------
    
    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper( this )
            .add( "namespace", namespace )
            .add( "key", key )
            .add( "value", value ).toString();
    }
    
    // -------------------------------------------------------------------------
    // Get and set methods
    // -------------------------------------------------------------------------
    
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

    public boolean isEncrypted()
    {
        return encrypted;
    }

    public void setEncrypted( boolean encrypted )
    {
        this.encrypted = encrypted;
    }

    public String getJbPlainValue()
    {
        return !this.encrypted && this.value != null ? this.value : this.jbPlainValue;
    }

    public void setJbPlainValue( String jbPlainValue )
    {
        this.jbPlainValue = jbPlainValue;
    }

    public String getEncryptedValue()
    {
        return this.encrypted && this.value != null ? this.value : this.encryptedValue;
    }

    public void setEncryptedValue( String encryptedValue )
    {
        this.encryptedValue = encryptedValue;
    }
}
