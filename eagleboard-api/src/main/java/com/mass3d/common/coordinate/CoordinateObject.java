package com.mass3d.common.coordinate;

import com.vividsolutions.jts.geom.Geometry;
import com.mass3d.organisationunit.FeatureType;

public interface CoordinateObject
{
    FeatureType getFeatureType();

    String getCoordinates();

    boolean hasCoordinates();

    boolean hasDescendantsWithCoordinates();

    default String extractCoordinates(Geometry geometry)
    {
        return CoordinateUtils.getCoordinatesFromGeometry( geometry );
    }
}
