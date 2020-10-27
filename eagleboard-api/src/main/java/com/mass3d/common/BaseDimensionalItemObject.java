package com.mass3d.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.mass3d.analytics.AggregationType;
import javax.persistence.MappedSuperclass;

//@MappedSuperclass
public class BaseDimensionalItemObject
    extends BaseNameableObject
    implements DimensionalItemObject
{
    /**
     * The dimension type.
     */
    private DimensionItemType dimensionItemType;

    /**
     * The aggregation type for this dimension.
     */
    protected AggregationType aggregationType;

    /**
     * A value representing a period offset that can be applied to Dimensional Item
     * Object within a Indicator formula
     */
    protected transient int periodOffset = 0;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public BaseDimensionalItemObject()
    {
    }

    public BaseDimensionalItemObject( String dimensionItem )
    {
        this.uid = dimensionItem;
        this.code = dimensionItem;
        this.name = dimensionItem;
    }

    // -------------------------------------------------------------------------
    // DimensionalItemObject
    // -------------------------------------------------------------------------

    @Override
    public boolean hasAggregationType()
    {
        return getAggregationType() != null;
    }

    @Override
    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getDimensionItem()
    {
        return getUid();
    }

    @Override
    public String getDimensionItem( IdScheme idScheme )
    {
        return getPropertyValue( idScheme );
    }

    @Override
    public TotalAggregationType getTotalAggregationType()
    {
        return TotalAggregationType.SUM;
    }

    // -------------------------------------------------------------------------
    // Get and set methods
    // -------------------------------------------------------------------------

    @Override
    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public DimensionItemType getDimensionItemType()
    {
        return dimensionItemType;
    }

    public void setDimensionItemType( DimensionItemType dimensionItemType )
    {
        this.dimensionItemType = dimensionItemType;
    }

    @Override
    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public AggregationType getAggregationType()
    {
        return aggregationType;
    }

    public void setAggregationType( AggregationType aggregationType )
    {
        this.aggregationType = aggregationType;
    }

    @Override
    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public int getPeriodOffset()
    {
        return periodOffset;
    }

    public void setPeriodOffset( int periodOffset )
    {
        this.periodOffset = periodOffset;
    }

    @Override
    public boolean equals( Object o )
    {
        if ( !super.equals( o ) )
        {
            return false;
        }

        final BaseDimensionalItemObject that = (BaseDimensionalItemObject) o;

        return periodOffset == that.periodOffset;
    }

    @Override
    public int hashCode()
    {
        int result = super.hashCode();
        result = 31 * result + periodOffset;
        return result;
    }
}
