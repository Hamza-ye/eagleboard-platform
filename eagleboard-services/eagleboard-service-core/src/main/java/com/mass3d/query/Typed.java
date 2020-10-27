package com.mass3d.query;

import com.google.common.collect.Iterators;
import com.mass3d.schema.Klass;

/**
 * Simple class for checking if an object is one of several allowed classes, mainly used in Operator where
 * a parameter can be type constrained.
 *
 */
public class Typed
{
    private final Class<?>[] klasses;

    public Typed( Class<?>[] klasses )
    {
        this.klasses = klasses;
    }

    public Class<?>[] getKlasses()
    {
        return klasses;
    }

    public boolean isValid( Klass klass )
    {
        return klass == null || isValid( klass.getKlass() );
    }

    public boolean isValid( Class<?> klass )
    {
        if ( klasses.length == 0 || klass == null )
        {
            return true;
        }

        for ( Class<?> k : klasses )
        {
            if ( k != null && k.isAssignableFrom( klass ) )
            {
                return true;
            }
        }

        return false;
    }

    public static Typed from( Class<?>... klasses )
    {
        return new Typed( klasses );
    }

    public static Typed from( Iterable<? extends Class<?>> iterable )
    {
        return new Typed( Iterators.toArray( iterable.iterator(), Class.class ) );
    }
}
