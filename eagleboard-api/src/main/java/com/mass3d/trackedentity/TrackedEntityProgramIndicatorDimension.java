package com.mass3d.trackedentity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.mass3d.common.BaseIdentifiableObject;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.program.ProgramIndicator;

@JacksonXmlRootElement( localName = "programIndicatorDimension", namespace = DxfNamespaces.DXF_2_0 )
public class TrackedEntityProgramIndicatorDimension
{
    private int id;

    /**
     * Program indicator.
     */
    private ProgramIndicator programIndicator;

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

    public TrackedEntityProgramIndicatorDimension()
    {
    }

    public TrackedEntityProgramIndicatorDimension( ProgramIndicator programIndicator, String filter )
    {
        this.programIndicator = programIndicator;
//        this.legendSet = legendSet;
        this.filter = filter;
    }

    // -------------------------------------------------------------------------
    // Logic
    // -------------------------------------------------------------------------

    public String getUid()
    {
        return programIndicator != null ? programIndicator.getUid() : null;
    }

    public String getDisplayName()
    {
        return programIndicator != null ? programIndicator.getDisplayName() : null;
    }

    @Override
    public String toString()
    {
        return "[Id: " + id + ", program indicator: " + programIndicator + ", "
//            + "legend set: " + legendSet + ", "
            + "filter: " + filter + "]";
    }

    @Override
    public int hashCode()
    {
        int result = id;
        result = 31 * result + (programIndicator != null ? programIndicator.hashCode() : 0);
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

        final TrackedEntityProgramIndicatorDimension other = (TrackedEntityProgramIndicatorDimension) o;

        if ( programIndicator != null ? !programIndicator.equals( other.programIndicator ) : other.programIndicator != null )
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
    public ProgramIndicator getProgramIndicator()
    {
        return programIndicator;
    }

    public void setProgramIndicator( ProgramIndicator programIndicator )
    {
        this.programIndicator = programIndicator;
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
