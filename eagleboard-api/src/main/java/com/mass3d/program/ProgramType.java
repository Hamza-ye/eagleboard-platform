package com.mass3d.program;

public enum ProgramType
{
    WITH_REGISTRATION( "with_registration" ),
    WITHOUT_REGISTRATION( "without_registration" );

    private final String value;

    ProgramType( String value )
    {
        this.value = value;
    }

    public static ProgramType fromValue( String value )
    {
        for ( ProgramType programType : ProgramType.values() )
        {
            if ( programType.value.equalsIgnoreCase( value ) )
            {
                return programType;
            }
        }

        return null;
    }

    public String getValue()
    {
        return value;
    }

}
