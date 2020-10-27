package com.mass3d.dxf2.datavalue;

import com.fasterxml.jackson.core.JsonGenerator;
import java.io.IOException;

public class StreamingJsonDataValue extends DataValue
{
    private JsonGenerator generator;

    public StreamingJsonDataValue( JsonGenerator generator )
    {
        this.generator = generator;

        try
        {
            generator.writeStartObject();
        }
        catch ( IOException ignored )
        {

        }
    }

    @Override
    public void setDataElement( String dataElement )
    {
        writeObjectField( "dataElement", dataElement );
    }

    @Override
    public void setPeriod( String period )
    {
        writeObjectField( "period", period );
    }

    @Override
    public void setOrgUnit( String orgUnit )
    {
        writeObjectField( "orgUnit", orgUnit );
    }

    @Override
    public void setCategoryOptionCombo( String categoryOptionCombo )
    {
        writeObjectField( "categoryOptionCombo", categoryOptionCombo );
    }

    @Override
    public void setAttributeOptionCombo( String attributeOptionCombo )
    {
        writeObjectField( "attributeOptionCombo", attributeOptionCombo );
    }

    @Override
    public void setValue( String value )
    {
        writeObjectField( "value", value );
    }

    @Override
    public void setStoredBy( String storedBy )
    {
        writeObjectField( "storedBy", storedBy );
    }

    @Override
    public void setCreated( String created )
    {
        writeObjectField( "created", created );
    }

    @Override
    public void setLastUpdated( String lastUpdated )
    {
        writeObjectField( "lastUpdated", lastUpdated );
    }

    @Override
    public void setComment( String comment )
    {
        writeObjectField( "comment", comment );
    }

    @Override
    public void setFollowup( Boolean followup )
    {
        writeObjectField( "followup", followup );
    }
    
    @Override
    public void setDeleted( Boolean deleted )
    {
        writeObjectField( "deleted", deleted );
    }

    @Override
    public void close()
    {
        if ( generator == null )
        {
            return;
        }

        try
        {
            generator.writeEndObject();
        }
        catch ( IOException ignored )
        {
        }
    }

    private void writeObjectField( String fieldName, Object value )
    {
        if ( value == null )
        {
            return;
        }

        try
        {
            generator.writeObjectField( fieldName, value );
        }
        catch ( IOException ignored )
        {
        }
    }
}
