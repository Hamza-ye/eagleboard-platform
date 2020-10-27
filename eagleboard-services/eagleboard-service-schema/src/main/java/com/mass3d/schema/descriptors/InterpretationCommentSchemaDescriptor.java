package com.mass3d.schema.descriptors;

import com.mass3d.interpretation.InterpretationComment;
import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaDescriptor;

public class InterpretationCommentSchemaDescriptor implements SchemaDescriptor
{
    public static final String SINGULAR = "interpretationComment";

    public static final String PLURAL = "interpretationComments";

    public static final String API_ENDPOINT = "/" + PLURAL;

    @Override
    public Schema getSchema()
    {
        return new Schema( InterpretationComment.class, SINGULAR, PLURAL );
    }
}
