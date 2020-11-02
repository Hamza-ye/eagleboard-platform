package com.mass3d.schema.descriptors;

import com.mass3d.programstagefilter.ProgramStageInstanceFilter;
import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaDescriptor;

public class ProgramStageInstanceFilterSchemaDescriptor implements SchemaDescriptor
{

    public static final String SINGULAR = "eventFilter";

    public static final String PLURAL = "eventFilters";

    public static final String API_ENDPOINT = "/" + PLURAL;

    @Override
    public Schema getSchema()
    {
        Schema schema = new Schema( ProgramStageInstanceFilter.class, SINGULAR, PLURAL );
        schema.setRelativeApiEndpoint( API_ENDPOINT );
        return schema;
    }

}
