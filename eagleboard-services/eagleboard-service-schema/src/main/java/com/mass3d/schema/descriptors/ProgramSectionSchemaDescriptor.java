package com.mass3d.schema.descriptors;

import com.mass3d.program.ProgramSection;
import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaDescriptor;

public class ProgramSectionSchemaDescriptor
    implements SchemaDescriptor
{
    public static final String SINGULAR = "programSection";

    public static final String PLURAL = "programSections";

    public static final String API_ENDPOINT = "/" + PLURAL;

    @Override
    public Schema getSchema()
    {
        Schema schema = new Schema( ProgramSection.class, SINGULAR, PLURAL );
        schema.setRelativeApiEndpoint( API_ENDPOINT );
        schema.setOrder( 1550 );

        return schema;
    }
}
