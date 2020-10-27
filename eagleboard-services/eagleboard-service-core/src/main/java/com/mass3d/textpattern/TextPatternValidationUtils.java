package com.mass3d.textpattern;

import java.util.regex.Pattern;
import com.mass3d.common.ValueType;

public class TextPatternValidationUtils
{
    public static boolean validateSegmentValue( TextPatternSegment segment, String value )
    {
        return segment.getMethod().getType().validateText( segment.getParameter(), value );
    }

    public static boolean validateTextPatternValue( TextPattern textPattern, String value )
    {
        StringBuilder builder = new StringBuilder();

        builder.append( "^" );

        textPattern.getSegments().forEach(
            ( segment ) -> builder.append( segment.getMethod().getType().getValueRegex( segment.getParameter() ) ) );

        builder.append( "$" );

        return Pattern.compile( builder.toString() ).matcher( value ).matches();
    }

    public static long getTotalValuesPotential( TextPatternSegment generatedSegment )
    {
        long res = 1;
        if ( generatedSegment != null )
        {
            if ( TextPatternMethod.SEQUENTIAL.equals( generatedSegment.getMethod() ) )
            {
                // Subtract by 1 since we don't use all zeroes.
                res = (long) (Math.pow( 10, generatedSegment.getParameter().length() ) - 1);
            }
            else if ( TextPatternMethod.RANDOM.equals( generatedSegment.getMethod() ) )
            {
                for ( char c : generatedSegment.getParameter().toCharArray() )
                {
                    switch ( c )
                    {
                    case '*':
                        res = res * 62;
                        break;
                    case '#':
                        res = res * 10;
                        break;
                    case 'X':
                        res = res * 26;
                        break;
                    case 'x':
                        res = res * 26;
                        break;
                    default:
                        break;
                    }
                }
            }
        }

        if ( res < 0 )
        {
            res = Long.MAX_VALUE;
        }

        return res;
    }

    public static boolean validateValueType( TextPattern textPattern, ValueType valueType )
    {
        if ( ValueType.TEXT.equals( valueType ) )
        {
            return true;
        }
        else if ( ValueType.NUMBER.equals( valueType ) )
        {
            boolean isAllNumbers = true;

            for ( TextPatternSegment segment : textPattern.getSegments() )
            {
                isAllNumbers = isAllNumbers && isNumericOnly( segment );
            }

            return isAllNumbers;
        }
        else
        {
            return false;
        }
    }

    private static boolean isNumericOnly( TextPatternSegment segment )
    {
        if ( TextPatternMethod.SEQUENTIAL.equals( segment.getMethod() ) )
        {
            return true;
        }

        if ( TextPatternMethod.RANDOM.equals( segment.getMethod() ) )
        {
            return segment.getParameter().matches( "^#+$" );
        }

        if ( TextPatternMethod.TEXT.equals( segment.getMethod() ) )
        {
            return segment.getParameter().matches( "^[0-9]*$" );
        }

        return false;
    }
}
