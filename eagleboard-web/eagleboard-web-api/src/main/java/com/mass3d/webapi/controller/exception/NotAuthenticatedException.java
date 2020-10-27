package com.mass3d.webapi.controller.exception;

public class NotAuthenticatedException extends Exception
{
    public NotAuthenticatedException()
    {
        super( "User object is null, user is not authenticated." );
    }
}
