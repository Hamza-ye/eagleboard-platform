package com.mass3d.category;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import com.mass3d.common.BaseDimensionalItemObject;
import com.mass3d.common.BaseIdentifiableObject;
import com.mass3d.common.DimensionItemType;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.common.SystemDefaultMetadataObject;
import com.mass3d.dataelement.DataElement;
import com.mass3d.dataset.DataSet;
import com.mass3d.dataset.DataSetElement;
import com.mass3d.organisationunit.OrganisationUnit;
import com.mass3d.schema.annotation.PropertyRange;
import com.mass3d.translation.TranslationProperty;

@JacksonXmlRootElement( localName = "categoryOption", namespace = DxfNamespaces.DXF_2_0 )
public class CategoryOption
    extends BaseDimensionalItemObject implements SystemDefaultMetadataObject
{
    public static final String DEFAULT_NAME = "default";

    private Date startDate;

    private Date endDate;

    private Set<OrganisationUnit> organisationUnits = new HashSet<>();

    private Set<Category> categories = new HashSet<>();

    private Set<CategoryOptionCombo> categoryOptionCombos = new HashSet<>();

    private Set<CategoryOptionGroup> groups = new HashSet<>();

//    private ObjectStyle style;

    /**
     * The name to appear in forms.
     */
    private String formName;

    /**
     * The i18n variant of the display name. Should not be persisted.
     */
    protected transient String displayFormName;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public CategoryOption()
    {

    }

    public CategoryOption( String name )
    {
        this.name = name;
    }

    // -------------------------------------------------------------------------
    // Logic
    // -------------------------------------------------------------------------

    @JsonProperty( "isDefault" )
    @Override
    public boolean isDefault()
    {
        return DEFAULT_NAME.equals( name );
    }

    /**
     * Returns a set of category option group sets which are associated with the
     * category option groups of this category option.
     */
    public Set<CategoryOptionGroupSet> getGroupSets()
    {
        Set<CategoryOptionGroupSet> groupSets = new HashSet<>();

        if ( groups != null )
        {
            for ( CategoryOptionGroup group : groups )
            {
                groupSets.addAll( group.getGroupSets() );
            }
        }

        return groupSets;
    }

    public void addCategoryOptionCombo( CategoryOptionCombo dataElementCategoryOptionCombo )
    {
        categoryOptionCombos.add( dataElementCategoryOptionCombo );
        dataElementCategoryOptionCombo.getCategoryOptions().add( this );
    }

    public void removeCategoryOptionCombo( CategoryOptionCombo dataElementCategoryOptionCombo )
    {
        categoryOptionCombos.remove( dataElementCategoryOptionCombo );
        dataElementCategoryOptionCombo.getCategoryOptions().remove( this );
    }

    public void addOrganisationUnit( OrganisationUnit organisationUnit )
    {
        organisationUnits.add( organisationUnit );
        organisationUnit.getCategoryOptions().add( this );
    }

    public void removeOrganisationUnit( OrganisationUnit organisationUnit )
    {
        organisationUnits.remove( organisationUnit );
        organisationUnit.getCategoryOptions().remove( this );
    }

    public boolean includes( OrganisationUnit ou )
    {
        return organisationUnits == null || organisationUnits.isEmpty() || ou.isDescendant( organisationUnits );
    }

    public boolean includesAny( Set<OrganisationUnit> orgUnits )
    {
        for ( OrganisationUnit ou : orgUnits )
        {
            if ( includes( ou ) )
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Gets an adjusted end date, adjusted if this data set has
     * open periods after the end date.
     *
     * @param dataSet the data set to adjust for
     * @return the adjusted end date
     */
    public Date getAdjustedEndDate( DataSet dataSet )
    {
        if ( endDate == null || dataSet.getOpenPeriodsAfterCoEndDate() == 0 )
        {
            return endDate;
        }

        return dataSet.getPeriodType().getRewindedDate( endDate, -dataSet.getOpenPeriodsAfterCoEndDate() );
    }

    /**
     * Gets an adjusted end date, adjusted if a data element belongs
     * to any data sets that have open periods after the end date.
     * If so, it chooses the latest end date.
     *
     * @param dataElement the data element to adjust for
     * @return the adjusted end date
     */
    public Date getAdjustedEndDate( DataElement dataElement )
    {
        if ( endDate == null )
        {
            return null;
        }

        Date latestAdjustedDate = endDate;

        for ( DataSetElement element : dataElement.getDataSetElements() )
        {
            Date adjustedDate = getAdjustedEndDate( element.getDataSet() );

            if ( adjustedDate.after( latestAdjustedDate ) )
            {
                latestAdjustedDate = adjustedDate;
            }
        }

        return latestAdjustedDate;
    }

    /**
     * Gets an adjusted end date for a data set or, if that is not present,
     * a data element.
     *
     * @param dataSet the data set to adjust for
     * @param dataElement the data element to adjust for
     * @return the adjusted end date
     */
    public Date getAdjustedEndDate( DataSet dataSet, DataElement dataElement )
    {
        return dataSet != null
            ? getAdjustedEndDate( dataSet )
            : getAdjustedEndDate( dataElement );
    }

    // -------------------------------------------------------------------------
    // DimensionalItemObject
    // -------------------------------------------------------------------------

    @Override
    public DimensionItemType getDimensionItemType()
    {
        return DimensionItemType.CATEGORY_OPTION;
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

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
    @JsonSerialize( contentAs = BaseIdentifiableObject.class )
    @JacksonXmlElementWrapper( localName = "organisationUnits", namespace = DxfNamespaces.DXF_2_0 )
    @JacksonXmlProperty( localName = "organisationUnit", namespace = DxfNamespaces.DXF_2_0 )
    public Set<OrganisationUnit> getOrganisationUnits()
    {
        return organisationUnits;
    }

    public void setOrganisationUnits( Set<OrganisationUnit> organisationUnits )
    {
        this.organisationUnits = organisationUnits;
    }

    @JsonProperty
    @JsonSerialize( contentAs = BaseIdentifiableObject.class )
    @JacksonXmlElementWrapper( localName = "categories", namespace = DxfNamespaces.DXF_2_0 )
    @JacksonXmlProperty( localName = "category", namespace = DxfNamespaces.DXF_2_0 )
    public Set<Category> getCategories()
    {
        return categories;
    }

    public void setCategories( Set<Category> categories )
    {
        this.categories = categories;
    }

    @JsonProperty
    @JsonSerialize( contentAs = BaseIdentifiableObject.class )
    @JacksonXmlElementWrapper( localName = "categoryOptionCombos", namespace = DxfNamespaces.DXF_2_0 )
    @JacksonXmlProperty( localName = "categoryOptionCombo", namespace = DxfNamespaces.DXF_2_0 )
    public Set<CategoryOptionCombo> getCategoryOptionCombos()
    {
        return categoryOptionCombos;
    }

    public void setCategoryOptionCombos( Set<CategoryOptionCombo> categoryOptionCombos )
    {
        this.categoryOptionCombos = categoryOptionCombos;
    }

    @JsonProperty
    @JsonSerialize( contentAs = BaseIdentifiableObject.class )
    @JacksonXmlElementWrapper( localName = "categoryOptionGroups", namespace = DxfNamespaces.DXF_2_0 )
    @JacksonXmlProperty( localName = "categoryOptionGroup", namespace = DxfNamespaces.DXF_2_0 )
    public Set<CategoryOptionGroup> getGroups()
    {
        return groups;
    }

    public void setGroups( Set<CategoryOptionGroup> groups )
    {
        this.groups = groups;
    }

//    @JsonProperty
//    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
//    public ObjectStyle getStyle()
//    {
//        return style;
//    }
//
//    public void setStyle( ObjectStyle style )
//    {
//        this.style = style;
//    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    @PropertyRange( min = 2 )
    public String getFormName()
    {
        return formName;
    }

    public void setFormName( String formName )
    {
        this.formName = formName;
    }

    /**
     * Returns the form name, or the name if it does not exist.
     */
    public String getFormNameFallback()
    {
        return formName != null && !formName.isEmpty() ? getFormName() : getDisplayName();
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getDisplayFormName()
    {
        displayFormName = null; //getTranslation( TranslationProperty.FORM_NAME, displayFormName );
        return displayFormName != null ? displayFormName : getFormNameFallback();
    }

    public void setDisplayFormName( String displayFormName )
    {
        this.displayFormName = displayFormName;
    }
}
