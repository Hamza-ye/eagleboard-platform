package com.mass3d.node.exception;

public class DuplicateNodeException extends RuntimeException
{
    public DuplicateNodeException()
    {
        super( "A node with that name already exists in the child list." );
    }
}
