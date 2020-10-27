package com.mass3d.random;

import static io.github.benas.randombeans.EnhancedRandomBuilder.aNewEnhancedRandomBuilder;
import static io.github.benas.randombeans.FieldDefinitionBuilder.*;
import static io.github.benas.randombeans.FieldDefinitionBuilder.field;

import com.vividsolutions.jts.geom.Geometry;
import io.github.benas.randombeans.api.EnhancedRandom;
import java.util.List;
import java.util.stream.Collectors;
import com.mass3d.period.PeriodType;

public class BeanRandomizer
{
    private EnhancedRandom rand;

    public BeanRandomizer()
    {
        rand = aNewEnhancedRandomBuilder()
            .randomize( PeriodType.class, new PeriodTypeRandomizer() )
            .randomize( Geometry.class, new GeometryRandomizer() )
            .randomize( field().named( "uid" ).ofType( String.class ).get(), new UidRandomizer() )
            .randomize( field().named( "id" ).ofType( long.class ).get(), new IdRandomizer() )
            .build();
    }

    /**
     * Generates an instance of the specified type and fill the instance's properties with random data
     * @param type The bean type
     * @param excludedFields a list of fields to exclude from the random population
     *
     * @return an instance of the specified type
     */
    public <T> T randomObject( final Class<T> type, final String... excludedFields )
    {
        return rand.nextObject( type, excludedFields );
    }

    /**
     * Generates multiple instances of the specified type and fills each instance's properties with random data
     * @param type The bean type
     * @param amount the amount of beans to generate
     * @param excludedFields a list of fields to exclude from the random population
     *
     * @return an instance of the specified type
     */
    public <T> List<T> randomObjects( final Class<T> type, int amount, final String... excludedFields )
    {
        return rand.objects( type, amount, excludedFields ).collect( Collectors.toList() );
    }
}
