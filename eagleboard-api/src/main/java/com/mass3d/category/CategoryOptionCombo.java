package com.mass3d.category;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import com.mass3d.common.BaseDimensionalItemObject;
import com.mass3d.common.BaseIdentifiableObject;
import com.mass3d.common.DateRange;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.common.SystemDefaultMetadataObject;
import com.mass3d.dataelement.DataElement;
import com.mass3d.dataset.DataSet;
import com.mass3d.organisationunit.OrganisationUnit;

@JacksonXmlRootElement( localName = "categoryOptionCombo", namespace = DxfNamespaces.DXF_2_0 )
public class CategoryOptionCombo
    extends BaseDimensionalItemObject implements SystemDefaultMetadataObject
{
    public static final String DEFAULT_NAME = "default";

    public static final String DEFAULT_TOSTRING = "(default)";

    /**
     * The category combo.
     */
    private CategoryCombo categoryCombo;

    /**
     * The category options.
     */
    private Set<CategoryOption> categoryOptions = new HashSet<>();

    /**
     * Indicates whether to ignore data approval.
     */
    private boolean ignoreApproval;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public CategoryOptionCombo()
    {
    }

    // -------------------------------------------------------------------------
    // hashCode, equals and toString
    // -------------------------------------------------------------------------

    @Override
    public int hashCode()
    {
        final int prime = 31;

        int result = 1;

        result = prime * result + ((categoryCombo == null) ? 0 : categoryCombo.hashCode());
        result = prime * result + ((categoryOptions == null) ? 0 : categoryOptions.hashCode());

        return result;
    }

    @Override
    public boolean equals( Object object )
    {
        if ( this == object )
        {
            return true;
        }

        if ( object == null )
        {
            return false;
        }

        if ( !(object instanceof CategoryOptionCombo) )
        {
            return false;
        }

        final CategoryOptionCombo other = (CategoryOptionCombo) object;

        if ( categoryCombo == null )
        {
            if ( other.categoryCombo != null )
            {
                return false;
            }
        }
        else if ( !categoryCombo.equals( other.categoryCombo ) )
        {
            return false;
        }

        if ( categoryOptions == null )
        {
            if ( other.categoryOptions != null )
            {
                return false;
            }
        }
        else if ( !categoryOptions.equals( other.categoryOptions ) )
        {
            return false;
        }

        return true;
    }

    @Override
    public String toString()
    {
        return "{" +
            "\"class\":\"" + getClass() + "\", " +
            "\"id\":\"" + getId() + "\", " +
            "\"uid\":\"" + getUid() + "\", " +
            "\"code\":\"" + getCode() + "\", " +
            "\"categoryCombo\":" + categoryCombo + ", " +
            "\"categoryOptions\":" + categoryOptions +
            "}";
    }

    // -------------------------------------------------------------------------
    // hashCode and equals based on identifiable object
    // -------------------------------------------------------------------------

    public int hashCodeIdentifiableObject()
    {
        return super.hashCode();
    }

    public boolean equalsIdentifiableObject( Object object )
    {
        return super.equals( object );
    }

    // -------------------------------------------------------------------------
    // Logic
    // -------------------------------------------------------------------------

    public void addCategoryOption( CategoryOption dataElementCategoryOption )
    {
        categoryOptions.add( dataElementCategoryOption );
        dataElementCategoryOption.getCategoryOptionCombos().add( this );
    }

    public void removeCategoryOption( CategoryOption dataElementCategoryOption )
    {
        categoryOptions.remove( dataElementCategoryOption );
        dataElementCategoryOption.getCategoryOptionCombos().remove( this );
    }

    public void removeAllCategoryOptions()
    {
        categoryOptions.clear();
    }

    @Override
    public boolean isDefault()
    {
        return categoryCombo != null && DEFAULT_NAME.equals( categoryCombo.getName() );
    }

    /**
     * Gets a range of valid dates for this (attribute) cateogry option combo
     * for a data set.
     * <p>
     * The earliest valid date is the latest start date (if any) from all the
     * category options associated with this option combo.
     * <p>
     * The latest valid date is the earliest end date (if any) from all the
     * category options associated with this option combo.
     *
     * @param dataSet the data set for which to check dates.
     * @return valid date range for this (attribute) category option combo.
     */
    public DateRange getDateRange( DataSet dataSet )
    {
        return getDateRange( dataSet, null );
    }

    /**
     * Gets a range of valid dates for this (attribute) cateogry option combo
     * for a data element (for all data sets to which the data element belongs).
     * <p>
     * The earliest valid date is the latest start date (if any) from all the
     * category options associated with this option combo.
     * <p>
     * The latest valid date is the earliest end date (if any) from all the
     * category options associated with this option combo.
     *
     * @param dataElement the data element for which to check dates.
     * @return valid date range for this (attribute) category option combo.
     */
    public DateRange getDateRange( DataElement dataElement )
    {
        return getDateRange( null, dataElement );
    }

    /**
     * Gets a set of valid organisation units (subtrees) for this (attribute)
     * category option combo, if any.
     * <p>
     * The set of valid organisation units (if any) is the intersection of the
     * sets of valid organisation untis for all the category options associated
     * with this option combo.
     * <p>
     * Note: returns null if there are no organisation unit restrictions (no
     * associated option combos have any organisation unit restrictions), but
     * returns an empty set if associated option combos have organisation unit
     * restrictions and their intersection is empty.
     *
     * @return valid organisation units for this (attribute) category option
     * combo.
     */
    public Set<OrganisationUnit> getOrganisationUnits()
    {
        Set<OrganisationUnit> orgUnits = null;

        for ( CategoryOption option : getCategoryOptions() )
        {
            if ( !CollectionUtils.isEmpty( option.getOrganisationUnits() ) )
            {
                if ( orgUnits == null )
                {
                    orgUnits = option.getOrganisationUnits();
                }
                else
                {
                    orgUnits = new HashSet<>( orgUnits );
                    orgUnits.retainAll( option.getOrganisationUnits() );
                }
            }
        }

        return orgUnits;
    }

    /**
     * Gets the latest category option start date for this category
     * option combo. The combo is only valid between the latest start
     * date of any options and the earliest end date of any options.
     *
     * @return the latest option start date for this combo.
     */
    public Date getLatestStartDate()
    {
        Date latestStartDate = null;

        for ( CategoryOption co : getCategoryOptions() )
        {
            if ( co.getStartDate() != null )
            {
                latestStartDate = (latestStartDate == null || latestStartDate.before( co.getStartDate() ) ?
                    co.getStartDate() : latestStartDate);
            }
        }

        return latestStartDate;
    }

    /**
     * Gets the earliest category option end date for this category
     * option combo. The combo is only valid between the latest start
     * date of any options and the earliest end date of any options.
     * <p>
     * Note that this end date does not take into account any possible
     * extensions to the category end dates for aggregate data entry
     * in data sets with openPeriodsAfterCoEndDate.
     *
     * @return the latest option start date for this combo.
     */
    public Date getEarliestEndDate()
    {
        Date earliestEndDate = null;

        for ( CategoryOption co : getCategoryOptions() )
        {
            if ( co.getEndDate() != null )
            {
                earliestEndDate = ( earliestEndDate == null || earliestEndDate.after( co.getEndDate() ) ?
                    co.getStartDate() : earliestEndDate);
            }
        }

        return earliestEndDate;
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    /**
     * Gets a range of valid dates for this (attribute) cateogry option combo
     * for a data set or, if that is not present, a data element.
     *
     * @param dataSet the data set to get the range for, or
     * @param dataElement the data element to get the range for
     * @return valid date range for this (attribute) category option combo.
     */
    private DateRange getDateRange( DataSet dataSet, DataElement dataElement )
    {
        Date latestStartDate = null;
        Date earliestEndDate = null;

        for ( CategoryOption co : getCategoryOptions() )
        {
            if ( co.getStartDate() != null && (latestStartDate == null || co.getStartDate().after( latestStartDate ) ) )
            {
                latestStartDate = co.getStartDate();
            }

            Date coEndDate = dataSet != null
                ? co.getAdjustedEndDate( dataSet )
                : co.getAdjustedEndDate( dataElement );

            if ( coEndDate != null && (earliestEndDate == null || coEndDate.before( earliestEndDate ) ) )
            {
                earliestEndDate = coEndDate;
            }
        }

        return new DateRange( latestStartDate, earliestEndDate );
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    @Override
    public String getName()
    {
        if ( name != null )
        {
            return name;
        }

        StringBuilder builder = new StringBuilder();

        if ( categoryCombo == null || categoryCombo.getCategories().isEmpty() )
        {
            return uid;
        }

        List<Category> categories = categoryCombo.getCategories();

        for ( Category category : categories )
        {
            List<CategoryOption> options = category.getCategoryOptions();

            optionLoop:
            for ( CategoryOption option : categoryOptions )
            {
                if ( options.contains( option ) )
                {
                    builder.append( option.getDisplayName() ).append( ", " );

                    continue optionLoop;
                }
            }
        }

        builder.delete( Math.max( builder.length() - 2, 0 ), builder.length() );

        return StringUtils.substring( builder.toString(), 0, 255 );
    }

    @Override
    public void setName( String name )
    {
        this.name = name;
    }

    @Override
    @JsonIgnore
    public String getShortName()
    {
        return getName();
    }

    @Override
    public void setShortName( String shortName )
    {
        // Not supported
    }

    @JsonProperty
    @JsonSerialize( as = BaseIdentifiableObject.class )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public CategoryCombo getCategoryCombo()
    {
        return categoryCombo;
    }

    public void setCategoryCombo( CategoryCombo categoryCombo )
    {
        this.categoryCombo = categoryCombo;
    }

    @JsonProperty
    @JsonSerialize( contentAs = BaseIdentifiableObject.class )
    @JacksonXmlElementWrapper( localName = "categoryOptions", namespace = DxfNamespaces.DXF_2_0 )
    @JacksonXmlProperty( localName = "categoryOption", namespace = DxfNamespaces.DXF_2_0 )
    public Set<CategoryOption> getCategoryOptions()
    {
        return categoryOptions;
    }

    public void setCategoryOptions( Set<CategoryOption> categoryOptions )
    {
        this.categoryOptions = categoryOptions;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public boolean isIgnoreApproval()
    {
        return ignoreApproval;
    }

    public void setIgnoreApproval( boolean ignoreApproval )
    {
        this.ignoreApproval = ignoreApproval;
    }
}
