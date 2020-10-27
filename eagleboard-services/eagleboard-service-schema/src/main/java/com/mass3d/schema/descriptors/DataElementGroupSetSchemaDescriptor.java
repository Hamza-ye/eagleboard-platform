package com.mass3d.schema.descriptors;

import com.google.common.collect.Lists;
import com.mass3d.dataelement.DataElementGroupSet;
import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaDescriptor;
import com.mass3d.security.Authority;
import com.mass3d.security.AuthorityType;

public class DataElementGroupSetSchemaDescriptor implements SchemaDescriptor
{
    public static final String SINGULAR = "dataElementGroupSet";

    public static final String PLURAL = "dataElementGroupSets";

    public static final String API_ENDPOINT = "/" + PLURAL;

    @Override
    public Schema getSchema()
    {
        Schema schema = new Schema( DataElementGroupSet.class, SINGULAR, PLURAL );
        schema.setRelativeApiEndpoint( API_ENDPOINT );
        schema.setOrder( 1220 );

        schema.getAuthorities().add( new Authority( AuthorityType.CREATE_PUBLIC, Lists.newArrayList( "F_DATAELEMENTGROUPSET_PUBLIC_ADD" ) ) );
        schema.getAuthorities().add( new Authority( AuthorityType.CREATE_PRIVATE, Lists.newArrayList( "F_DATAELEMENTGROUPSET_PRIVATE_ADD" ) ) );
        schema.getAuthorities().add( new Authority( AuthorityType.DELETE, Lists.newArrayList( "F_DATAELEMENTGROUPSET_DELETE" ) ) );

        return schema;
    }
}
