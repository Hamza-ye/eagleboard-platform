package com.mass3d.schema.descriptors;

import com.mass3d.category.CategoryOptionGroupSetDimension;
import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaDescriptor;

public class CategoryOptionGroupSetDimensionSchemaDescriptor implements SchemaDescriptor
{
    public static final String SINGULAR = "categoryOptionGroupSetDimension";

    public static final String PLURAL = "categoryOptionGroupSetDimensions";

    public static final String API_ENDPOINT = "/" + PLURAL;

    @Override
    public Schema getSchema()
    {
        return new Schema( CategoryOptionGroupSetDimension.class, SINGULAR, PLURAL );
    }
}
