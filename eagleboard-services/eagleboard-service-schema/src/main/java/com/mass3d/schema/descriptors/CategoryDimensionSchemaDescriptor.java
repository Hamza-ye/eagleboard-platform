package com.mass3d.schema.descriptors;

import com.mass3d.category.CategoryDimension;
import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaDescriptor;

public class CategoryDimensionSchemaDescriptor implements SchemaDescriptor
{
    public static final String SINGULAR = "categoryDimension";

    public static final String PLURAL = "categoryDimensions";

    public static final String API_ENDPOINT = "/" + PLURAL;

    @Override
    public Schema getSchema()
    {
        return new Schema( CategoryDimension.class, SINGULAR, PLURAL );
    }
}
