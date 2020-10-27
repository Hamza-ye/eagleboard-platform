package com.mass3d.schema.descriptors;

import com.google.common.collect.Lists;
import com.mass3d.metadata.version.MetadataVersion;
import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaDescriptor;
import com.mass3d.security.Authority;
import com.mass3d.security.AuthorityType;

public class MetadataVersionSchemaDescriptor
    implements SchemaDescriptor
{
    public static final String SINGULAR = "metadataVersion";

    public static final String PLURAL = "metadataVersions";

    public static final String API_ENDPOINT = "/" + "metadata/version";

    @Override
    public Schema getSchema()
    {
        Schema schema = new Schema( MetadataVersion.class, SINGULAR, PLURAL );
        schema.setRelativeApiEndpoint( API_ENDPOINT );

        schema.getAuthorities().add( new Authority( AuthorityType.CREATE_PUBLIC, Lists
            .newArrayList( "ALL", "F_METADATA_MANAGE" ) ) );

        return schema;
    }
}