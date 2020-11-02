package com.mass3d.schema.descriptors;

import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaDescriptor;
import com.mass3d.trackedentityattributevalue.TrackedEntityAttributeValue;

public class TrackedEntityAttributeValueSchemaDescriptor implements SchemaDescriptor
{
    public static final String SINGULAR = "trackedEntityAttributeValue";

    public static final String PLURAL = "trackedEntityAttributeValues";

    public static final String API_ENDPOINT = "/" + PLURAL;

    @Override
    public Schema getSchema()
    {
        Schema schema = new Schema( TrackedEntityAttributeValue.class, SINGULAR, PLURAL );
        schema.setRelativeApiEndpoint( API_ENDPOINT );

        return schema;
    }
}
