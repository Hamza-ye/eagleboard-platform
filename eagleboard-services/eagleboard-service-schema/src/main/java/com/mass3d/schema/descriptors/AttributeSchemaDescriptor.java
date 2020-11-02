package com.mass3d.schema.descriptors;

import com.google.common.collect.Lists;
import com.mass3d.attribute.Attribute;
import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaDescriptor;
import com.mass3d.security.Authority;
import com.mass3d.security.AuthorityType;

public class AttributeSchemaDescriptor implements SchemaDescriptor
{
    public static final String SINGULAR = "attribute";

    public static final String PLURAL = "attributes";

    public static final String API_ENDPOINT = "/" + PLURAL;

    @Override
    public Schema getSchema()
    {
        Schema schema = new Schema( Attribute.class, SINGULAR, PLURAL );
        schema.setRelativeApiEndpoint( API_ENDPOINT );
        schema.setOrder( 100 );

        schema.getAuthorities().add( new Authority( AuthorityType.CREATE_PUBLIC, Lists.newArrayList( "F_ATTRIBUTE_PUBLIC_ADD" ) ) );
        schema.getAuthorities().add( new Authority( AuthorityType.CREATE_PRIVATE, Lists.newArrayList( "F_ATTRIBUTE_PRIVATE_ADD" ) ) );
        schema.getAuthorities().add( new Authority( AuthorityType.DELETE, Lists.newArrayList( "F_ATTRIBUTE_DELETE" ) ) );

        return schema;
    }
}
