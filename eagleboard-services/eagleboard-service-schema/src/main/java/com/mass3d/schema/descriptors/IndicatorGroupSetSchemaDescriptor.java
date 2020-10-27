package com.mass3d.schema.descriptors;

import com.google.common.collect.Lists;
import com.mass3d.indicator.IndicatorGroupSet;
import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaDescriptor;
import com.mass3d.security.Authority;
import com.mass3d.security.AuthorityType;

public class IndicatorGroupSetSchemaDescriptor implements SchemaDescriptor
{
    public static final String SINGULAR = "indicatorGroupSet";

    public static final String PLURAL = "indicatorGroupSets";

    public static final String API_ENDPOINT = "/" + PLURAL;

    @Override
    public Schema getSchema()
    {
        Schema schema = new Schema( IndicatorGroupSet.class, SINGULAR, PLURAL );
        schema.setRelativeApiEndpoint( API_ENDPOINT );
        schema.setOrder( 1270 );

        schema.getAuthorities().add( new Authority( AuthorityType.CREATE_PUBLIC, Lists.newArrayList( "F_INDICATORGROUPSET_PUBLIC_ADD" ) ) );
        schema.getAuthorities().add( new Authority( AuthorityType.CREATE_PRIVATE, Lists.newArrayList( "F_INDICATORGROUPSET_PRIVATE_ADD" ) ) );
        schema.getAuthorities().add( new Authority( AuthorityType.DELETE, Lists.newArrayList( "F_INDICATORGROUPSET_DELETE" ) ) );

        return schema;
    }
}
