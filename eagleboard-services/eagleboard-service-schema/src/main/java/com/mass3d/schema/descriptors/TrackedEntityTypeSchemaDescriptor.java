package com.mass3d.schema.descriptors;

import com.google.common.collect.Lists;
import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaDescriptor;
import com.mass3d.security.Authority;
import com.mass3d.security.AuthorityType;
import com.mass3d.trackedentity.TrackedEntityType;

public class TrackedEntityTypeSchemaDescriptor implements SchemaDescriptor
{
    public static final String SINGULAR = "trackedEntityType";

    public static final String PLURAL = "trackedEntityTypes";

    public static final String API_ENDPOINT = "/" + PLURAL;

    @Override
    public Schema getSchema()
    {
        Schema schema = new Schema( TrackedEntityType.class, SINGULAR, PLURAL );
        schema.setDataShareable( true );
        schema.setRelativeApiEndpoint( API_ENDPOINT );
        schema.setOrder( 1480 );

        schema.getAuthorities().add( new Authority( AuthorityType.CREATE, Lists.newArrayList( "F_TRACKED_ENTITY_ADD" ) ) );
        schema.getAuthorities().add( new Authority( AuthorityType.UPDATE, Lists.newArrayList( "F_TRACKED_ENTITY_UPDATE" ) ) );
        schema.getAuthorities().add( new Authority( AuthorityType.DELETE, Lists.newArrayList( "F_TRACKED_ENTITY_DELETE" ) ) );

        return schema;
    }
}
