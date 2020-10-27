package com.mass3d.schema.descriptors;

import com.mass3d.message.MessageConversation;
import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaDescriptor;

public class MessageConversationSchemaDescriptor implements SchemaDescriptor
{
    public static final String SINGULAR = "messageConversation";

    public static final String PLURAL = "messageConversations";

    public static final String API_ENDPOINT = "/" + PLURAL;

    @Override
    public Schema getSchema()
    {
        Schema schema = new Schema( MessageConversation.class, SINGULAR, PLURAL );
        schema.setRelativeApiEndpoint( API_ENDPOINT );

        return schema;
    }
}
