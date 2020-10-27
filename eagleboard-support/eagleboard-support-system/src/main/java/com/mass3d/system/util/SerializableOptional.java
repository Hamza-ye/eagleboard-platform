package com.mass3d.system.util;

import java.io.Serializable;

/**
 * Optional for {@link Serializable} values. Accepts nulls.
 *
 */
public class SerializableOptional
    implements Serializable
{
    private final Serializable value;

    private SerializableOptional()
    {
        this.value = null;
    }

    private SerializableOptional( Serializable value )
    {
        this.value = value;
    }

    /**
     * Creates a {@link SerializableOptional} with the given value.
     *
     * @param value the value.
     * @return a {@link SerializableOptional}.
     */
    public static SerializableOptional of( Serializable value )
    {
        return new SerializableOptional( value );
    }

    /**
     * Returns a {@link SerializableOptional} with a null value.
     *
     * @return a {@link SerializableOptional} with a null value.
     */
    public static SerializableOptional empty()
    {
        return new SerializableOptional();
    }

    /**
     * Indicates whether a value is present.
     *
     * @return true if a value is present.
     */
    public boolean isPresent()
    {
        return value != null;
    }

    /**
     * Returns the value, may be null.
     *
     * @return the value.
     */
    public Serializable get()
    {
        return value;
    }
}
