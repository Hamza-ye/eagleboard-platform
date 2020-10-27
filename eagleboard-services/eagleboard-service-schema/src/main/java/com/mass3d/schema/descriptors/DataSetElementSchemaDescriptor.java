package com.mass3d.schema.descriptors;

import com.mass3d.dataset.DataSetElement;
import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaDescriptor;

public class DataSetElementSchemaDescriptor implements SchemaDescriptor
{
    public static final String SINGULAR = "dataSetElement";

    public static final String PLURAL = "dataSetElements";

    public static final String API_ENDPOINT = "/" + PLURAL;

    @Override
    public Schema getSchema()
    {
        return new Schema( DataSetElement.class, SINGULAR, PLURAL );
    }
}
