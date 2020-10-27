package com.mass3d.dxf2.datavalueset;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import java.io.IOException;
import com.mass3d.dxf2.datavalue.DataValue;
import com.mass3d.dxf2.datavalue.StreamingCsvDataValue;

public class StreamingCsvDataValueSet
    extends DataValueSet
{
    private CsvWriter writer;
    
    private CsvReader reader;
    
    public StreamingCsvDataValueSet( CsvWriter writer )
    {
        this.writer = writer;
        
        try
        {
            this.writer.writeRecord( StreamingCsvDataValue.getHeaders() ); // Write headers
        }
        catch ( IOException ex )
        {
            throw new RuntimeException( "Failed to write CSV headers", ex );
        }
    }
    
    public StreamingCsvDataValueSet( CsvReader reader )
    {
        this.reader = reader;
    }
    
    @Override
    public boolean hasNextDataValue()
    {
        try
        {
            return reader.readRecord();
        }
        catch ( IOException ex )
        {
            throw new RuntimeException( "Failed to read record", ex );
        }
    }

    @Override
    public DataValue getNextDataValue()
    {
        try
        {
            return new StreamingCsvDataValue( reader.getValues() );
        }
        catch ( IOException ex )
        {
            throw new RuntimeException( "Failed to get CSV values", ex );
        }
    }

    @Override
    public DataValue getDataValueInstance()
    {
        return new StreamingCsvDataValue( writer );
    }

    @Override
    public void close()
    {
        if ( writer != null )
        {
            writer.close();
        }
        
        if ( reader != null )
        {
            reader.close();
        }
    }    
}
