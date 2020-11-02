package com.mass3d.schema.descriptors;

import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaDescriptor;
import com.mass3d.trackedentity.TrackedEntityDataElementDimension;

public class TrackedEntityDataElementDimensionSchemaDescriptor implements SchemaDescriptor
{
    public static final String SINGULAR = "trackedEntityDataElementDimension";

    public static final String PLURAL = "trackedEntityDataElementDimensions";

    public static final String API_ENDPOINT = "/" + PLURAL;

    @Override
    public Schema getSchema()
    {
        return new Schema( TrackedEntityDataElementDimension.class, SINGULAR, PLURAL );
    }
}
