package com.mass3d.dataelement;

import static com.mass3d.common.DimensionalObjectUtils.COMPOSITE_DIM_OBJECT_PLAIN_SEP;
import static com.mass3d.expression.ExpressionService.SYMBOL_WILDCARD;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.Objects;
import com.mass3d.analytics.AggregationType;
import com.mass3d.category.CategoryOptionCombo;
import com.mass3d.common.BaseDimensionalItemObject;
import com.mass3d.common.BaseIdentifiableObject;
import com.mass3d.common.DimensionItemType;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.common.EmbeddedObject;
import com.mass3d.common.IdScheme;

/**
 * This object can act both as a hydrated persisted object and as a wrapper
 * object (but not both at the same time).
 * <p>
 * This object implements IdentifiableObject but does not have any UID. Instead
 * the UID is generated based on the data element and category option combo which
 * this object is based on.
 *
 */
@JacksonXmlRootElement( localName = "dataElementOperand", namespace = DxfNamespaces.DXF_2_0 )
public class DataElementOperand
    extends BaseDimensionalItemObject implements EmbeddedObject
{
    public static final String SEPARATOR = COMPOSITE_DIM_OBJECT_PLAIN_SEP;

    private static final String SPACE = " ";

    // -------------------------------------------------------------------------
    // Properties
    // -------------------------------------------------------------------------

    private DataElement dataElement;

    private CategoryOptionCombo categoryOptionCombo;

    private CategoryOptionCombo attributeOptionCombo;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public DataElementOperand()
    {
        setAutoFields();
    }

    public DataElementOperand( DataElement dataElement )
    {
        this.dataElement = dataElement;
    }

    public DataElementOperand( DataElement dataElement, CategoryOptionCombo categoryOptionCombo )
    {
        this.dataElement = dataElement;
        this.categoryOptionCombo = categoryOptionCombo;
    }

    public DataElementOperand( DataElement dataElement, CategoryOptionCombo categoryOptionCombo, CategoryOptionCombo attributeOptionCombo )
    {
        this.dataElement = dataElement;
        this.categoryOptionCombo = categoryOptionCombo;
        this.attributeOptionCombo = attributeOptionCombo;
    }

    // -------------------------------------------------------------------------
    // DimensionalItemObject
    // -------------------------------------------------------------------------

    @Override
    public String getDimensionItem()
    {
        return getDimensionItem( IdScheme.UID );
    }

    @Override
    public String getDimensionItem( IdScheme idScheme )
    {
        String item = null;

        if ( dataElement != null )
        {
            item = dataElement.getPropertyValue( idScheme );

            if ( categoryOptionCombo != null )
            {
                item += SEPARATOR + categoryOptionCombo.getPropertyValue( idScheme );
            }
            else if ( attributeOptionCombo != null )
            {
                item += SEPARATOR + SYMBOL_WILDCARD;
            }

            if ( attributeOptionCombo != null )
            {
                item += SEPARATOR + attributeOptionCombo.getPropertyValue( idScheme );
            }
        }

        return item;
    }

    @Override
    public DimensionItemType getDimensionItemType()
    {
        return DimensionItemType.DATA_ELEMENT_OPERAND;
    }

    @Override
    public AggregationType getAggregationType()
    {
        return dataElement.getAggregationType();
    }

    // -------------------------------------------------------------------------
    // IdentifiableObject
    // -------------------------------------------------------------------------

    @Override
    public String getUid()
    {
        String uid = null;

        if ( dataElement != null )
        {
            uid = dataElement.getUid();
        }

        if ( categoryOptionCombo != null && !categoryOptionCombo.isDefault() )
        {
            uid += SEPARATOR + categoryOptionCombo.getUid();
        }

        return uid;
    }

    @Override
    public String getName()
    {
        if ( name != null )
        {
            return name;
        }

        String name = null;

        if ( dataElement != null )
        {
            name = dataElement.getName();
        }

        if ( hasNonDefaultCategoryOptionCombo() )
        {
            name += SPACE + categoryOptionCombo.getName();
        }
        else if ( hasNonDefaultAttributeOptionCombo() )
        {
            name += SPACE + SYMBOL_WILDCARD;
        }

        if ( hasNonDefaultAttributeOptionCombo() )
        {
            name += SPACE + attributeOptionCombo.getName();
        }

        return name;
    }

    @Override
    public String getShortName()
    {
        String shortName = null;

        if ( dataElement != null )
        {
            shortName = dataElement.getShortName();
        }

        if ( hasNonDefaultCategoryOptionCombo() )
        {
            shortName += SPACE + categoryOptionCombo.getShortName();
        }
        else if ( hasNonDefaultAttributeOptionCombo() )
        {
            shortName += SPACE + SYMBOL_WILDCARD;
        }

        if ( hasNonDefaultAttributeOptionCombo() )
        {
            shortName += SPACE + attributeOptionCombo.getName();
        }

        return shortName;
    }

    @Override
    public String getDisplayShortName()
    {
        String displayShortName = null;

        if ( dataElement != null )
        {
            displayShortName = dataElement.getDisplayShortName();
        }

        if ( hasNonDefaultCategoryOptionCombo() )
        {
            displayShortName += SPACE + categoryOptionCombo.getDisplayShortName();
        }
        else if ( hasNonDefaultAttributeOptionCombo() )
        {
            displayShortName += SPACE + SYMBOL_WILDCARD;
        }

        if ( hasNonDefaultAttributeOptionCombo() )
        {
            displayShortName += SPACE + attributeOptionCombo.getDisplayShortName();
        }

        return displayShortName;
    }

    @Override
    public String getDisplayName()
    {
        String displayName = null;

        if ( dataElement != null )
        {
            displayName = dataElement.getDisplayName();
        }

        if ( hasNonDefaultCategoryOptionCombo() )
        {
            displayName += SPACE + categoryOptionCombo.getDisplayName();
        }
        else if ( hasNonDefaultAttributeOptionCombo() )
        {
            displayName += SPACE + SYMBOL_WILDCARD;
        }

        if ( hasNonDefaultAttributeOptionCombo() )
        {
            displayName += SPACE + attributeOptionCombo.getDisplayName();
        }

        return displayName;
    }

    /**
     * Creates a {@link DataElementOperand} instance from the given identifiers.
     *
     * @param dataElementUid         the data element identifier.
     * @param categoryOptionComboUid the category option combo identifier.
     * @return a data element operand instance.
     */
    public static DataElementOperand instance( String dataElementUid, String categoryOptionComboUid )
    {
        DataElement de = new DataElement();
        de.setUid( dataElementUid );

        CategoryOptionCombo coc = null;

        if ( categoryOptionComboUid != null )
        {
            coc = new CategoryOptionCombo();
            coc.setUid( categoryOptionComboUid );
        }

        return new DataElementOperand( de, coc );
    }

    /**
     * Indicates whether this operand specifies a data element only
     * with no option combinations.
     *
     * @return true if operand specifies a data element only.
     */
    public boolean isTotal()
    {
        return categoryOptionCombo == null && attributeOptionCombo == null;
    }

    /**
     * Indicates whether a category option combination exists which is different
     * from default.
     */
    public boolean hasNonDefaultCategoryOptionCombo()
    {
        return categoryOptionCombo != null && !categoryOptionCombo.isDefault();
    }

    /**
     * Indicates whether an attribute option combination exists which is different
     * from default.
     */
    public boolean hasNonDefaultAttributeOptionCombo()
    {
        return attributeOptionCombo != null && !attributeOptionCombo.isDefault();
    }

    // -------------------------------------------------------------------------
    // Getters & setters
    // -------------------------------------------------------------------------

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

    @JsonProperty
    @JsonSerialize( as = BaseIdentifiableObject.class )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public CategoryOptionCombo getCategoryOptionCombo()
    {
        return categoryOptionCombo;
    }

    public void setCategoryOptionCombo( CategoryOptionCombo categoryOptionCombo )
    {
        this.categoryOptionCombo = categoryOptionCombo;
    }

    @JsonProperty
    @JsonSerialize( as = BaseIdentifiableObject.class )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public CategoryOptionCombo getAttributeOptionCombo()
    {
        return attributeOptionCombo;
    }

    public void setAttributeOptionCombo( CategoryOptionCombo attributeOptionCombo )
    {
        this.attributeOptionCombo = attributeOptionCombo;
    }

    // -------------------------------------------------------------------------
    // hashCode, equals and toString
    // -------------------------------------------------------------------------

    @Override
    public boolean equals( Object o )
    {
        if ( this == o )
            return true;
        if ( o == null || getClass() != o.getClass() )
            return false;
        if ( !super.equals( o ) )
            return false;
        DataElementOperand that = (DataElementOperand) o;
        return Objects.equals( dataElement, that.dataElement ) &&
            Objects.equals( categoryOptionCombo, that.categoryOptionCombo ) &&
            Objects.equals( attributeOptionCombo, that.attributeOptionCombo );
    }

    @Override
    public int hashCode()
    {
        return Objects
            .hash( super.hashCode(), dataElement, categoryOptionCombo, attributeOptionCombo );
    }

    @Override
    public String toString()
    {
        return "{" +
            "\"class\":\"" + getClass() + "\", " +
            "\"id\":\"" + id + "\", " +
            "\"uid\":\"" + uid + "\", " +
            "\"dataElement\":" + dataElement + ", " +
            "\"categoryOptionCombo\":" + categoryOptionCombo +
            "\"attributeOptionCombo\":" + attributeOptionCombo +
            '}';
    }

    // -------------------------------------------------------------------------
    // Option combination type
    // -------------------------------------------------------------------------

    public enum TotalType
    {
        COC_ONLY( true, false, 1 ),
        AOC_ONLY( false, true, 1 ),
        COC_AND_AOC( true, true, 2 ),
        NONE( false, false, 0 );

        private boolean coc;
        private boolean aoc;
        private int propertyCount;

        TotalType()
        {
        }

        TotalType( boolean coc, boolean aoc, int propertyCount )
        {
            this.coc = coc;
            this.aoc = aoc;
            this.propertyCount = propertyCount;
        }

        public boolean isCategoryOptionCombo()
        {
            return coc;
        }

        public boolean isAttributeOptionCombo()
        {
            return aoc;
        }

        public int getPropertyCount()
        {
            return propertyCount;
        }
    }

    public TotalType getTotalType()
    {
        if ( categoryOptionCombo != null && attributeOptionCombo != null )
        {
            return TotalType.COC_AND_AOC;
        }
        else if ( categoryOptionCombo != null )
        {
            return TotalType.COC_ONLY;
        }
        else if ( attributeOptionCombo != null )
        {
            return TotalType.AOC_ONLY;
        }
        else
        {
            return TotalType.NONE;
        }
    }
}
