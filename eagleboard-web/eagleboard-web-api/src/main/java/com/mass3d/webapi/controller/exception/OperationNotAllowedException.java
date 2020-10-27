package com.mass3d.webapi.controller.exception;

/**
 * This exception could be used in all operation forbidden cases
 */
public class OperationNotAllowedException
    extends Exception
{

    public OperationNotAllowedException( String message )
    {
        super( message );
    }

    public OperationNotAllowedException( Throwable cause )
    {
        super( cause );
    }

    public OperationNotAllowedException( String message, Throwable cause )
    {
        super( message, cause );
    }
}
