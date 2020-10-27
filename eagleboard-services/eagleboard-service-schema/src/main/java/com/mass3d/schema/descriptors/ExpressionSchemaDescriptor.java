package com.mass3d.schema.descriptors;

import com.mass3d.expression.Expression;
import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaDescriptor;

public class ExpressionSchemaDescriptor
    implements SchemaDescriptor
{
    public static final String SINGULAR = "expression";

    public static final String PLURAL = "expressions";

    public static final String API_ENDPOINT = "/" + PLURAL;

    @Override
    public Schema getSchema()
    {
        Schema schema = new Schema( Expression.class, SINGULAR, PLURAL );
        schema.setRelativeApiEndpoint( API_ENDPOINT );
        schema.setOrder( 1000 );

        return schema;
    }
}
