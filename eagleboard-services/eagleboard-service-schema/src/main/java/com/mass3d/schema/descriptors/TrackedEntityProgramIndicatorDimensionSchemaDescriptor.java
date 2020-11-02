package com.mass3d.schema.descriptors;

import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaDescriptor;
import com.mass3d.trackedentity.TrackedEntityProgramIndicatorDimension;

public class TrackedEntityProgramIndicatorDimensionSchemaDescriptor implements SchemaDescriptor
{
    public static final String SINGULAR = "dataElementDimension";

    public static final String PLURAL = "dataElementDimensions";

    public static final String API_ENDPOINT = "/" + PLURAL;

    @Override
    public Schema getSchema()
    {
        return new Schema( TrackedEntityProgramIndicatorDimension.class, SINGULAR, PLURAL );
    }
}
