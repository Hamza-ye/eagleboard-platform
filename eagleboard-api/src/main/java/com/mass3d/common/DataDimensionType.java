package com.mass3d.common;

public enum DataDimensionType
{
    DISAGGREGATION("disaggregation"), ATTRIBUTE("attribute");
    
    private final String value;

    DataDimensionType( String value )
    {
        this.value = value;
    }

    public static DataDimensionType fromValue( String value )
    {
        for ( DataDimensionType type : DataDimensionType.values() )
        {
            if ( type.value.equalsIgnoreCase( value ) )
            {
                return type;
            }
        }

        return null;
    }
    
    public String getValue()
    {
        return value;
    }
}
