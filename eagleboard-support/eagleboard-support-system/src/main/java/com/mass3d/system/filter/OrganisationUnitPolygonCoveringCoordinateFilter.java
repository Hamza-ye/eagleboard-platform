package com.mass3d.system.filter;

import com.vividsolutions.jts.geom.Geometry;
import com.mass3d.commons.filter.Filter;
import com.mass3d.organisationunit.FeatureType;
import com.mass3d.organisationunit.OrganisationUnit;
import com.mass3d.system.util.GeoUtils;

public class OrganisationUnitPolygonCoveringCoordinateFilter
    implements Filter<OrganisationUnit>
{
    private double longitude;
    private double latitude;

    public OrganisationUnitPolygonCoveringCoordinateFilter( double longitude, double latitude )
    {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    @Override
    public boolean retain( OrganisationUnit unit )
    {
        Geometry geometry = unit.getGeometry();
        return geometry != null && FeatureType.getTypeFromName(geometry.getGeometryType()) == FeatureType.POLYGON
            && GeoUtils.checkPointWithMultiPolygon( longitude, latitude, unit.getGeometry() );
    }
}
