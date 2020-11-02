package com.mass3d.schema.descriptors;

import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaDescriptor;
import com.mass3d.trackedentity.TrackedEntityInstance;

public class TrackedEntityInstanceSchemaDescriptor implements SchemaDescriptor
{
    public static final String SINGULAR = "trackedEntityInstance";

    public static final String PLURAL = "trackedEntityInstances";

    public static final String API_ENDPOINT = "/" + PLURAL;

    @Override
    public Schema getSchema()
    {
        Schema schema = new Schema( TrackedEntityInstance.class, SINGULAR, PLURAL );
        schema.setRelativeApiEndpoint( API_ENDPOINT );

        return schema;
    }
}
