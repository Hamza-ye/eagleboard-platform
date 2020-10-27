package com.mass3d.schema.descriptors;

import com.mass3d.fileresource.ExternalFileResource;
import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaDescriptor;

public class ExternalFileResourceSchemaDescriptor
    implements SchemaDescriptor
{
    public static final String SINGULAR = "externalFileResource";

    public static final String PLURAL = "externalFileResources";

    public static final String API_ENDPOINT = "/" + PLURAL;

    @Override
    public Schema getSchema()
    {
        Schema schema = new Schema( ExternalFileResource.class, SINGULAR, PLURAL );
        schema.setRelativeApiEndpoint( API_ENDPOINT );
        schema.setOrder( 1000 );

        return schema;
    }
}
