package com.mass3d.schema.descriptors;

import com.google.common.collect.Lists;
import com.mass3d.option.OptionGroup;
import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaDescriptor;
import com.mass3d.security.Authority;
import com.mass3d.security.AuthorityType;

public class OptionGroupSchemaDescriptor implements SchemaDescriptor
{
    public static final String SINGULAR = "optionGroup";

    public static final String PLURAL = "optionGroups";

    public static final String API_ENDPOINT = "/" + PLURAL;

    @Override
    public Schema getSchema()
    {
        Schema schema = new Schema( OptionGroup.class, SINGULAR, PLURAL );
        schema.setRelativeApiEndpoint( API_ENDPOINT );
        schema.setOrder( 1051 );

        schema.getAuthorities().add( new Authority( AuthorityType.CREATE_PUBLIC, Lists.newArrayList( "F_OPTIONGROUP_PUBLIC_ADD" ) ) );
        schema.getAuthorities().add( new Authority( AuthorityType.CREATE_PRIVATE, Lists.newArrayList( "F_OPTIONGROUP_PRIVATE_ADD" ) ) );
        schema.getAuthorities().add( new Authority( AuthorityType.DELETE, Lists.newArrayList( "F_OPTIONGROUP_DELETE" ) ) );

        return schema;
    }
}