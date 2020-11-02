package com.mass3d.schema.descriptors;

import com.google.common.collect.Lists;
import com.mass3d.relationship.RelationshipType;
import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaDescriptor;
import com.mass3d.security.Authority;
import com.mass3d.security.AuthorityType;

public class RelationshipTypeSchemaDescriptor implements SchemaDescriptor
{
    public static final String SINGULAR = "relationshipType";

    public static final String PLURAL = "relationshipTypes";

    public static final String API_ENDPOINT = "/" + PLURAL;

    @Override
    public Schema getSchema()
    {
        Schema schema = new Schema( RelationshipType.class, SINGULAR, PLURAL );
        schema.setRelativeApiEndpoint( API_ENDPOINT );
        schema.setOrder( 1600 );

        schema.setDataShareable( true );

        schema.getAuthorities().add( new Authority( AuthorityType.CREATE_PUBLIC, Lists.newArrayList( "F_RELATIONSHIPTYPE_PUBLIC_ADD" ) ) );
        schema.getAuthorities().add( new Authority( AuthorityType.CREATE_PRIVATE, Lists.newArrayList( "F_RELATIONSHIPTYPE_PRIVATE_ADD" ) ) );

        schema.getAuthorities().add( new Authority( AuthorityType.DELETE, Lists.newArrayList( "F_RELATIONSHIPTYPE_DELETE" ) ) );

        return schema;
    }
}
