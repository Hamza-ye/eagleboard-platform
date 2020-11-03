package com.mass3d.common;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.google.common.collect.ImmutableSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import com.mass3d.organisationunit.OrganisationUnit;
import com.mass3d.trackedentity.TrackedEntityInstance;
import org.opengis.geometry.primitive.Point;

@JacksonXmlRootElement( localName = "valueType", namespace = DxfNamespaces.DXF_2_0 )
public enum ValueType
{
    TEXT( String.class, true ),
    LONG_TEXT( String.class, true ),
    LETTER( String.class, true ),
    PHONE_NUMBER( String.class, false ),
    EMAIL( String.class, false ),
    BOOLEAN( Boolean.class, true ),
    TRUE_ONLY( Boolean.class, true ),
    DATE( LocalDate.class, false ),
    DATETIME( LocalDateTime.class, false ),
    TIME( String.class, false ),
    NUMBER( Double.class, true ),
    UNIT_INTERVAL( Double.class, true ),
    PERCENTAGE( Double.class, true ),
    INTEGER( Integer.class, true ),
    INTEGER_POSITIVE( Integer.class, true ),
    INTEGER_NEGATIVE( Integer.class, true ),
    INTEGER_ZERO_OR_POSITIVE( Integer.class, true ),
    TRACKER_ASSOCIATE( TrackedEntityInstance.class, false ),
    USERNAME( String.class, false ),
    COORDINATE( Point.class, true ),
    ORGANISATION_UNIT( OrganisationUnit.class, false ),
    AGE( Date.class, false ),
    URL( String.class, false ),
    FILE_RESOURCE( String.class, false ),
    IMAGE( String.class, false);

    public static final Set<ValueType> INTEGER_TYPES = ImmutableSet.of(
        INTEGER, INTEGER_POSITIVE, INTEGER_NEGATIVE, INTEGER_ZERO_OR_POSITIVE );

    public static final Set<ValueType> DECIMAL_TYPES = ImmutableSet.of(
        NUMBER, UNIT_INTERVAL, PERCENTAGE );

    public static final Set<ValueType> BOOLEAN_TYPES = ImmutableSet.of(
        BOOLEAN, TRUE_ONLY );

    public static final Set<ValueType> TEXT_TYPES = ImmutableSet.of(
        TEXT, LONG_TEXT, LETTER, TIME, USERNAME, EMAIL, PHONE_NUMBER, URL );

    public static final Set<ValueType> DATE_TYPES = ImmutableSet.of(
        DATE, DATETIME, AGE );

    public static final Set<ValueType> FILE_TYPES = ImmutableSet.of(
        FILE_RESOURCE, IMAGE );

    public static final Set<ValueType> GEO_TYPES = ImmutableSet.of(
        COORDINATE );

    public static final Set<ValueType> NUMERIC_TYPES = ImmutableSet.<ValueType>builder().addAll(
        INTEGER_TYPES ).addAll( DECIMAL_TYPES ).build();

    @Deprecated
    private final Class<?> javaClass;

    private boolean aggregateable;

    ValueType()
    {
        this.javaClass = null;
    }

    ValueType( Class<?> javaClass, boolean aggregateable )
    {
        this.javaClass = javaClass;
        this.aggregateable = aggregateable;
    }

    public Class<?> getJavaClass()
    {
        return javaClass;
    }

    public boolean isInteger()
    {
        return INTEGER_TYPES.contains( this );
    }

    public boolean isDecimal()
    {
        return DECIMAL_TYPES.contains( this );
    }

    public boolean isBoolean()
    {
        return BOOLEAN_TYPES.contains( this );
    }

    public boolean isText()
    {
        return TEXT_TYPES.contains( this );
    }

    public boolean isDate()
    {
        return DATE_TYPES.contains( this );
    }

    public boolean isFile()
    {
        return FILE_TYPES.contains( this );
    }

    public boolean isGeo()
    {
        return GEO_TYPES.contains( this );
    }

    public boolean isOrganisationUnit()
    {
        return ORGANISATION_UNIT == this;
    }

    /**
     * Includes integer and decimal types.
     */
    public boolean isNumeric()
    {
        return NUMERIC_TYPES.contains( this );
    }

    public boolean isAggregateable()
    {
        return aggregateable;
    }

    /**
     * Returns a simplified value type. As an example, if the value type is
     * any numeric type such as integer, percentage, then {@link ValueType#NUMBER}
     * is returned. Can return any of:
     *
     * <ul>
     * <li>{@link ValueType#NUMBER} for any numeric types.</li>
     * <li>{@link ValueType#BOOLEAN} for any boolean types.</li>
     * <li>{@link ValueType#DATE} for any date / time types.</li>
     * <li>{@link ValueType#FILE_RESOURCE} for any file types.</li>
     * <li>{@link ValueType#COORDINATE} for any geometry types.</li>
     * <li>{@link ValueType#TEXT} for any textual types.</li>
     * </ul>
     *
     * @return a simplified value type.
     */
    public ValueType asSimplifiedValueType()
    {
        if ( isNumeric() )
        {
            return ValueType.NUMBER;
        }
        else if ( isBoolean() )
        {
            return ValueType.BOOLEAN;
        }
        else if ( isDate() )
        {
            return ValueType.DATE;
        }
        else if ( isFile() )
        {
            return ValueType.FILE_RESOURCE;
        }
        else if ( isGeo() )
        {
            return ValueType.COORDINATE;
        }
        else
        {
            return ValueType.TEXT;
        }
    }

    public static ValueType fromString( String valueType )
        throws IllegalArgumentException
    {
        return Arrays.stream( ValueType.values() ).filter( v -> v.toString().equals( valueType ) ).findFirst()
            .orElseThrow( () -> new IllegalArgumentException( "unknown value: " + valueType ) );
    }
}
