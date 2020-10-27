package com.mass3d.schema.descriptors;

import com.mass3d.dataelement.DataElementGroupSetDimension;
import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaDescriptor;

public class DataElementGroupSetDimensionSchemaDescriptor implements SchemaDescriptor
{
    public static final String SINGULAR = "dataElementGroupSetDimension";

    public static final String PLURAL = "dataElementGroupSetDimensions";

    public static final String API_ENDPOINT = "/" + PLURAL;

    @Override
    public Schema getSchema()
    {
        return new Schema( DataElementGroupSetDimension.class, SINGULAR, PLURAL );
    }
}
