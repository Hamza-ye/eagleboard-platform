package com.mass3d.expression;

public enum Operator
{
    equal_to( "==" ),
    not_equal_to( "!=" ),
    greater_than( ">" ),
    greater_than_or_equal_to( ">=" ),
    less_than( "<" ),
    less_than_or_equal_to( "<=" ),
    compulsory_pair( "[Compulsory pair]" ),
    exclusive_pair( "[Exclusive pair]" );

    private final String mathematicalOperator;

    Operator( String mathematicalOperator )
    {
        this.mathematicalOperator = mathematicalOperator;
    }

    public String getMathematicalOperator()
    {
        return mathematicalOperator;
    }

    public static Operator fromValue( String value )
    {
        for ( Operator operator : Operator.values() )
        {
            if ( operator.mathematicalOperator.equalsIgnoreCase( value ) )
            {
                return operator;
            }
        }

        return null;
    }

    public static Operator safeValueOf( String name )
    {
        return name != null ? Operator.valueOf( name ) : null;
    }
}
