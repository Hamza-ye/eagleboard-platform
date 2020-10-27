package com.mass3d.random;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.shape.random.RandomPointsInGridBuilder;
import io.github.benas.randombeans.api.Randomizer;

public class GeometryRandomizer
    implements
    Randomizer<Geometry>
{

    @Override
    public Geometry getRandomValue()
    {
        return new RandomPointsInGridBuilder().getGeometry();
    }
}
