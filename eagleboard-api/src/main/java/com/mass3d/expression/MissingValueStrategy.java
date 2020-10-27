package com.mass3d.expression;

public enum MissingValueStrategy
{
    SKIP_IF_ANY_VALUE_MISSING( "skip_if_any_value_missing" ),
    SKIP_IF_ALL_VALUES_MISSING( "skip_if_all_values_missing" ),
    NEVER_SKIP( "never_skip" );

    private final String value;

    MissingValueStrategy( String value )
    {
        this.value = value;
    }

    public String getValue()
    {
        return value;
    }

    public static MissingValueStrategy safeValueOf( String value )
    {
        return value != null ? MissingValueStrategy.valueOf( value ) : null;
    }

    public static MissingValueStrategy safeValueOf( String value, MissingValueStrategy defaultValue )
    {
        return value != null ? MissingValueStrategy.valueOf( value ) : defaultValue;
    }
}
