package com.mass3d.schema.descriptors;

import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaDescriptor;
import com.mass3d.validation.ValidationResult;

public class ValidationResultSchemaDescriptor implements SchemaDescriptor
{
    public static final String SINGULAR = "validationResult";

    public static final String PLURAL = "validationResults";

    public static final String API_ENDPOINT = "/" + PLURAL;

    @Override
    public Schema getSchema()
    {
        Schema schema = new Schema( ValidationResult.class, SINGULAR, PLURAL );
        schema.setRelativeApiEndpoint( API_ENDPOINT );
        schema.setOrder( 2000 );

        return schema;
    }
}