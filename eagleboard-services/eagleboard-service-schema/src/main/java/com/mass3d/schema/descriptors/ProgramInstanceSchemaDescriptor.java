package com.mass3d.schema.descriptors;

import com.mass3d.program.ProgramInstance;
import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaDescriptor;

public class ProgramInstanceSchemaDescriptor implements SchemaDescriptor
{

    public static final String SINGULAR = "programInstance";

    public static final String PLURAL = "programInstances";

    public static final String API_ENDPOINT = "/" + PLURAL;

    @Override
    public Schema getSchema()
    {
        Schema schema = new Schema( ProgramInstance.class, SINGULAR, PLURAL );
        schema.setRelativeApiEndpoint( API_ENDPOINT );

        return schema;
    }
}
