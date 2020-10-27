package com.mass3d.schema.descriptors;

import com.mass3d.common.ReportingRate;
import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaDescriptor;

public class ReportingRateSchemaDescriptor implements SchemaDescriptor
{
    public static final String SINGULAR = "reportingRate";

    public static final String PLURAL = "reportingRates";

    public static final String API_ENDPOINT = "/" + PLURAL;

    @Override
    public Schema getSchema()
    {
        return new Schema( ReportingRate.class, SINGULAR, PLURAL );
    }
}
