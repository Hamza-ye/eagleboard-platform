package com.mass3d.schema.descriptors;

import com.google.common.collect.Lists;
import com.mass3d.program.ProgramIndicator;
import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaDescriptor;
import com.mass3d.security.Authority;
import com.mass3d.security.AuthorityType;

public class ProgramIndicatorSchemaDescriptor implements SchemaDescriptor
{
    public static final String SINGULAR = "programIndicator";

    public static final String PLURAL = "programIndicators";

    public static final String API_ENDPOINT = "/" + PLURAL;

    @Override
    public Schema getSchema()
    {
        Schema schema = new Schema( ProgramIndicator.class, SINGULAR, PLURAL );
        schema.setRelativeApiEndpoint( API_ENDPOINT );
        schema.setOrder( 1560 );

        schema.getAuthorities().add( new Authority( AuthorityType.CREATE_PUBLIC, Lists.newArrayList( "F_PROGRAM_INDICATOR_PUBLIC_ADD" ) ) );
        schema.getAuthorities().add( new Authority( AuthorityType.CREATE_PRIVATE, Lists.newArrayList( "F_PROGRAM_INDICATOR_PRIVATE_ADD" ) ) );
        schema.getAuthorities().add( new Authority( AuthorityType.DELETE, Lists.newArrayList( "F_PROGRAM_INDICATOR_DELETE" ) ) );

        return schema;
    }
}
