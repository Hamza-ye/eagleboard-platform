package com.mass3d.schema.descriptors;

import com.mass3d.dataset.DataInputPeriod;
import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaDescriptor;

public class DataInputPeriodSchemaDescriptor implements SchemaDescriptor
{
    public static final String SINGULAR = "dataInputPeriod";

    public static final String PLURAL = "dataInputPeriods";

    public static final String API_ENDPOINT = "/" + PLURAL;

    @Override
    public Schema getSchema()
    {
        return new Schema( DataInputPeriod.class, SINGULAR, PLURAL );
    }
}
