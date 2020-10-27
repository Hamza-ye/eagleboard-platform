package com.mass3d.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableSet;
import java.io.Serializable;
import com.mass3d.option.OptionSet;

public class GridHeader
    implements Serializable
{
    private static final ImmutableSet<String> NUMERIC_TYPES =
        ImmutableSet
            .of( Float.class.getName(), Double.class.getName(), Long.class.getName(), Integer.class.getName() );

    /**
     * Format header key name.
     */
    private String name;

    /**
     * Readable pretty header title.
     */
    private String column;

    private ValueType valueType;

    private String type;

    private boolean hidden;

    private boolean meta;

    private OptionSet optionSet;

//    private LegendSet legendSet;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public GridHeader()
    {
    }

    /**
     * @param name formal header name.
     */
    public GridHeader( String name )
    {
        this.name = name;
        this.type = String.class.getName();
        this.hidden = false;
        this.meta = false;
    }

    /**
     * @param name formal header name.
     * @param column readable header title.
     */
    public GridHeader( String name, String column )
    {
        this( name );
        this.column = column;
    }

    /**
     * Sets the column property to the name value. Sets the type property to
     * String.
     *
     * @param name formal header name.
     * @param hidden indicates whether header is hidden.
     * @param meta indicates whether header is meta data.
     */
    public GridHeader( String name, boolean hidden, boolean meta )
    {
        this( name );
        this.column = name;
        this.hidden = hidden;
        this.meta = meta;
    }

    /**
     * @param name formal header name.
     * @param column readable header title.
     * @param valueType header value type.
     * @param type header type (deprecated).
     * @param hidden indicates whether header is hidden.
     * @param meta indicates whether header is meta data.
     */
    public GridHeader( String name, String column, ValueType valueType, String type, boolean hidden, boolean meta )
    {
        this( name, column );
        this.valueType = valueType;
        this.type = type;
        this.hidden = hidden;
        this.meta = meta;
    }

    /**
     * @param name formal header name.
     * @param column readable header title.
     * @param valueType header value type.
     * @param type header type (deprecated).
     * @param hidden indicates whether header is hidden.
     * @param meta indicates whether header is meta data.
     * @param optionSet option set.
     */
    public GridHeader( String name, String column, ValueType valueType, String type, boolean hidden, boolean meta, OptionSet optionSet)
    {
        this( name, column, valueType, type, hidden, meta );
        this.optionSet = optionSet;
    }

    // -------------------------------------------------------------------------
    // Logic
    // -------------------------------------------------------------------------

    public boolean isNumeric()
    {
        return type != null && NUMERIC_TYPES.contains( type );
    }

//    public boolean hasLegendSet()
//    {
//        return legendSet != null;
//    }

    public boolean hasOptionSet()
    {
        return optionSet != null;
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    @JsonProperty
    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    @JsonProperty
    public String getColumn()
    {
        return column;
    }

    @JsonProperty
    public ValueType getValueType()
    {
        return valueType;
    }

    @JsonProperty
    public String getType()
    {
        return type;
    }

    @JsonProperty
    public boolean isHidden()
    {
        return hidden;
    }

    @JsonProperty
    public boolean isMeta()
    {
        return meta;
    }

    @JsonProperty
    public String getOptionSet()
    {
        return optionSet != null ? optionSet.getUid() : null;
    }

    @JsonIgnore
    public OptionSet getOptionSetObject()
    {
        return optionSet;
    }

//    @JsonProperty
//    public String getLegendSet()
//    {
//        return legendSet != null ? legendSet.getUid() : null;
//    }
//
//    @JsonIgnore
//    public LegendSet getLegendSetObject()
//    {
//        return legendSet;
//    }

    // -------------------------------------------------------------------------
    // hashCode, equals, toString
    // -------------------------------------------------------------------------

    @Override
    public int hashCode()
    {
        return name.hashCode();
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

        if ( getClass() != object.getClass() )
        {
            return false;
        }

        final GridHeader other = (GridHeader) object;

        return name.equals( other.name );
    }

    @Override
    public String toString()
    {
        return "[Name: " + name + ", column: " + column + ", value type: " + valueType + ", type: " + type + "]";
    }
}
