package com.mass3d.programstagefilter;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import java.util.Date;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.period.RelativePeriodEnum;

/**
 * Filtering parameters for date type.
 * 
 *
 */
public class DateFilterPeriod implements Serializable
{
    private static final long serialVersionUID = 1L;

    /**
     * An integer referring to relative startDate based on the current date.
     */
    private int startBuffer;

    /**
     * An integer referring to relative startDate based on the current date.
     */
    private int endBuffer;
    
    /**
     * An absolute start date
     */
    private Date startDate;
    
    /**
     * An absolute end date
     */
    private Date endDate;
    
    /**
     * Relative period.
     */
    private RelativePeriodEnum period;
    
    /**
     * Enum indicating whether this date filter is absolute or relative
     */
    private DatePeriodType type;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public DateFilterPeriod()
    {

    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public int getEndBuffer()
    {
        return endBuffer;
    }

    public void setEndBuffer( int to )
    {
        this.endBuffer = to;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public RelativePeriodEnum getPeriod()
    {
        return period;
    }

    public void setPeriod( RelativePeriodEnum relativePeriod )
    {
        this.period = relativePeriod;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public int getStartBuffer()
    {
        return startBuffer;
    }

    public void setStartBuffer( int startBuffer )
    {
        this.startBuffer = startBuffer;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public Date getStartDate()
    {
        return startDate;
    }

    public void setStartDate( Date startDate )
    {
        this.startDate = startDate;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public Date getEndDate()
    {
        return endDate;
    }

    public void setEndDate( Date endDate )
    {
        this.endDate = endDate;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public DatePeriodType getType()
    {
        return type;
    }

    public void setType( DatePeriodType type )
    {
        this.type = type;
    }
    
}
