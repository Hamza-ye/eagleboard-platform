package com.mass3d.schema.descriptors;

import com.mass3d.program.ProgramTrackedEntityAttributeDimensionItem;
import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaDescriptor;

public class ProgramTrackedEntityAttributeDimensionItemSchemaDescriptor implements SchemaDescriptor
{
    public static final String SINGULAR = "ProgramTrackedEntityAttributeDimensionItem";

    public static final String PLURAL = "ProgramTrackedEntityAttributeDimensionItems";

    @Override
    public Schema getSchema()
    {
        return new Schema( ProgramTrackedEntityAttributeDimensionItem.class, SINGULAR, PLURAL );
    }
}
