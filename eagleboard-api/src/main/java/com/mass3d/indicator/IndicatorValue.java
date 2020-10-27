package com.mass3d.indicator;

/**
 * Non-persisted class for representing the various components of an aggregated
 * indicator value.
 * 
 */
public class IndicatorValue
{    
    private double numeratorValue;
    
    private double denominatorValue;

    private int multiplier;

    private int divisor;

    public IndicatorValue()
    {
    }

    // -------------------------------------------------------------------------
    // Logic methods
    // -------------------------------------------------------------------------

    /**
     * Returns the calculated indicator value.
     */
    public double getValue()
    {
        return ( numeratorValue * multiplier ) / ( denominatorValue * divisor );
    }

    /**
     * Returns the ratio of the multiplier and divisor.
     */
    public double getFactor()
    {
        return ( (double) multiplier ) / ( (double) divisor );
    }

    // -------------------------------------------------------------------------
    // Get and set methods
    // -------------------------------------------------------------------------

    public double getNumeratorValue()
    {
        return numeratorValue;
    }

    public IndicatorValue setNumeratorValue( double numeratorValue )
    {
        this.numeratorValue = numeratorValue;
        return this;
    }

    public double getDenominatorValue()
    {
        return denominatorValue;
    }

    public IndicatorValue setDenominatorValue( double denominatorValue )
    {
        this.denominatorValue = denominatorValue;
        return this;
    }

    public int getMultiplier()
    {
        return multiplier;
    }

    public IndicatorValue setMultiplier( int multiplier )
    {
        this.multiplier = multiplier;
        return this;
    }

    public int getDivisor()
    {
        return divisor;
    }

    public IndicatorValue setDivisor( int divisor )
    {
        this.divisor = divisor;
        return this;
    }
}
