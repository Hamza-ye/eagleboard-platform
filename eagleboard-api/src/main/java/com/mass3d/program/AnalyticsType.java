package com.mass3d.program;

public enum AnalyticsType
{
    EVENT( "event" ), 
    ENROLLMENT( "enrollment" );
    
    private final String value;

    private AnalyticsType( String value )
    {
        this.value = value;
    }

    public static AnalyticsType fromValue( String value )
    {
        for ( AnalyticsType analyticsType : AnalyticsType.values() )
        {
            if ( analyticsType.getValue().equalsIgnoreCase( value ) )
            {
                return analyticsType;
            }
        }

        return null;
    }
    
    public String getValue()
    {
        return value;
    }
}
