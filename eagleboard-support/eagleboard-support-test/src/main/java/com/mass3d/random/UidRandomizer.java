package com.mass3d.random;

import io.github.benas.randombeans.api.Randomizer;
import com.mass3d.common.CodeGenerator;

public class UidRandomizer
    implements
    Randomizer<String>
{
    @Override
    public String getRandomValue()
    {
        return CodeGenerator.generateUid();
    }
}
