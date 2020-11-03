package com.mass3d.reservedvalue;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeoutException;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import com.mass3d.textpattern.TextPattern;
import com.mass3d.textpattern.TextPatternGenerationException;
import com.mass3d.textpattern.TextPatternMethod;
import com.mass3d.textpattern.TextPatternMethodUtils;
import com.mass3d.textpattern.TextPatternSegment;
import com.mass3d.textpattern.TextPatternService;
import com.mass3d.textpattern.TextPatternValidationUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service( "com.mass3d.reservedvalue.ReservedValueService" )
public class DefaultReservedValueService
    implements ReservedValueService
{
    private static final long GENERATION_TIMEOUT = (1000 * 30); // 30 seconds

    private TextPatternService textPatternService;

    private ReservedValueStore reservedValueStore;

    private SequentialNumberCounterStore sequentialNumberCounterStore;

    public DefaultReservedValueService( TextPatternService textPatternService, ReservedValueStore reservedValueStore,
        SequentialNumberCounterStore sequentialNumberCounterStore )
    {
        checkNotNull( textPatternService );
        checkNotNull( reservedValueStore );
        checkNotNull( sequentialNumberCounterStore );

        this.textPatternService = textPatternService;
        this.reservedValueStore = reservedValueStore;
        this.sequentialNumberCounterStore = sequentialNumberCounterStore;
    }

    @Override
    @Transactional
    public List<ReservedValue> reserve( TextPattern textPattern, int numberOfReservations, Map<String, String> values, Date expires )
        throws ReserveValueException, TextPatternGenerationException
    {
        long startTime = System.currentTimeMillis();
        int attemptsLeft = 10;

        List<ReservedValue> resultList = new ArrayList<>();

        TextPatternSegment generatedSegment = getGeneratedSegment( textPattern );

        String key = textPatternService.resolvePattern( textPattern, values );

        // Used for searching value tables
        String valueKey = ( generatedSegment != null ?
            key.replaceAll( Pattern.quote( generatedSegment.getRawSegment() ), "%" ) :
            key );

        ReservedValue reservedValue = new ReservedValue( textPattern.getOwnerObject().name(), textPattern.getOwnerUid(),
            key,
            valueKey,
            expires );

        if ( !hasEnoughValuesLeft( reservedValue,
            TextPatternValidationUtils.getTotalValuesPotential( generatedSegment ),
            numberOfReservations ) )
        {
            throw new ReserveValueException( "Not enough values left to reserve " + numberOfReservations + " values." );
        }

        if ( generatedSegment == null && numberOfReservations == 1 )
        {
            reservedValue.setValue( key );
            return reservedValueStore.reserveValues( reservedValue, Lists.newArrayList( key ) );
        }

        List<String> usedGeneratedValues = new ArrayList<>();

        int numberOfValuesLeftToGenerate = numberOfReservations;

        try
        {
            while ( attemptsLeft-- > 0 && numberOfValuesLeftToGenerate > 0 )
            {
                if ( System.currentTimeMillis() - startTime >= GENERATION_TIMEOUT )
                {
                    throw new TimeoutException( "Generation and reservation of values took too long" );
                }

                List<String> resolvedPatterns = new ArrayList<>();

                List<String> generatedValues = new ArrayList<>();

                int maxGenerateAttempts = 10;

                while ( generatedValues.size() < numberOfValuesLeftToGenerate && maxGenerateAttempts-- > 0 )
                {
                    generatedValues.addAll( generateValues( textPattern, key, numberOfReservations - resultList.size() ) );
                    generatedValues.removeAll( usedGeneratedValues );
                }

                usedGeneratedValues.addAll( generatedValues );

                // Get a list of resolved patterns
                for ( int i = 0; i < numberOfReservations - resultList.size(); i++ )
                {
                    resolvedPatterns.add( textPatternService.resolvePattern( textPattern,
                        ImmutableMap.<String, String>builder()
                            .putAll( values )
                            .put( generatedSegment.getMethod().name(), generatedValues.get( i ) )
                            .build() ) );
                }

                resultList.addAll( reservedValueStore.reserveValues( reservedValue, resolvedPatterns ) );

                numberOfValuesLeftToGenerate = numberOfReservations - resultList.size();
            }
        }
        catch ( TimeoutException ex )
        {
            log.warn( String.format(
                "Generation and reservation of values for %s wih uid %s timed out. %s values was reserved. You might be running low on available values",
                textPattern.getOwnerObject().name(), textPattern.getOwnerUid(), resultList.size() ) );
        }

        return resultList;
    }

    @Override
    @Transactional
    public boolean useReservedValue( TextPattern textPattern, String value )
    {
        return reservedValueStore.useReservedValue( textPattern.getOwnerUid(), value );
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isReserved( TextPattern textPattern, String value )
    {
        return reservedValueStore.isReserved( textPattern.getOwnerObject().name(), textPattern.getOwnerUid(), value );
    }

    @Override
    @Transactional
    public void removeExpiredReservations()
    {
        reservedValueStore.removeExpiredReservations();
    }

    @Override
    @Transactional
    public void deleteReservedValueByUid( String uid )
    {
        reservedValueStore.deleteReservedValueByUid( uid );
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    private TextPatternSegment getGeneratedSegment( TextPattern textPattern )
    {
        return textPattern.getSegments()
            .stream()
            .filter( ( tp ) -> tp.getMethod().isGenerated() )
            .findFirst()
            .orElse( null );
    }

    private List<String> generateValues( TextPattern textPattern, String key, int numberOfValues )
    {
        List<String> generatedValues = new ArrayList<>();
        TextPatternSegment segment = getGeneratedSegment( textPattern );

        if ( segment.getMethod().equals( TextPatternMethod.SEQUENTIAL ) )
        {
            generatedValues.addAll( sequentialNumberCounterStore
                .getNextValues( textPattern.getOwnerUid(), key, numberOfValues )
                .stream()
                .map( ( n ) -> String.format( "%0" + segment.getParameter().length() + "d", n ) )
                .collect( Collectors.toList() ) );
        }
        else if ( segment.getMethod().equals( TextPatternMethod.RANDOM ) )
        {
            for ( int i = 0; i < numberOfValues; i++ )
            {
                generatedValues.add( TextPatternMethodUtils.generateRandom( new Random(), segment.getParameter() ) );
            }
        }

        return generatedValues;
    }

    private boolean hasEnoughValuesLeft( ReservedValue reservedValue, long totalValues, int valuesRequired )
    {
        int used = reservedValueStore.getNumberOfUsedValues( reservedValue );

        return totalValues >= valuesRequired + used;
    }
}
