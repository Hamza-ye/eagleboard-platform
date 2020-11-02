package com.mass3d.schema.descriptors;

import com.google.common.collect.Lists;
import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaDescriptor;
import com.mass3d.security.Authority;
import com.mass3d.security.AuthorityType;
import com.mass3d.security.oauth2.OAuth2Client;

public class OAuth2ClientSchemaDescriptor implements SchemaDescriptor
{
    public static final String SINGULAR = "oAuth2Client";

    public static final String PLURAL = "oAuth2Clients";

    public static final String API_ENDPOINT = "/" + PLURAL;

    @Override
    public Schema getSchema()
    {
        Schema schema = new Schema( OAuth2Client.class, SINGULAR, PLURAL );
        schema.setRelativeApiEndpoint( API_ENDPOINT );
        schema.setOrder( 1030 );

        schema.getAuthorities().add( new Authority( AuthorityType.READ, Lists.newArrayList( "F_OAUTH2_CLIENT_MANAGE" ) ) );
        schema.getAuthorities().add( new Authority( AuthorityType.CREATE, Lists.newArrayList( "F_OAUTH2_CLIENT_MANAGE" ) ) );
        schema.getAuthorities().add( new Authority( AuthorityType.DELETE, Lists.newArrayList( "F_OAUTH2_CLIENT_MANAGE" ) ) );

        return schema;
    }
}
