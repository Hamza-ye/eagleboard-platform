package com.mass3d.commons.config.jackson.geometry;

import com.bedatadriven.jackson.datatype.jts.serialization.GeometryDeserializer;
import com.bedatadriven.jackson.datatype.jts.serialization.GeometrySerializer;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

public class JtsXmlModule
    extends SimpleModule
{
    public JtsXmlModule()
    {
        this( new GeometryFactory() );
    }

    @SuppressWarnings({"rawtypes","unchecked"})
    public JtsXmlModule( GeometryFactory geometryFactory )
    {
        super( "JtsXmlModule", new Version( 1, 0, 0, (String) null, "org.dhis", "dhis-service-node" ) );
        this.addSerializer( Geometry.class, new GeometrySerializer() );
        XmlGenericGeometryParser genericGeometryParser = new XmlGenericGeometryParser( geometryFactory );
        this.addDeserializer( Geometry.class, new GeometryDeserializer( genericGeometryParser ) );
    }

    @Override
    public void setupModule( SetupContext context )
    {
        super.setupModule( context );
    }
}
