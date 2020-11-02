package com.mass3d.schema.descriptors;

import com.google.common.collect.Lists;
import com.mass3d.organisationunit.OrganisationUnit;
import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaDescriptor;
import com.mass3d.security.Authority;
import com.mass3d.security.AuthorityType;

public class OrganisationUnitSchemaDescriptor implements SchemaDescriptor
{
    public static final String SINGULAR = "organisationUnit";

    public static final String PLURAL = "organisationUnits";

    public static final String API_ENDPOINT = "/" + PLURAL;

    @Override
    public Schema getSchema()
    {
        Schema schema = new Schema( OrganisationUnit.class, SINGULAR, PLURAL );
        schema.setRelativeApiEndpoint( API_ENDPOINT );
        schema.setOrder( 1100 );

        schema.getAuthorities().add( new Authority( AuthorityType.CREATE, Lists.newArrayList( "F_ORGANISATIONUNIT_ADD" ) ) );
        schema.getAuthorities().add( new Authority( AuthorityType.DELETE, Lists.newArrayList( "F_ORGANISATIONUNIT_DELETE" ) ) );

        return schema;
    }
}
