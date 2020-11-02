package com.mass3d.dataset;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.mass3d.category.CategoryCombo;
import com.mass3d.common.BaseIdentifiableObject;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.common.MetadataObject;
import com.mass3d.dataelement.DataElement;
import com.mass3d.dataelement.DataElementOperand;
import com.mass3d.indicator.Indicator;
import com.mass3d.schema.annotation.PropertyRange;

@JacksonXmlRootElement( localName = "section", namespace = DxfNamespaces.DXF_2_0 )
public class Section
    extends BaseIdentifiableObject implements MetadataObject
{
    private String description;

    private DataSet dataSet;

    private List<DataElement> dataElements = new ArrayList<>();

    private List<Indicator> indicators = new ArrayList<>();

    private Set<DataElementOperand> greyedFields = new HashSet<>();

    private int sortOrder;

    private boolean showRowTotals;

    private boolean showColumnTotals;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public Section()
    {
    }

    public Section( String name, DataSet dataSet, List<DataElement> dataElements, Set<DataElementOperand> greyedFields )
    {
        this.name = name;
        this.dataSet = dataSet;
        this.dataElements = dataElements;
        this.greyedFields = greyedFields;
    }

    // -------------------------------------------------------------------------
    // Logic
    // -------------------------------------------------------------------------

    public boolean hasCategoryCombo()
    {
        return !getCategoryCombos().isEmpty();
    }

    @JsonProperty
    @JsonSerialize( as = BaseIdentifiableObject.class )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public Set<CategoryCombo> getCategoryCombos()
    {
        Set<CategoryCombo> categoryCombos = new HashSet<>();

        for ( DataElement dataElement : dataElements )
        {
            CategoryCombo categoryCombo = dataElement.getDataElementCategoryCombo( dataSet );

            if ( categoryCombo != null )
            {
                categoryCombos.add( categoryCombo );
            }
        }

        return categoryCombos;
    }

    public boolean hasDataElements()
    {
        return dataElements != null && !dataElements.isEmpty();
    }

    public List<DataElement> getDataElementsByCategoryCombo( CategoryCombo categoryCombo )
    {
        List<DataElement> dataElements = new ArrayList<>();

        for ( DataElement dataElement : this.dataElements )
        {
            if ( dataElement.getDataElementCategoryCombo( this.dataSet ).equals( categoryCombo ) )
            {
                dataElements.add( dataElement );
            }
        }

        return dataElements;
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    @PropertyRange( min = 2 )
    public String getDescription()
    {
        return description;
    }

    public void setDescription( String description )
    {
        this.description = description;
    }

    @JsonProperty
    @JsonSerialize( as = BaseIdentifiableObject.class )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public DataSet getDataSet()
    {
        return dataSet;
    }

    public void setDataSet( DataSet dataSet )
    {
        this.dataSet = dataSet;
    }

    @JsonProperty
    @JsonSerialize( contentAs = BaseIdentifiableObject.class )
    @JacksonXmlElementWrapper( localName = "dataElements", namespace = DxfNamespaces.DXF_2_0 )
    @JacksonXmlProperty( localName = "dataElement", namespace = DxfNamespaces.DXF_2_0 )
    public List<DataElement> getDataElements()
    {
        return dataElements;
    }

    public void setDataElements( List<DataElement> dataElements )
    {
        this.dataElements = dataElements;
    }

    @JsonProperty
    @JsonSerialize( contentAs = BaseIdentifiableObject.class )
    @JacksonXmlElementWrapper( localName = "indicators", namespace = DxfNamespaces.DXF_2_0 )
    @JacksonXmlProperty( localName = "indicator", namespace = DxfNamespaces.DXF_2_0 )
    public List<Indicator> getIndicators()
    {
        return indicators;
    }

    public void setIndicators( List<Indicator> indicators )
    {
        this.indicators = indicators;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public int getSortOrder()
    {
        return sortOrder;
    }

    public void setSortOrder( int sortOrder )
    {
        this.sortOrder = sortOrder;
    }

    @JsonProperty
    @JacksonXmlElementWrapper( localName = "greyedFields", namespace = DxfNamespaces.DXF_2_0 )
    @JacksonXmlProperty( localName = "greyedField", namespace = DxfNamespaces.DXF_2_0 )
    public Set<DataElementOperand> getGreyedFields()
    {
        return greyedFields;
    }

    public void setGreyedFields( Set<DataElementOperand> greyedFields )
    {
        this.greyedFields = greyedFields;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public boolean isShowRowTotals()
    {
        return showRowTotals;
    }

    public void setShowRowTotals( boolean showRowTotals )
    {
        this.showRowTotals = showRowTotals;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public boolean isShowColumnTotals()
    {
        return showColumnTotals;
    }

    public void setShowColumnTotals( boolean showColumnTotals )
    {
        this.showColumnTotals = showColumnTotals;
    }
}
