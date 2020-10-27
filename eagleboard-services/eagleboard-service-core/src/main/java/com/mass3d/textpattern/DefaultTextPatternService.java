package com.mass3d.textpattern;

import com.google.common.collect.ImmutableMap;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service("com.mass3d.textpattern.TextPatternService")
public class DefaultTextPatternService
    implements TextPatternService
{
    @Override
    public String resolvePattern( TextPattern pattern, Map<String, String> values )
        throws TextPatternGenerationException
    {
        StringBuilder resolvedPattern = new StringBuilder();

        for ( TextPatternSegment segment : pattern.getSegments() )
        {
            if ( isRequired( segment ) )
            {
                resolvedPattern.append( handleRequiredValue( segment, getSegmentValue( segment, values ) ) );
            }
            else if ( isOptional( segment ) )
            {
                resolvedPattern.append( handleOptionalValue( segment, getSegmentValue( segment, values ) ) );
            }
            else
            {
                resolvedPattern.append( handleFixedValues( segment ) );
            }
        }

        return resolvedPattern.toString();
    }

    @Override
    public Map<String, List<String>> getRequiredValues( TextPattern pattern )
    {
        return ImmutableMap.<String, List<String>>builder()
            .put( "REQUIRED", pattern.getSegments()
                .stream()
                .filter( this::isRequired )
                .map( segment -> segment.getMethod().name() )
                .collect( Collectors.toList() ) )
            .put( "OPTIONAL", pattern.getSegments()
                .stream()
                .filter( this::isOptional )
                .map( (segment -> segment.getMethod().name()) )
                .collect( Collectors.toList() ) )
            .build();
    }

    @Override
    public boolean validate( TextPattern textPattern, String text )
    {
        return TextPatternValidationUtils.validateTextPatternValue( textPattern, text );
    }

    private String handleFixedValues( TextPatternSegment segment )
    {
        if ( TextPatternMethod.CURRENT_DATE.getType().validatePattern( segment.getRawSegment() ) )
        {
            return new SimpleDateFormat( segment.getParameter() ).format( new Date() );
        }
        else
        {
            return segment.getParameter();
        }
    }

    private String handleOptionalValue( TextPatternSegment segment, String value )
        throws TextPatternGenerationException
    {
        if ( value != null && !TextPatternValidationUtils.validateSegmentValue( segment, value ) )
        {
            throw new TextPatternGenerationException( "Supplied optional value is invalid" );
        }
        else if ( value != null )
        {
            return getFormattedValue( segment, value );
        }
        else
        {
            return segment.getRawSegment();
        }
    }

    private String handleRequiredValue( TextPatternSegment segment, String value )
        throws TextPatternGenerationException
    {
        if ( value == null )
        {
            throw new TextPatternGenerationException( "Missing required value '" + segment.getMethod().name() + "'" );
        }

        String res = getFormattedValue( segment, value );

        if ( res == null || !TextPatternValidationUtils.validateSegmentValue( segment, res ) )
        {
            throw new TextPatternGenerationException( "Value is invalid: " + segment.getMethod().name() + " -> " + value );
        }

        return res;
    }

    private String getFormattedValue( TextPatternSegment segment, String value )
    {
        MethodType methodType = segment.getMethod().getType();

        return methodType.getFormattedText( segment.getParameter(), value );
    }

    private String getSegmentValue( TextPatternSegment segment, Map<String, String> values )
    {
        return values.get( segment.getMethod().name() );
    }

    private boolean isRequired( TextPatternSegment segment )
    {
        return segment.getMethod().isRequired();
    }

    private boolean isOptional( TextPatternSegment segment )
    {
        return segment.getMethod().isOptional();
    }
}
