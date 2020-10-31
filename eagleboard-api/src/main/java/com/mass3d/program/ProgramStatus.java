package com.mass3d.program;

public enum ProgramStatus
{
    ACTIVE( 0 ), COMPLETED( 1 ), CANCELLED( 2 );

    private int value;

    ProgramStatus( int value )
    {
        this.value = value;
    }

    public int getValue()
    {
        return value;
    }
}
