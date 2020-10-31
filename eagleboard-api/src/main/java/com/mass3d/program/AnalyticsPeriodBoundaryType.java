package com.mass3d.program;

public enum AnalyticsPeriodBoundaryType
{
    BEFORE_START_OF_REPORTING_PERIOD( "before_start_of_reporting_period" ), 
    BEFORE_END_OF_REPORTING_PERIOD( "before_end_of_reporting_period" ),
    AFTER_START_OF_REPORTING_PERIOD( "after_start_of_reporting_period" ), 
    AFTER_END_OF_REPORTING_PERIOD( "after_end_of_reporting_period" );
    
    private final String value;

    private AnalyticsPeriodBoundaryType( String value )
    {
        this.value = value;
    }

    public static AnalyticsPeriodBoundaryType fromValue( String value )
    {
        for ( AnalyticsPeriodBoundaryType analyticsPeriodBoundaryType : AnalyticsPeriodBoundaryType.values() )
        {
            if ( analyticsPeriodBoundaryType.getValue().equalsIgnoreCase( value ) )
            {
                return analyticsPeriodBoundaryType;
            }
        }

        return null;
    }
    
    public String getValue()
    {
        return value;
    }
    
    public Boolean isEndBoundary()
    {
        return this == BEFORE_END_OF_REPORTING_PERIOD || this == BEFORE_START_OF_REPORTING_PERIOD;
    }
    
    public Boolean isStartBoundary()
    {
        return this == AFTER_END_OF_REPORTING_PERIOD || this == AFTER_START_OF_REPORTING_PERIOD;
    }
}
