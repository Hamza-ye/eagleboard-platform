package com.mass3d.common;

/**
 * Exception thrown when a dependency loop is found between dimensional item objects.
 *
 */
public class CyclicReferenceException
    extends RuntimeException
{
    public CyclicReferenceException( String message )
    {
        super( message );
    }

    public CyclicReferenceException( String message, Throwable throwable )
    {
        super( message, throwable );
    }
}
