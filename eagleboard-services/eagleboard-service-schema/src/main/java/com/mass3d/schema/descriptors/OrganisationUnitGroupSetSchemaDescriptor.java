package com.mass3d.schema.descriptors;

import com.google.common.collect.Lists;
import com.mass3d.organisationunit.OrganisationUnitGroupSet;
import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaDescriptor;
import com.mass3d.security.Authority;
import com.mass3d.security.AuthorityType;

public class OrganisationUnitGroupSetSchemaDescriptor implements SchemaDescriptor
{
    public static final String SINGULAR = "organisationUnitGroupSet";

    public static final String PLURAL = "organisationUnitGroupSets";

    public static final String API_ENDPOINT = "/" + PLURAL;

    @Override
    public Schema getSchema()
    {
        Schema schema = new Schema( OrganisationUnitGroupSet.class, SINGULAR, PLURAL );
        schema.setRelativeApiEndpoint( API_ENDPOINT );
        schema.setOrder( 1130 );

        schema.getAuthorities().add( new Authority( AuthorityType.CREATE_PUBLIC, Lists.newArrayList( "F_ORGUNITGROUPSET_PUBLIC_ADD" ) ) );
        schema.getAuthorities().add( new Authority( AuthorityType.CREATE_PRIVATE, Lists.newArrayList( "F_ORGUNITGROUPSET_PRIVATE_ADD" ) ) );
        schema.getAuthorities().add( new Authority( AuthorityType.DELETE, Lists.newArrayList( "F_ORGUNITGROUPSET_DELETE" ) ) );

        return schema;
    }
}
