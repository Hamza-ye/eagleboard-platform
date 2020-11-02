package com.mass3d.schema.descriptors;

import com.mass3d.program.ProgramDataElementDimensionItem;
import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaDescriptor;

public class ProgramDataElementDimensionItemSchemaDescriptor implements SchemaDescriptor
{
    public static final String SINGULAR = "programDataElement";

    public static final String PLURAL = "programDataElements";

    public static final String API_ENDPOINT = "/" + PLURAL;

    @Override
    public Schema getSchema()
    {
        Schema schema = new Schema( ProgramDataElementDimensionItem.class, SINGULAR, PLURAL );
        schema.setRelativeApiEndpoint( API_ENDPOINT );

        return schema;
    }
}
