package com.mass3d.external.configuration;

/**
 * @version $Id$
 */
public class NoConfigurationFoundException
    extends Exception
{
    public NoConfigurationFoundException( String message )
    {
        super( message );
    }
    
    public NoConfigurationFoundException( Throwable throable )
    {
        super( throable );
    }
    
    public NoConfigurationFoundException( String message, Throwable trowable )
    {
        super( message, trowable );
    }
}
