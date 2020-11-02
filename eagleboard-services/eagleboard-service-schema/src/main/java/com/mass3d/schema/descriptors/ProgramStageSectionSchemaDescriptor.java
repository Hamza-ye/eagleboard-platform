package com.mass3d.schema.descriptors;

import com.mass3d.program.ProgramStageSection;
import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaDescriptor;

public class ProgramStageSectionSchemaDescriptor implements SchemaDescriptor
{
    public static final String SINGULAR = "programStageSection";

    public static final String PLURAL = "programStageSections";

    public static final String API_ENDPOINT = "/" + PLURAL;

    @Override
    public Schema getSchema()
    {
        Schema schema = new Schema( ProgramStageSection.class, SINGULAR, PLURAL );
        schema.setRelativeApiEndpoint( API_ENDPOINT );
        schema.setOrder( 1508 );

        return schema;
    }
}
