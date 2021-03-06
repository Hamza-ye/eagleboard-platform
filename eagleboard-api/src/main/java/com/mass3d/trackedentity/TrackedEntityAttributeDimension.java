package com.mass3d.trackedentity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.mass3d.common.BaseIdentifiableObject;
import com.mass3d.common.DxfNamespaces;

@JacksonXmlRootElement( localName = "attributeDimension", namespace = DxfNamespaces.DXF_2_0 )
public class TrackedEntityAttributeDimension
{
    private int id;

    /**
     * Attribute.
     */
    private TrackedEntityAttribute attribute;

//    /**
//     * Legend set.
//     */
//    private LegendSet legendSet;

    /**
     * Operator and filter on this format:
     * <operator>:<filter>;<operator>:<filter>
     * Operator and filter pairs can be repeated any number of times.
     */
    private String filter;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public TrackedEntityAttributeDimension()
    {
    }

    public TrackedEntityAttributeDimension( TrackedEntityAttribute attribute, String filter )
    {
        this.attribute = attribute;
//        this.legendSet = legendSet;
        this.filter = filter;
    }

    // -------------------------------------------------------------------------
    // Logic
    // -------------------------------------------------------------------------

    public String getUid()
    {
        return attribute != null ? attribute.getUid() : null;
    }

    public String getDisplayName()
    {
        return attribute != null ? attribute.getDisplayName() : null;
    }

    @Override
    public String toString()
    {
        return "{" +
            "\"class\":\"" + getClass() + "\", " +
            "\"id\":\"" + id + "\", " +
            "\"attribute\":" + attribute + ", " +
//            "\"legendSet\":" + legendSet + ", " +
            "\"filter\":\"" + filter + "\" " +
            "}";
    }

    @Override
    public int hashCode()
    {
        int result = id;
        result = 31 * result + (attribute != null ? attribute.hashCode() : 0);
//        result = 31 * result + (legendSet != null ? legendSet.hashCode() : 0);
        result = 31 * result + (filter != null ? filter.hashCode() : 0);

        return result;
    }

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

        final TrackedEntityAttributeDimension other = (TrackedEntityAttributeDimension) o;

        if ( attribute != null ? !attribute.equals( other.attribute ) : other.attribute != null )
        {
            return false;
        }

//        if ( legendSet != null ? !legendSet.equals( other.legendSet ) : other.legendSet != null )
//        {
//            return false;
//        }

        if ( filter != null ? !filter.equals( other.filter ) : other.filter != null )
        {
            return false;
        }

        return true;
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    public int getId()
    {
        return id;
    }

    public void setId( int id )
    {
        this.id = id;
    }

    @JsonProperty
    @JsonSerialize( as = BaseIdentifiableObject.class )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public TrackedEntityAttribute getAttribute()
    {
        return attribute;
    }

    public void setAttribute( TrackedEntityAttribute attribute )
    {
        this.attribute = attribute;
    }

//    @JsonProperty
//    @JsonSerialize( as = BaseIdentifiableObject.class )
//    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
//    public LegendSet getLegendSet()
//    {
//        return legendSet;
//    }
//
//    public void setLegendSet( LegendSet legendSet )
//    {
//        this.legendSet = legendSet;
//    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getFilter()
    {
        return filter;
    }

    public void setFilter( String filter )
    {
        this.filter = filter;
    }
}
