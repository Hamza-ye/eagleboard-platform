package com.mass3d.dataelement;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.mass3d.common.DxfNamespaces;

@JacksonXmlRootElement( localName = "dataElementDomain", namespace = DxfNamespaces.DXF_2_0 )
public enum DataElementDomain
{
    AGGREGATE( "aggregate" ), TRACKER( "tracker" );

    private final String value;

    DataElementDomain( String value )
    {
        this.value = value;
    }

    public static DataElementDomain fromValue( String value )
    {
        for ( DataElementDomain domainType : DataElementDomain.values() )
        {
            if ( domainType.value.equalsIgnoreCase( value ) )
            {
                return domainType;
            }
        }

        return null;
    }
    
    public String getValue()
    {
        return value;
    }
}
