package com.mass3d.schema.descriptors;

import com.google.common.collect.Lists;
import com.mass3d.program.notification.ProgramNotificationTemplate;
import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaDescriptor;
import com.mass3d.security.Authority;
import com.mass3d.security.AuthorityType;

public class ProgramNotificationTemplateSchemaDescriptor
    implements SchemaDescriptor
{
    public static final String SINGULAR = "programNotificationTemplate";

    public static final String PLURAL = "programNotificationTemplates";

    public static final String API_ENDPOINT = "/" + PLURAL;

    @Override
    public Schema getSchema()
    {
        Schema schema = new Schema( ProgramNotificationTemplate.class, SINGULAR, PLURAL );
        schema.setRelativeApiEndpoint( API_ENDPOINT );
        schema.setOrder( 1508 );

        schema.getAuthorities().add( new Authority( AuthorityType.CREATE, Lists.newArrayList( "F_PROGRAM_PUBLIC_ADD",
            "F_PROGRAM_PRIVATE_ADD" ) ) );
        schema.getAuthorities().add( new Authority( AuthorityType.DELETE, Lists.newArrayList( "F_PROGRAM_DELETE" ) ) );

        return schema;
    }
}
