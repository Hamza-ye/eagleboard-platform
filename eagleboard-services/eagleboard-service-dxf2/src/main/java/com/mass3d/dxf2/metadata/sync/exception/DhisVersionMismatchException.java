package com.mass3d.dxf2.metadata.sync.exception;

public class DhisVersionMismatchException extends Exception
{
    public DhisVersionMismatchException( String message )
    {
        super( message );
    }

    public DhisVersionMismatchException( Throwable cause )
    {
        super( cause );
    }

    public DhisVersionMismatchException( String message, Throwable cause )
    {
        super( message, cause );
    }
}
