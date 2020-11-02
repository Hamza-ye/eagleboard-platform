package com.mass3d.schema.descriptors;

import com.mass3d.program.ProgramStageDataElement;
import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaDescriptor;

public class ProgramStageDataElementSchemaDescriptor implements SchemaDescriptor
{
    public static final String SINGULAR = "programStageDataElement";

    public static final String PLURAL = "programStageDataElements";

    public static final String API_ENDPOINT = "/" + PLURAL;

    @Override
    public Schema getSchema()
    {
        return new Schema( ProgramStageDataElement.class, SINGULAR, PLURAL );
    }
}
