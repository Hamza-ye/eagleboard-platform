package com.mass3d.dxf2.datavalueset;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.OutputStream;
import com.mass3d.dxf2.datavalue.DataValue;
import com.mass3d.dxf2.datavalue.StreamingJsonDataValue;

public class StreamingJsonDataValueSet extends DataValueSet
{
    private JsonGenerator generator;

    private boolean startedArray;

    public StreamingJsonDataValueSet( OutputStream out )
    {
        try
        {
            JsonFactory factory = new ObjectMapper().getFactory();
            // Disables flushing every time that an object property is written to the stream
            factory.disable( JsonGenerator.Feature.FLUSH_PASSED_TO_STREAM );
            // Do not attempt to balance unclosed tags
            factory.disable( JsonGenerator.Feature.AUTO_CLOSE_JSON_CONTENT );
            generator = factory.createGenerator( out );
            generator.writeStartObject();
        }
        catch ( IOException ignored )
        {
        }
    }

    @Override
    public void setDataElementIdScheme( String dataElementIdScheme )
    {
        writeObjectField( FIELD_DATAELEMENTIDSCHEME, dataElementIdScheme );
    }

    @Override
    public void setOrgUnitIdScheme( String orgUnitIdScheme )
    {
        writeObjectField( FIELD_ORGUNITIDSCHEME, orgUnitIdScheme );
    }

    @Override
    public void setCategoryOptionComboIdScheme( String categoryOptionComboIdScheme )
    {
        writeObjectField( FIELD_CATEGORYOPTCOMBOIDSCHEME, categoryOptionComboIdScheme );
    }

    @Override
    public void setDataSetIdScheme( String dataSetIdScheme )
    {
        writeObjectField( FIELD_DATASETIDSCHEME, dataSetIdScheme );
    }

    @Override
    public void setDataSet( String dataSet )
    {
        writeObjectField( FIELD_DATASET, dataSet );
    }

    @Override
    public void setCompleteDate( String completeDate )
    {
        writeObjectField( FIELD_COMPLETEDATE, completeDate );
    }

    @Override
    public void setPeriod( String period )
    {
        writeObjectField( FIELD_PERIOD, period );
    }

    @Override
    public void setOrgUnit( String orgUnit )
    {
        writeObjectField( FIELD_ORGUNIT, orgUnit );
    }

    @Override
    public void setAttributeOptionCombo( String attributeOptionCombo )
    {
        writeObjectField( FIELD_ATTRIBUTE_OPTION_COMBO, attributeOptionCombo );
    }

    @Override
    public DataValue getDataValueInstance()
    {
        if ( !startedArray )
        {
            try
            {
                generator.writeArrayFieldStart( "dataValues" );
                startedArray = true;
            }
            catch ( IOException ignored )
            {
            }
        }

        return new StreamingJsonDataValue( generator );
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
            if ( startedArray )
            {
                generator.writeEndArray();
            }

            generator.writeEndObject();
        }
        catch ( IOException ignored )
        {
        }
        finally
        {
            try
            {
                generator.close();
            }
            catch ( IOException ignored )
            {
            }
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
