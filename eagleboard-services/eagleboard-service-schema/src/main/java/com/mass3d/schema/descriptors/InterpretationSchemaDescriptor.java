package com.mass3d.schema.descriptors;

import com.mass3d.interpretation.Interpretation;
import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaDescriptor;

public class InterpretationSchemaDescriptor implements SchemaDescriptor
{
    public static final String SINGULAR = "interpretation";

    public static final String PLURAL = "interpretations";

    public static final String API_ENDPOINT = "/" + PLURAL;

    @Override
    public Schema getSchema()
    {
        Schema schema = new Schema( Interpretation.class, SINGULAR, PLURAL );
        schema.setRelativeApiEndpoint( API_ENDPOINT );

        return schema;
    }
}
