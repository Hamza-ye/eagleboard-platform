package com.mass3d.schema.descriptors;

import com.google.common.collect.Lists;
import com.mass3d.category.CategoryOptionCombo;
import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaDescriptor;
import com.mass3d.security.Authority;
import com.mass3d.security.AuthorityType;

public class CategoryOptionComboSchemaDescriptor implements SchemaDescriptor
{
    public static final String SINGULAR = "categoryOptionCombo";

    public static final String PLURAL = "categoryOptionCombos";

    public static final String API_ENDPOINT = "/" + PLURAL;

    @Override
    public Schema getSchema()
    {
        Schema schema = new Schema( CategoryOptionCombo.class, SINGULAR, PLURAL );
        schema.setRelativeApiEndpoint( API_ENDPOINT );
        schema.setOrder( 1190 );

        schema.getAuthorities().add( new Authority( AuthorityType.CREATE,
            Lists.newArrayList( "F_CATEGORY_COMBO_PUBLIC_ADD", "F_CATEGORY_COMBO_PRIVATE_ADD" ) ) );
        schema.getAuthorities().add( new Authority( AuthorityType.DELETE, Lists.newArrayList( "F_CATEGORY_COMBO_DELETE" ) ) );

        return schema;
    }
}
