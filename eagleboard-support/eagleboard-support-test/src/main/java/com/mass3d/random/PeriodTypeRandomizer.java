package com.mass3d.random;

import io.github.benas.randombeans.api.Randomizer;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import com.mass3d.period.PeriodType;

public class PeriodTypeRandomizer
    implements
    Randomizer<PeriodType>
{
    private List<PeriodType> periodTypes = Arrays.asList(
        PeriodType.getPeriodTypeFromIsoString( "2011" ),
        PeriodType.getPeriodTypeFromIsoString( "201101" ),
        PeriodType.getPeriodTypeFromIsoString( "2011W1" ),
        PeriodType.getPeriodTypeFromIsoString( "2011W32" ),
        PeriodType.getPeriodTypeFromIsoString( "20110101" ),
        PeriodType.getPeriodTypeFromIsoString( "2011Q3" ),
        PeriodType.getPeriodTypeFromIsoString( "201101B" ),
        PeriodType.getPeriodTypeFromIsoString( "2011S1" ),
        PeriodType.getPeriodTypeFromIsoString( "2011AprilS1" ),
        PeriodType.getPeriodTypeFromIsoString( "2011April" ),
        PeriodType.getPeriodTypeFromIsoString( "2011July" ),
        PeriodType.getPeriodTypeFromIsoString( "2011Oct" )
    );

    @Override
    public PeriodType getRandomValue()
    {
        return periodTypes.get(new Random().nextInt(periodTypes.size() -1 ));
    }
}
