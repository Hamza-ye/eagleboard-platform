package com.mass3d.schema.descriptors;

import com.mass3d.program.ProgramTrackedEntityAttribute;
import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaDescriptor;

public class ProgramTrackedEntityAttributeSchemaDescriptor implements SchemaDescriptor
{
    public static final String SINGULAR = "programTrackedEntityAttribute";

    public static final String PLURAL = "programTrackedEntityAttributes";

    public static final String API_ENDPOINT = "/" + PLURAL;

    @Override
    public Schema getSchema()
    {
        return new Schema( ProgramTrackedEntityAttribute.class, SINGULAR, PLURAL );
    }
}
