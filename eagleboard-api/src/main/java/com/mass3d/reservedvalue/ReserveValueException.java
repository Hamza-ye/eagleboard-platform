package com.mass3d.reservedvalue;

public class ReserveValueException
    extends Exception
{
    ReserveValueException( String message )
    {
        super( "Could not reserve value: " + message );
    }
}
