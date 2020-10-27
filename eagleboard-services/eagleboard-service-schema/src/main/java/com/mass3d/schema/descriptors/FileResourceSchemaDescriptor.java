package com.mass3d.schema.descriptors;

import com.mass3d.fileresource.FileResource;
import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaDescriptor;

public class FileResourceSchemaDescriptor
    implements SchemaDescriptor
{
    public static final String SINGULAR = "fileResource";

    public static final String PLURAL = "fileResources";

    public static final String API_ENDPOINT = "/" + PLURAL;

    @Override
    public Schema getSchema()
    {
        Schema schema = new Schema( FileResource.class, SINGULAR, PLURAL );
        schema.setRelativeApiEndpoint( API_ENDPOINT );

        return schema;
    }
}
