package com.mass3d.schema.descriptors;

import com.google.common.collect.Lists;
import com.mass3d.category.CategoryCombo;
import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaDescriptor;
import com.mass3d.security.Authority;
import com.mass3d.security.AuthorityType;

public class CategoryComboSchemaDescriptor implements SchemaDescriptor
{
    public static final String SINGULAR = "categoryCombo";

    public static final String PLURAL = "categoryCombos";

    public static final String API_ENDPOINT = "/" + PLURAL;

    @Override
    public Schema getSchema()
    {
        Schema schema = new Schema( CategoryCombo.class, SINGULAR, PLURAL );
        schema.setRelativeApiEndpoint( API_ENDPOINT );
        schema.setOrder( 1180 );

        schema.getAuthorities().add( new Authority( AuthorityType.CREATE_PUBLIC, Lists.newArrayList( "F_CATEGORY_COMBO_PUBLIC_ADD" ) ) );
        schema.getAuthorities().add( new Authority( AuthorityType.CREATE_PRIVATE, Lists.newArrayList( "F_CATEGORY_COMBO_PRIVATE_ADD" ) ) );
        schema.getAuthorities().add( new Authority( AuthorityType.DELETE, Lists.newArrayList( "F_CATEGORY_COMBO_DELETE" ) ) );

        return schema;
    }
}
