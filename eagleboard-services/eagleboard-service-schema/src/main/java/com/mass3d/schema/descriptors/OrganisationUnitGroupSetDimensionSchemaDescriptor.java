package com.mass3d.schema.descriptors;

import com.mass3d.organisationunit.OrganisationUnitGroupSetDimension;
import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaDescriptor;

public class OrganisationUnitGroupSetDimensionSchemaDescriptor implements SchemaDescriptor
{
    public static final String SINGULAR = "organisationUnitGroupSetDimension";

    public static final String PLURAL = "organisationUnitGroupSetDimensions";

    public static final String API_ENDPOINT = "/" + PLURAL;

    @Override
    public Schema getSchema()
    {
        return new Schema( OrganisationUnitGroupSetDimension.class, SINGULAR, PLURAL );
    }
}
