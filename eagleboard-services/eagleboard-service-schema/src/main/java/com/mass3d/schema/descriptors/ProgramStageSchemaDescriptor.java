package com.mass3d.schema.descriptors;

import com.google.common.collect.Lists;
import com.mass3d.program.ProgramStage;
import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaDescriptor;
import com.mass3d.security.Authority;
import com.mass3d.security.AuthorityType;

public class ProgramStageSchemaDescriptor implements SchemaDescriptor
{
    public static final String SINGULAR = "programStage";

    public static final String PLURAL = "programStages";

    public static final String API_ENDPOINT = "/" + PLURAL;

    @Override
    public Schema getSchema()
    {
        Schema schema = new Schema( ProgramStage.class, SINGULAR, PLURAL );
        schema.setRelativeApiEndpoint( API_ENDPOINT );
        schema.setOrder( 1509 );
        schema.setDataShareable( true );

        schema.getAuthorities().add( new Authority( AuthorityType.CREATE, Lists.newArrayList( "F_PROGRAMSTAGE_ADD" ) ) );
        schema.getAuthorities().add( new Authority( AuthorityType.DELETE, Lists.newArrayList( "F_PROGRAMSTAGE_DELETE" ) ) );

        return schema;
    }
}
