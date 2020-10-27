package com.mass3d.schema.descriptors;

import com.google.common.collect.Lists;
import com.mass3d.indicator.IndicatorType;
import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaDescriptor;
import com.mass3d.security.Authority;
import com.mass3d.security.AuthorityType;

public class IndicatorTypeSchemaDescriptor implements SchemaDescriptor
{
    public static final String SINGULAR = "indicatorType";

    public static final String PLURAL = "indicatorTypes";

    public static final String API_ENDPOINT = "/" + PLURAL;

    @Override
    public Schema getSchema()
    {
        Schema schema = new Schema( IndicatorType.class, SINGULAR, PLURAL );
        schema.setRelativeApiEndpoint( API_ENDPOINT );
        schema.setOrder( 1240 );

        schema.getAuthorities().add( new Authority( AuthorityType.CREATE, Lists.newArrayList( "F_INDICATORTYPE_ADD" ) ) );
        schema.getAuthorities().add( new Authority( AuthorityType.DELETE, Lists.newArrayList( "F_INDICATORTYPE_DELETE" ) ) );

        return schema;
    }
}
