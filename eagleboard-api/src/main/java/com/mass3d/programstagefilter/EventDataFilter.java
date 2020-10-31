package com.mass3d.programstagefilter;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import java.util.Set;
import com.mass3d.common.DxfNamespaces;

/**
 * Filter parameters to be used for filtering data element values.
 * 
 *
 */
public class EventDataFilter implements Serializable
{
    private static final long serialVersionUID = 1L;

    /**
     * The data element id or data item
     */
    private String dataItem;

    /**
     * Less than or equal to
     */
    private String le;
    
    /**
     * Greater than or equal to
     */
    private String ge;
    
    /**
     * Greater than
     */
    private String gt;
    
    /**
     * Lesser than
     */
    private String lt;
    
    /**
     * Equal to
     */
    private String eq;
    
    /**
     * In a list
     */
    private Set<String> in;
    
    /**
     * Like
     */
    private String like;
    
    /**
     * If the dataItem is of type date, then date filtering parameters are specified using this.
     */
    private DateFilterPeriod dateFilter;
    
    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public EventDataFilter()
    {

    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getDataItem()
    {
        return dataItem;
    }

    public void setDataItem( String data )
    {
        this.dataItem = data;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getLe()
    {
        return le;
    }

    public void setLe( String le )
    {
        this.le = le;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getGe()
    {
        return ge;
    }

    public void setGe( String ge )
    {
        this.ge = ge;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getGt()
    {
        return gt;
    }

    public void setGt( String gt )
    {
        this.gt = gt;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getLt()
    {
        return lt;
    }

    public void setLt( String lt )
    {
        this.lt = lt;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getEq()
    {
        return eq;
    }

    public void setEq( String eq )
    {
        this.eq = eq;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public Set<String> getIn()
    {
        return in;
    }

    public void setIn( Set<String> in )
    {
        this.in = in;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getLike()
    {
        return like;
    }

    public void setLike( String like )
    {
        this.like = like;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public DateFilterPeriod getDateFilter()
    {
        return dateFilter;
    }

    public void setDateFilter( DateFilterPeriod dateFilter )
    {
        this.dateFilter = dateFilter;
    }
    
    

}
