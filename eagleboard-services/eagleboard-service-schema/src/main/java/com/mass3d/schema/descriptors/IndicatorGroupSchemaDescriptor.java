package com.mass3d.schema.descriptors;

import com.google.common.collect.Lists;
import com.mass3d.indicator.IndicatorGroup;
import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaDescriptor;
import com.mass3d.security.Authority;
import com.mass3d.security.AuthorityType;

public class IndicatorGroupSchemaDescriptor implements SchemaDescriptor
{
    public static final String SINGULAR = "indicatorGroup";

    public static final String PLURAL = "indicatorGroups";

    public static final String API_ENDPOINT = "/" + PLURAL;

    @Override
    public Schema getSchema()
    {
        Schema schema = new Schema( IndicatorGroup.class, SINGULAR, PLURAL );
        schema.setRelativeApiEndpoint( API_ENDPOINT );
        schema.setOrder( 1260 );

        schema.getAuthorities().add( new Authority( AuthorityType.CREATE_PUBLIC, Lists.newArrayList( "F_INDICATORGROUP_PUBLIC_ADD" ) ) );
        schema.getAuthorities().add( new Authority( AuthorityType.CREATE_PRIVATE, Lists.newArrayList( "F_INDICATORGROUP_PRIVATE_ADD" ) ) );
        schema.getAuthorities().add( new Authority( AuthorityType.DELETE, Lists.newArrayList( "F_INDICATORGROUP_DELETE" ) ) );

        return schema;
    }
}
