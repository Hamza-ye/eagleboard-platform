package com.mass3d.trackedentity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.mass3d.common.BaseIdentifiableObject;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.dataelement.DataElement;
import com.mass3d.program.ProgramStage;

@JacksonXmlRootElement( localName = "dataElementDimension", namespace = DxfNamespaces.DXF_2_0 )
public class TrackedEntityDataElementDimension
{
    private int id;

    /**
     * Data element.
     */
    private DataElement dataElement;

//    /**
//     * Legend set.
//     */
//    private LegendSet legendSet;

    /**
     * Program stage.
     */
    private ProgramStage programStage;

    /**
     * Operator and filter on this format:
     * <operator>:<filter>;<operator>:<filter>
     * Operator and filter pairs can be repeated any number of times.
     */
    private String filter;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public TrackedEntityDataElementDimension()
    {
    }

    public TrackedEntityDataElementDimension( DataElement dataElement, ProgramStage programStage, String filter )
    {
        this.dataElement = dataElement;
//        this.legendSet = legendSet;
        this.programStage = programStage;
        this.filter = filter;
    }

    // -------------------------------------------------------------------------
    // Logic
    // -------------------------------------------------------------------------

    public String getUid()
    {
        return dataElement != null ? dataElement.getUid() : null;
    }

    public String getDisplayName()
    {
        return dataElement != null ? dataElement.getDisplayName() : null;
    }

    @Override
    public String toString()
    {
        return "{" +
            "\"class\":\"" + getClass() + "\", " +
            "\"id\":\"" + id + "\", " +
            "\"dataElement\":" + dataElement + ", " +
//            "\"legendSet\":" + legendSet + ", " +
            "\"programStage\":" + programStage + ", " +
            "\"filter\":\"" + filter + "\" " +
            "}";
    }

    @Override
    public int hashCode()
    {
        int result = id;
        result = 31 * result + (dataElement != null ? dataElement.hashCode() : 0);
//        result = 31 * result + (legendSet != null ? legendSet.hashCode() : 0);
        result = 31 * result + (programStage != null ? programStage.hashCode() : 0);
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

        final TrackedEntityDataElementDimension other = (TrackedEntityDataElementDimension) o;

        if ( dataElement != null ? !dataElement.equals( other.dataElement ) : other.dataElement != null )
        {
            return false;
        }

//        if ( legendSet != null ? !legendSet.equals( other.legendSet ) : other.legendSet != null )
//        {
//            return false;
//        }

        if ( programStage != null ? !programStage.equals( other.programStage ) : other.programStage != null )
        {
            return false;
        }

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
    public DataElement getDataElement()
    {
        return dataElement;
    }

    public void setDataElement( DataElement dataElement )
    {
        this.dataElement = dataElement;
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
    @JsonSerialize( as = BaseIdentifiableObject.class )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public ProgramStage getProgramStage()
    {
        return programStage;
    }

    public void setProgramStage( ProgramStage programStage )
    {
        this.programStage = programStage;
    }

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
