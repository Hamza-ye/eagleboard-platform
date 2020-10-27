package com.mass3d.schema.descriptors;

import com.google.common.collect.Lists;
import com.mass3d.scheduling.JobConfiguration;
import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaDescriptor;
import com.mass3d.security.Authority;
import com.mass3d.security.AuthorityType;

public class JobConfigurationSchemaDescriptor implements SchemaDescriptor
{
    public static final String SINGULAR = "jobConfiguration";

    public static final String PLURAL = "jobConfigurations";

    public static final String API_ENDPOINT = "/" + PLURAL;

    @Override
    public Schema getSchema()
    {
        Schema schema = new Schema( JobConfiguration.class, SINGULAR, PLURAL );
        schema.setRelativeApiEndpoint( API_ENDPOINT );

        schema.setOrder( 1040 );

        schema.getAuthorities().add( new Authority( AuthorityType.CREATE, Lists.newArrayList( "F_SCHEDULING_ADMIN" ) ) );
        schema.getAuthorities().add( new Authority( AuthorityType.DELETE, Lists.newArrayList( "F_SCHEDULING_ADMIN" ) ) );

        return schema;
    }
}
