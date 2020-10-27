package com.mass3d.random;

import io.github.benas.randombeans.api.Randomizer;
import org.apache.commons.lang3.RandomUtils;

public class IdRandomizer
    implements
    Randomizer<Long>
{
    @Override
    public Long getRandomValue()
    {
        return RandomUtils.nextLong( 1, 100000 );
    }
}
