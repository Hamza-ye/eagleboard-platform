package com.mass3d.commons.config.jackson.geometry;

import com.bedatadriven.jackson.datatype.jts.parsers.BaseParser;
import com.bedatadriven.jackson.datatype.jts.parsers.GeometryParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class XmlGenericGeometryParser
    extends BaseParser
    implements GeometryParser<Geometry>
{
    public XmlGenericGeometryParser( GeometryFactory geometryFactory )
    {
        super( geometryFactory );
    }

    public Geometry geometryFromJson( JsonNode node )
    {
        WKTReader wktR = new WKTReader();
        try
        {
            return wktR.read( node.asText() );
        }
        catch ( ParseException e )
        {
            log.error( "Error reading WKT of geometry", e );
            return null;
        }
    }
}
