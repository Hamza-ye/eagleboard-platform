package com.mass3d.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class BaseDataDimensionalItemObject
    extends BaseDimensionalItemObject implements DataDimensionalItemObject
{
    /**
     * The category option combo identifier used for aggregated data exports through
     * analytics, can be null.
     */
    protected String aggregateExportCategoryOptionCombo;

    /**
     * The attribute option combo identifier used for aggregated data exports through
     * analytics, can be null.
     */
    protected String aggregateExportAttributeOptionCombo;

    // -------------------------------------------------------------------------
    // Logic
    // -------------------------------------------------------------------------

    @Override
    public boolean hasAggregateExportCategoryOptionCombo()
    {
        return aggregateExportCategoryOptionCombo != null;
    }

    // -------------------------------------------------------------------------
    // Get and set methods
    // -------------------------------------------------------------------------

    @Override
    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getAggregateExportCategoryOptionCombo()
    {
        return aggregateExportCategoryOptionCombo;
    }

    public void setAggregateExportCategoryOptionCombo( String aggregateExportCategoryOptionCombo )
    {
        this.aggregateExportCategoryOptionCombo = aggregateExportCategoryOptionCombo;
    }

    public void setAggregateExportAttributeOptionCombo( String aggregateExportAttributeOptionCombo )
    {
        this.aggregateExportAttributeOptionCombo = aggregateExportAttributeOptionCombo;
    }
}
