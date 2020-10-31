package com.mass3d.attribute;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.io.Serializable;
import java.util.Objects;
import com.mass3d.common.CustomAttributeSerializer;
import com.mass3d.common.DxfNamespaces;

@JacksonXmlRootElement( localName = "attributeValues", namespace = DxfNamespaces.DXF_2_0 )
public class AttributeValue
    implements Serializable
{
    /**
     * Determines if a de-serialized file is compatible with this class.
     */
    private static final long serialVersionUID = -6625127769248931066L;

    private Attribute attribute;

    private String value;

    public AttributeValue()
    {
    }

    public AttributeValue( String value )
    {
        this();
        this.value = value;
    }

    public AttributeValue( String value, Attribute attribute )
    {
        this.value = value;
        this.attribute = attribute;
    }

    public AttributeValue( Attribute attribute, String value )
    {
        this.value = value;
        this.attribute = attribute;
    }

    @Override
    public boolean equals( Object o )
    {
        if ( this == o ) return true;
        if ( o == null || getClass() != o.getClass() ) return false;

        AttributeValue that = ( AttributeValue ) o;

        if ( !Objects.equals( attribute, that.attribute ) ) return false;
        if ( !Objects.equals( value, that.value ) ) return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = 7;
        result = 31 * result + (attribute != null ? attribute.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    @Override
    public String toString()
    {
        return "AttributeValue{" +
                "class=" + getClass() +
                ", value='" + value + '\'' +
            ", attribute='" + attribute + '\'' +
                '}';
    }

    @JsonProperty
    @JacksonXmlProperty
    public String getValue()
    {
        return value;
    }

    public void setValue( String value )
    {
        this.value = value;
    }

    @JsonProperty
    @JsonSerialize( using = CustomAttributeSerializer.class )
    public Attribute getAttribute()
    {
        return attribute;
    }

    public void setAttribute( Attribute attribute )
    {
        this.attribute = attribute;
    }
}
