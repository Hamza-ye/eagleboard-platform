package com.mass3d.schema.descriptors;

import com.google.common.collect.Lists;
import com.mass3d.dataelement.DataElementGroup;
import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaDescriptor;
import com.mass3d.security.Authority;
import com.mass3d.security.AuthorityType;

public class DataElementGroupSchemaDescriptor implements SchemaDescriptor
{
    public static final String SINGULAR = "dataElementGroup";

    public static final String PLURAL = "dataElementGroups";

    public static final String API_ENDPOINT = "/" + PLURAL;

    @Override
    public Schema getSchema()
    {
        Schema schema = new Schema( DataElementGroup.class, SINGULAR, PLURAL );
        schema.setRelativeApiEndpoint( API_ENDPOINT );
        schema.setOrder( 1210 );

        schema.getAuthorities().add( new Authority( AuthorityType.CREATE_PUBLIC, Lists.newArrayList( "F_DATAELEMENTGROUP_PUBLIC_ADD" ) ) );
        schema.getAuthorities().add( new Authority( AuthorityType.CREATE_PRIVATE, Lists.newArrayList( "F_DATAELEMENTGROUP_PRIVATE_ADD" ) ) );
        schema.getAuthorities().add( new Authority( AuthorityType.DELETE, Lists.newArrayList( "F_DATAELEMENTGROUP_DELETE" ) ) );

        return schema;
    }
}
