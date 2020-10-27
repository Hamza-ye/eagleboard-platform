package com.mass3d.common;

public enum AccessLevel
{
    OPEN( 0 ),
    AUDITED( 1 ),
    PROTECTED( 2 ),
    CLOSED( 3 );
    
    private final int value;

    AccessLevel( int value )
    {
        this.value = value;
    }

    public int getValue()
    {
        return value;
    }
}
