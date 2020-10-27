package com.mass3d.fieldfilter;

import com.mass3d.schema.Schema;

public class FieldFilterException
    extends RuntimeException
{
    private final String fieldKey;

    private final Schema schema;

    public FieldFilterException( String fieldKey, Schema schema )
    {
        super( "Unknown field property `" + fieldKey + "`, available fields are " + schema.getPropertyMap().keySet() );
        this.fieldKey = fieldKey;
        this.schema = schema;
    }

    public String getFieldKey()
    {
        return fieldKey;
    }

    public Schema getSchema()
    {
        return schema;
    }
}
