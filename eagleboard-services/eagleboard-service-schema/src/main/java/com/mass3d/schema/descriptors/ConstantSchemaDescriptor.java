package com.mass3d.schema.descriptors;

import com.google.common.collect.Lists;
import com.mass3d.constant.Constant;
import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaDescriptor;
import com.mass3d.security.Authority;
import com.mass3d.security.AuthorityType;

public class ConstantSchemaDescriptor implements SchemaDescriptor
{
    public static final String SINGULAR = "constant";

    public static final String PLURAL = "constants";

    public static final String API_ENDPOINT = "/" + PLURAL;

    @Override
    public Schema getSchema()
    {
        Schema schema = new Schema( Constant.class, SINGULAR, PLURAL );
        schema.setRelativeApiEndpoint( API_ENDPOINT );
        schema.setOrder( 1030 );

        schema.getAuthorities().add( new Authority( AuthorityType.CREATE, Lists.newArrayList( "F_CONSTANT_ADD" ) ) );
        schema.getAuthorities().add( new Authority( AuthorityType.DELETE, Lists.newArrayList( "F_CONSTANT_DELETE" ) ) );

        return schema;
    }
}
