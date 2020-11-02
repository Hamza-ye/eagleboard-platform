package com.mass3d.schema.descriptors;

import com.google.common.collect.Lists;
import com.mass3d.program.ProgramTrackedEntityAttributeGroup;
import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaDescriptor;
import com.mass3d.security.Authority;
import com.mass3d.security.AuthorityType;

public class ProgramTrackedEntityAttributeGroupSchemaDescriptor implements SchemaDescriptor
{
    public static final String SINGULAR = "programTrackedEntityAttributeGroup";

    public static final String PLURAL = "programTrackedEntityAttributeGroups";

    public static final String API_ENDPOINT = "/" + PLURAL;

    @Override
    public Schema getSchema()
    {
        Schema schema = new Schema( ProgramTrackedEntityAttributeGroup.class, SINGULAR, PLURAL );
        schema.setRelativeApiEndpoint( API_ENDPOINT );
        schema.setOrder( 1500 );

        schema.getAuthorities().add( new Authority( AuthorityType.CREATE, Lists.newArrayList( "F_PROGRAM_TRACKED_ENTITY_ATTRIBUTE_GROUP_ADD" ) ) );
        schema.getAuthorities().add( new Authority( AuthorityType.DELETE, Lists.newArrayList( "F_PROGRAM_TRACKED_ENTITY_ATTRIBUTE_GROUP_DELETE" ) ) );

        return schema;
    }
}
