package com.mass3d.category;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import com.mass3d.common.BaseDimensionalItemObject;
import com.mass3d.common.BaseDimensionalObject;
import com.mass3d.common.BaseIdentifiableObject;
import com.mass3d.common.DataDimensionType;
import com.mass3d.common.DimensionType;
import com.mass3d.common.DimensionalItemObject;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.common.SystemDefaultMetadataObject;

/**
 * A Category is a dimension of a data element. DataElements can have sets of
 * dimensions (known as CategoryCombos). An Example of a Category might be
 * "Sex". The Category could have two (or more) CategoryOptions such as "Male"
 * and "Female".
 *
 */
@JacksonXmlRootElement( localName = "category", namespace = DxfNamespaces.DXF_2_0 )
public class Category
    extends BaseDimensionalObject implements SystemDefaultMetadataObject
{
    public static final String DEFAULT_NAME = "default";

    private List<CategoryOption> categoryOptions = new ArrayList<>();

    private List<CategoryCombo> categoryCombos = new ArrayList<>();

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public Category()
    {
    }

    public Category( String name, DataDimensionType dataDimensionType )
    {
        this.dataDimensionType = dataDimensionType;
        this.name = name;
    }

    public Category( String name, DataDimensionType dataDimensionType, List<CategoryOption> categoryOptions )
    {
        this( name, dataDimensionType );
        this.categoryOptions = categoryOptions;
    }

    // -------------------------------------------------------------------------
    // Logic
    // -------------------------------------------------------------------------

    public void addCategoryOption( CategoryOption categoryOption )
    {
        categoryOptions.add( categoryOption );
        categoryOption.getCategories().add( this );
    }

    public void removeCategoryOption( CategoryOption categoryOption )
    {
        categoryOptions.remove( categoryOption );
        categoryOption.getCategories().remove( this );
    }

    public void removeAllCategoryOptions()
    {
        for ( CategoryOption categoryOption : categoryOptions )
        {
            categoryOption.getCategories().remove( this );
        }

        categoryOptions.clear();
    }

    public void addCategoryCombo( CategoryCombo categoryCombo )
    {
        categoryCombos.add( categoryCombo );
        categoryCombo.getCategories().add( this );
    }

    public void removeCategoryCombo( CategoryCombo categoryCombo )
    {
        categoryCombos.remove( categoryCombo );
        categoryCombo.getCategories().remove( this );
    }

    public void removeAllCategoryCombos()
    {
        for ( CategoryCombo categoryCombo : categoryCombos )
        {
            categoryCombo.getCategories().remove( this );
        }

        categoryCombos.clear();
    }

    public CategoryOption getCategoryOption( CategoryOptionCombo categoryOptionCombo )
    {
        for ( CategoryOption categoryOption : categoryOptions )
        {
            if ( categoryOption.getCategoryOptionCombos().contains( categoryOptionCombo ) )
            {
                return categoryOption;
            }
        }

        return null;
    }

    @Override
    public boolean isDefault()
    {
        return DEFAULT_NAME.equals( name );
    }

    // -------------------------------------------------------------------------
    // Dimensional object
    // -------------------------------------------------------------------------

    @Override
    @JsonProperty
    @JsonSerialize( contentAs = BaseDimensionalItemObject.class )
    @JacksonXmlElementWrapper( localName = "items", namespace = DxfNamespaces.DXF_2_0 )
    @JacksonXmlProperty( localName = "item", namespace = DxfNamespaces.DXF_2_0 )
    public List<DimensionalItemObject> getItems()
    {
        return Lists.newArrayList( categoryOptions );
    }

    @Override
    public DimensionType getDimensionType()
    {
        return DimensionType.CATEGORY;
    }

    // ------------------------------------------------------------------------
    // Getters and setters
    // ------------------------------------------------------------------------

    @Override
    public String getShortName()
    {
        if ( getName() == null || getName().length() <= 50 )
        {
            return getName();
        }
        else
        {
            return getName().substring( 0, 49 );
        }
    }

    @JsonProperty
    @JsonSerialize( contentAs = BaseIdentifiableObject.class )
    @JacksonXmlElementWrapper( localName = "categoryOptions", namespace = DxfNamespaces.DXF_2_0 )
    @JacksonXmlProperty( localName = "categoryOption", namespace = DxfNamespaces.DXF_2_0 )
    public List<CategoryOption> getCategoryOptions()
    {
        return categoryOptions;
    }

    public void setCategoryOptions( List<CategoryOption> categoryOptions )
    {
        this.categoryOptions = categoryOptions;
    }

    @JsonProperty
    @JsonSerialize( contentAs = BaseIdentifiableObject.class )
    @JacksonXmlElementWrapper( localName = "categoryCombos", namespace = DxfNamespaces.DXF_2_0 )
    @JacksonXmlProperty( localName = "categoryCombo", namespace = DxfNamespaces.DXF_2_0 )
    public List<CategoryCombo> getCategoryCombos()
    {
        return categoryCombos;
    }

    public void setCategoryCombos( List<CategoryCombo> categoryCombos )
    {
        this.categoryCombos = categoryCombos;
    }
}
