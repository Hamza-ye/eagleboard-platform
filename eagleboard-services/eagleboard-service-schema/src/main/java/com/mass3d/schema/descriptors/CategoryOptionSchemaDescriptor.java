package com.mass3d.schema.descriptors;

import com.google.common.collect.Lists;
import com.mass3d.category.CategoryOption;
import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaDescriptor;
import com.mass3d.security.Authority;
import com.mass3d.security.AuthorityType;

public class CategoryOptionSchemaDescriptor implements SchemaDescriptor
{
    public static final String SINGULAR = "categoryOption";

    public static final String PLURAL = "categoryOptions";

    public static final String API_ENDPOINT = "/" + PLURAL;

    @Override
    public Schema getSchema()
    {
        Schema schema = new Schema( CategoryOption.class, SINGULAR, PLURAL );
        schema.setRelativeApiEndpoint( API_ENDPOINT );
        schema.setOrder( 1140 );
        schema.setDataShareable( true );

        schema.getAuthorities().add( new Authority( AuthorityType.CREATE_PUBLIC, Lists.newArrayList( "F_CATEGORY_OPTION_PUBLIC_ADD" ) ) );
        schema.getAuthorities().add( new Authority( AuthorityType.CREATE_PRIVATE, Lists.newArrayList( "F_CATEGORY_OPTION_PRIVATE_ADD" ) ) );
        schema.getAuthorities().add( new Authority( AuthorityType.DELETE, Lists.newArrayList( "F_CATEGORY_OPTION_DELETE" ) ) );

        return schema;
    }
}
