package com.mass3d.schema.descriptors;

import com.google.common.collect.Lists;
import com.mass3d.program.ProgramIndicatorGroup;
import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaDescriptor;
import com.mass3d.security.Authority;
import com.mass3d.security.AuthorityType;

public class ProgramIndicatorGroupSchemaDescriptor implements SchemaDescriptor
{
    public static final String SINGULAR = "programIndicatorGroup";

    public static final String PLURAL = "programIndicatorGroups";

    public static final String API_ENDPOINT = "/" + PLURAL;

    @Override
    public Schema getSchema()
    {
        Schema schema = new Schema( ProgramIndicatorGroup.class, SINGULAR, PLURAL );
        schema.setRelativeApiEndpoint( API_ENDPOINT );
        schema.setOrder( 1600 );

        schema.getAuthorities().add( new Authority( AuthorityType.CREATE,
            Lists.newArrayList( "F_PROGRAM_INDICATOR_GROUP_PUBLIC_ADD", "F_PROGRAM_INDICATOR_GROUP_PRIVATE_ADD" ) ) );
        schema.getAuthorities().add( new Authority( AuthorityType.DELETE, Lists.newArrayList( "F_PROGRAM_INDICATOR_GROUP_DELETE" ) ) );

        return schema;
    }
}
