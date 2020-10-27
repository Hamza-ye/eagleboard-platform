package com.mass3d.webapi.controller.exception;

public class FilterTooShortException extends Exception
{
    public FilterTooShortException()
    {
        super( "Required String parameter 'filter' must be at least 3 characters in length." );
    }
}
