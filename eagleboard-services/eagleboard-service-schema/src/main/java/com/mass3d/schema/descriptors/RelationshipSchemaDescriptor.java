package com.mass3d.schema.descriptors;

import com.mass3d.relationship.Relationship;
import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaDescriptor;

public class RelationshipSchemaDescriptor
    implements SchemaDescriptor
{
    public static final String SINGULAR = "relationship";

    public static final String PLURAL = "relationships";

    public static final String API_ENDPOINT = "/" + PLURAL;

    @Override
    public Schema getSchema()
    {
        Schema schema = new Schema( Relationship.class, SINGULAR, PLURAL );
        schema.setRelativeApiEndpoint( API_ENDPOINT );

        return schema;
    }


}
