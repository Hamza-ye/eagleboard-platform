package com.mass3d.schema;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.mass3d.common.DxfNamespaces;

@JacksonXmlRootElement( localName = "propertyType", namespace = DxfNamespaces.DXF_2_0 )
public enum PropertyType
{
    IDENTIFIER,
    TEXT,
    NUMBER,
    INTEGER,
    BOOLEAN,
    EMAIL,
    PASSWORD,
    URL,
    DATE,
    PHONENUMBER,
    GEOLOCATION,
    COLOR,
    CONSTANT,

    COMPLEX,
    COLLECTION,
    REFERENCE;

    public boolean isSimple()
    {
        return IDENTIFIER == this || TEXT == this || NUMBER == this || INTEGER == this || EMAIL == this || PASSWORD == this || URL == this
            || DATE == this || PHONENUMBER == this || GEOLOCATION == this || COLOR == this || CONSTANT == this;
    }
}
