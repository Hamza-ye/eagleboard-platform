package com.mass3d.schema.descriptors;

import com.mass3d.dataelement.DataElementOperand;
import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaDescriptor;

public class DataElementOperandSchemaDescriptor implements SchemaDescriptor
{
    public static final String SINGULAR = "dataElementOperand";

    public static final String PLURAL = "dataElementOperands";

    public static final String API_ENDPOINT = "/" + PLURAL;

    @Override
    public Schema getSchema()
    {
        Schema schema = new Schema( DataElementOperand.class, SINGULAR, PLURAL );
        schema.setRelativeApiEndpoint( API_ENDPOINT );

        return schema;
    }
}
