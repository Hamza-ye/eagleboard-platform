package com.mass3d.schema.descriptors;

import com.mass3d.program.ProgramStageInstance;
import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaDescriptor;

public class ProgramStageInstanceSchemaDescriptor implements SchemaDescriptor
{

    public static final String SINGULAR = "programStageInstance";

    public static final String PLURAL = "programStageInstances";

    public static final String API_ENDPOINT = "/" + PLURAL;

    @Override
    public Schema getSchema()
    {
        Schema schema = new Schema( ProgramStageInstance.class, SINGULAR, PLURAL );
        schema.setRelativeApiEndpoint( API_ENDPOINT );

        return schema;
    }
}
