package com.mass3d.period;

import java.util.Date;

public class ConfigurablePeriod
    extends Period
{
    private String value;

    public ConfigurablePeriod( String value )
    {
        this.value = value;
        this.name = value;
        this.code = value;
        this.setStartDate( new Date() );
        this.setEndDate( new Date() );
    }
    
    @Override
    public String getIsoDate()
    {
        return value;
    }

    // -------------------------------------------------------------------------
    // hashCode, equals and toString
    // -------------------------------------------------------------------------

    @Override
    public int hashCode()
    {
        return value.hashCode();
    }

    @Override
    public boolean equals( Object o )
    {
        if ( this == o )
        {
            return true;
        }

        if ( o == null )
        {
            return false;
        }

        if ( !(o instanceof Period) )
        {
            return false;
        }

        final Period other = (Period) o;

        return value.equals( other.getIsoDate() );
    }

    @Override
    public String toString()
    {
        return "[" + value + "]";
    }
}
