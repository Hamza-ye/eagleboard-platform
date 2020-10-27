package com.mass3d.webapi.controller.exception;

public class NotFoundException extends Exception
{
    public NotFoundException()
    {
        super( "Object not found." );
    }

    public NotFoundException( String uid )
    {
        super( "Object not found for uid: " + uid );
    }

    public NotFoundException( String type, String uid )
    {
        super( type + " not found for uid: " + uid );
    }
}
