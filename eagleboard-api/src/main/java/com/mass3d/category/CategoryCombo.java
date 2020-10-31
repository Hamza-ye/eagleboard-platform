package com.mass3d.category;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import com.mass3d.common.BaseIdentifiableObject;
import com.mass3d.common.CombinationGenerator;
import com.mass3d.common.DataDimensionType;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.common.SystemDefaultMetadataObject;

@JacksonXmlRootElement( localName = "categoryCombo", namespace = DxfNamespaces.DXF_2_0 )
public class CategoryCombo
    extends BaseIdentifiableObject implements SystemDefaultMetadataObject
{
    public static final String DEFAULT_CATEGORY_COMBO_NAME = "default";

    /**
     * A set with categories.
     */
    private List<Category> categories = new ArrayList<>();

    /**
     * A set of category option combinations. Use getSortedOptionCombos() to get a
     * sorted list of category option combinations.
     */
    private Set<CategoryOptionCombo> optionCombos = new HashSet<>();

    /**
     * Type of data dimension. Category combinations of type DISAGGREGATION can
     * be linked to data elements, whereas type ATTRIBUTE can be linked to data
     * sets.
     */
    private DataDimensionType dataDimensionType;

    /**
     * Indicates whether to skip total values for the categories in reports.
     */
    private boolean skipTotal;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public CategoryCombo()
    {
    }

    public CategoryCombo( String name, DataDimensionType dataDimensionType )
    {
        this.name = name;
        this.dataDimensionType = dataDimensionType;
    }

    public CategoryCombo( String name, DataDimensionType dataDimensionType, List<Category> categories )
    {
        this( name, dataDimensionType );
        this.categories = categories;
    }

    // -------------------------------------------------------------------------
    // Logic
    // -------------------------------------------------------------------------

    @JsonProperty( "isDefault" )
    @Override
    public boolean isDefault()
    {
        return DEFAULT_CATEGORY_COMBO_NAME.equals( name );
    }

    /**
     * Indicates whether this category combo has at least one category, has at
     * least one category option combo and that all categories have at least one
     * category option.
     */
    public boolean isValid()
    {
        if ( categories == null || categories.isEmpty() )
        {
            return false;
        }

        for ( Category category : categories )
        {
            if ( category == null || category.getCategoryOptions() == null || category.getCategoryOptions().isEmpty() )
            {
                return false;
            }
        }

        return true;
    }

    public List<CategoryOption> getCategoryOptions()
    {
        final List<CategoryOption> categoryOptions = new ArrayList<>();

        for ( Category category : categories )
        {
            categoryOptions.addAll( category.getCategoryOptions() );
        }

        return categoryOptions;
    }

    public boolean doTotal()
    {
        return optionCombos != null && optionCombos.size() > 1 && !skipTotal;
    }

    public boolean doSubTotals()
    {
        return categories != null && categories.size() > 1;
    }

    public List<List<CategoryOption>> getCategoryOptionsAsLists()
    {
        return categories.stream()
            .filter( ca -> !ca.getCategoryOptions().isEmpty() )
            .map( ca -> ca.getCategoryOptions() )
            .collect( Collectors.toList() );
    }

    public List<CategoryOptionCombo> generateOptionCombosList()
    {
        List<CategoryOptionCombo> list = new ArrayList<>();

        CombinationGenerator<CategoryOption> generator =
            CombinationGenerator.newInstance( getCategoryOptionsAsLists() );

        while ( generator.hasNext() )
        {
            CategoryOptionCombo optionCombo = new CategoryOptionCombo();
            optionCombo.setCategoryOptions( new HashSet<>( generator.getNext() ) );
            optionCombo.setCategoryCombo( this );
            list.add( optionCombo );
        }

        return list;
    }

    public List<CategoryOptionCombo> getSortedOptionCombos()
    {
        List<CategoryOptionCombo> list = new ArrayList<>();

        CombinationGenerator<CategoryOption> generator =
            CombinationGenerator.newInstance( getCategoryOptionsAsLists() );

        while ( generator.hasNext() )
        {
            List<CategoryOption> categoryOptions = generator.getNext();

            Set<CategoryOption> categoryOptionSet = new HashSet<>( categoryOptions );

            for ( CategoryOptionCombo optionCombo : optionCombos )
            {
                Set<CategoryOption> persistedCategoryOptions = new HashSet<>( optionCombo.getCategoryOptions() );

                if ( categoryOptionSet.equals( persistedCategoryOptions ) )
                {
                    list.add( optionCombo );
                    continue;
                }
            }
        }

        return list;
    }

    public void generateOptionCombos()
    {
        this.optionCombos = new HashSet<>( generateOptionCombosList() );

        for ( CategoryOptionCombo optionCombo : optionCombos )
        {
            for ( CategoryOption categoryOption : optionCombo.getCategoryOptions() )
            {
                categoryOption.addCategoryOptionCombo( optionCombo );
            }
        }
    }

    public boolean hasOptionCombos()
    {
        return optionCombos != null && !optionCombos.isEmpty();
    }

    // -------------------------------------------------------------------------
    // Logic
    // -------------------------------------------------------------------------

    public void addCategory( Category category )
    {
        categories.add( category );
        category.getCategoryCombos().add( this );
    }

    public void removeCategory( Category category )
    {
        categories.remove( category );
        category.getCategoryCombos().remove( this );
    }

    public void removeAllCategories()
    {
        for ( Category category : categories )
        {
            category.getCategoryCombos().remove( this );
        }

        categories.clear();
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    @JsonProperty
    @JsonSerialize( contentAs = BaseIdentifiableObject.class )
    @JacksonXmlElementWrapper( localName = "categories", namespace = DxfNamespaces.DXF_2_0 )
    @JacksonXmlProperty( localName = "category", namespace = DxfNamespaces.DXF_2_0 )
    public List<Category> getCategories()
    {
        return categories;
    }

    public void setCategories( List<Category> categories )
    {
        this.categories = categories;
    }

    @JsonProperty( "categoryOptionCombos" )
    @JsonSerialize( contentAs = BaseIdentifiableObject.class )
    @JacksonXmlElementWrapper( localName = "categoryOptionCombos", namespace = DxfNamespaces.DXF_2_0 )
    @JacksonXmlProperty( localName = "categoryOptionCombo", namespace = DxfNamespaces.DXF_2_0 )
    public Set<CategoryOptionCombo> getOptionCombos()
    {
        return optionCombos;
    }

    public void setOptionCombos( Set<CategoryOptionCombo> optionCombos )
    {
        this.optionCombos = optionCombos;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public DataDimensionType getDataDimensionType()
    {
        return dataDimensionType;
    }

    public void setDataDimensionType( DataDimensionType dataDimensionType )
    {
        this.dataDimensionType = dataDimensionType;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public boolean isSkipTotal()
    {
        return skipTotal;
    }

    public void setSkipTotal( boolean skipTotal )
    {
        this.skipTotal = skipTotal;
    }
}
