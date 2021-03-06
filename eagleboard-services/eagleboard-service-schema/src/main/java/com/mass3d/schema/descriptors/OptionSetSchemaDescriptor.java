package com.mass3d.schema.descriptors;

import com.google.common.collect.Lists;
import com.mass3d.option.OptionSet;
import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaDescriptor;
import com.mass3d.security.Authority;
import com.mass3d.security.AuthorityType;

public class OptionSetSchemaDescriptor implements SchemaDescriptor
{
    public static final String SINGULAR = "optionSet";

    public static final String PLURAL = "optionSets";

    public static final String API_ENDPOINT = "/" + PLURAL;

    @Override
    public Schema getSchema()
    {
        Schema schema = new Schema( OptionSet.class, SINGULAR, PLURAL );
        schema.setRelativeApiEndpoint( API_ENDPOINT );
        schema.setOrder( 1050 );

        schema.getAuthorities().add( new Authority( AuthorityType.CREATE_PUBLIC, Lists.newArrayList( "F_OPTIONSET_PUBLIC_ADD" ) ) );
        schema.getAuthorities().add( new Authority( AuthorityType.CREATE_PRIVATE, Lists.newArrayList( "F_OPTIONSET_PRIVATE_ADD" ) ) );
        schema.getAuthorities().add( new Authority( AuthorityType.DELETE, Lists.newArrayList( "F_OPTIONSET_DELETE" ) ) );

        return schema;
    }
}
