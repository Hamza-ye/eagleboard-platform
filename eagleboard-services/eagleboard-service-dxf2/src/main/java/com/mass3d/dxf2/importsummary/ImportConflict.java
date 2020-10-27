package com.mass3d.dxf2.importsummary;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.mass3d.common.DxfNamespaces;

@JacksonXmlRootElement( localName = "conflict", namespace = DxfNamespaces.DXF_2_0 )
public class ImportConflict
{
    private String object;

    private String value;

    public ImportConflict()
    {
    }
    
    public ImportConflict( String object, String value )
    {
        this.object = object;
        this.value = value;
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    @JsonProperty
    @JacksonXmlProperty( isAttribute = true )
    public String getObject()
    {
        return object;
    }

    public void setObject( String object )
    {
        this.object = object;
    }

    @JsonProperty
    @JacksonXmlProperty( isAttribute = true )
    public String getValue()
    {
        return value;
    }

    public void setValue( String value )
    {
        this.value = value;
    }

    // -------------------------------------------------------------------------
    // equals, hashCode, toString
    // -------------------------------------------------------------------------

    @Override
    public int hashCode()
    {
        int result = object != null ? object.hashCode() : 0;
        result = 31 * result + ( value != null ? value.hashCode() : 0 );

        return result;
    }

    /**
     * Class check uses isAssignableFrom and get-methods to handle proxied objects.
     */
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

        if ( !getClass().isAssignableFrom( o.getClass() ) )
        {
            return false;
        }

        final ImportConflict other = (ImportConflict) o;

        if ( object != null ? !object.equals( other.object ) : other.object != null )
        {
            return false;
        }

        if ( value != null ? !value.equals( other.value ) : other.value != null )
        {
            return false;
        }

        return true;
    }

    @Override
    public String toString()
    {
        return "ImportConflict{" +
            "object='" + object + '\'' +
            ", value='" + value + '\'' +
            '}';
    }
}
