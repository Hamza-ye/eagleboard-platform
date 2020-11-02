package com.mass3d.schema.descriptors;

import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaDescriptor;
import com.mass3d.trackedentity.TrackedEntityTypeAttribute;

public class TrackedEntityTypeAttributeSchemaDescriptor implements SchemaDescriptor
{
    public static final String SINGULAR = "trackedEntityTypeAttribute";

    public static final String PLURAL = "trackedEntityTypeAttributes";

    public static final String API_ENDPOINT = "/" + PLURAL;

    @Override
    public Schema getSchema()
    {        
        Schema schema = new Schema( TrackedEntityTypeAttribute.class, SINGULAR, PLURAL );
        schema.setOrder( 1500 );
        return schema;
    }
}