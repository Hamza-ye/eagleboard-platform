package com.mass3d.node.exception;

public class InvalidTypeException extends RuntimeException
{
    public InvalidTypeException()
    {
        super( "Adding children to a node of type simple is not allowed." );
    }
}
