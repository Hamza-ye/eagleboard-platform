package com.mass3d.schema.descriptors;

import com.google.common.collect.Lists;
import com.mass3d.organisationunit.OrganisationUnitGroup;
import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaDescriptor;
import com.mass3d.security.Authority;
import com.mass3d.security.AuthorityType;

public class OrganisationUnitGroupSchemaDescriptor implements SchemaDescriptor
{
    public static final String SINGULAR = "organisationUnitGroup";

    public static final String PLURAL = "organisationUnitGroups";

    public static final String API_ENDPOINT = "/" + PLURAL;

    @Override
    public Schema getSchema()
    {
        Schema schema = new Schema( OrganisationUnitGroup.class, SINGULAR, PLURAL );
        schema.setRelativeApiEndpoint( API_ENDPOINT );
        schema.setOrder( 1120 );

        schema.getAuthorities().add( new Authority( AuthorityType.CREATE_PUBLIC, Lists.newArrayList( "F_ORGUNITGROUP_PUBLIC_ADD" ) ) );
        schema.getAuthorities().add( new Authority( AuthorityType.CREATE_PRIVATE, Lists.newArrayList( "F_ORGUNITGROUP_PRIVATE_ADD" ) ) );
        schema.getAuthorities().add( new Authority( AuthorityType.DELETE, Lists.newArrayList( "F_ORGUNITGROUP_DELETE" ) ) );

        return schema;
    }
}
