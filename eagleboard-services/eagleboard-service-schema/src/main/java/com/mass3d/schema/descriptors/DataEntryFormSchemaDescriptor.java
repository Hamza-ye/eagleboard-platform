package com.mass3d.schema.descriptors;

import com.mass3d.dataentryform.DataEntryForm;
import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaDescriptor;

public class DataEntryFormSchemaDescriptor implements SchemaDescriptor
{
    public static final String SINGULAR = "dataEntryForm";

    public static final String PLURAL = "dataEntryForms";

    public static final String API_ENDPOINT = "/" + PLURAL;

    @Override
    public Schema getSchema()
    {
        Schema schema = new Schema( DataEntryForm.class, SINGULAR, PLURAL );
        schema.setRelativeApiEndpoint( API_ENDPOINT );
        schema.setOrder( 1300 );

        return schema;
    }
}
