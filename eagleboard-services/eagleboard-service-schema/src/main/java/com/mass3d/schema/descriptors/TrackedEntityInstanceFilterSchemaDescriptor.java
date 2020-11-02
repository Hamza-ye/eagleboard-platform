package com.mass3d.schema.descriptors;

import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaDescriptor;
import com.mass3d.trackedentityfilter.TrackedEntityInstanceFilter;

public class TrackedEntityInstanceFilterSchemaDescriptor implements SchemaDescriptor
{

    public static final String SINGULAR = "trackedEntityInstanceFilter";

    public static final String PLURAL = "trackedEntityInstanceFilters";

    public static final String API_ENDPOINT = "/" + PLURAL;

    @Override
    public Schema getSchema()
    {
        return new Schema( TrackedEntityInstanceFilter.class, SINGULAR, PLURAL );
    }

}
