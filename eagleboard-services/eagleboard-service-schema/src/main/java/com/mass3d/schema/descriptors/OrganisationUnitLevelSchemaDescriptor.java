package com.mass3d.schema.descriptors;

import com.google.common.collect.Lists;
import com.mass3d.organisationunit.OrganisationUnitLevel;
import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaDescriptor;
import com.mass3d.security.Authority;
import com.mass3d.security.AuthorityType;

public class OrganisationUnitLevelSchemaDescriptor implements SchemaDescriptor
{
    public static final String SINGULAR = "organisationUnitLevel";

    public static final String PLURAL = "organisationUnitLevels";

    public static final String API_ENDPOINT = "/" + PLURAL;

    @Override
    public Schema getSchema()
    {
        Schema schema = new Schema( OrganisationUnitLevel.class, SINGULAR, PLURAL );
        schema.setRelativeApiEndpoint( API_ENDPOINT );
        schema.setOrder( 1110 );

        schema.getAuthorities().add( new Authority( AuthorityType.CREATE, Lists.newArrayList( "F_ORGANISATIONUNITLEVEL_UPDATE" ) ) );
        schema.getAuthorities().add( new Authority( AuthorityType.UPDATE, Lists.newArrayList( "F_ORGANISATIONUNITLEVEL_UPDATE" ) ) );
        schema.getAuthorities().add( new Authority( AuthorityType.DELETE, Lists.newArrayList( "F_ORGANISATIONUNITLEVEL_UPDATE" ) ) );

        return schema;
    }
}
