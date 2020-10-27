package com.mass3d.security.oauth2;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.Sets;
import java.util.HashSet;
import java.util.Set;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;

@Service( "defaultClientDetailsService" )
public class DefaultClientDetailsService implements ClientDetailsService
{
    private static final Set<String> SCOPES = Sets.newHashSet( "ALL" );

    private final OAuth2ClientService oAuth2ClientService;

    public DefaultClientDetailsService( OAuth2ClientService oAuth2ClientService )
    {
        checkNotNull( oAuth2ClientService );

        this.oAuth2ClientService = oAuth2ClientService;
    }

    @Override
    public ClientDetails loadClientByClientId( String clientId ) throws ClientRegistrationException
    {
        ClientDetails clientDetails = clientDetails( oAuth2ClientService.getOAuth2ClientByClientId( clientId ) );

        if ( clientDetails == null )
        {
            throw new ClientRegistrationException( "Invalid client_id" );
        }

        return clientDetails;
    }

    private ClientDetails clientDetails( OAuth2Client client )
    {
        if ( client == null )
        {
            return null;
        }

        BaseClientDetails clientDetails = new BaseClientDetails();
        clientDetails.setClientId( client.getCid() );
        clientDetails.setClientSecret( client.getSecret() );
        clientDetails.setAuthorizedGrantTypes( new HashSet<>( client.getGrantTypes() ) );
        clientDetails.setScope( SCOPES );
        clientDetails.setRegisteredRedirectUri( new HashSet<>( client.getRedirectUris() ) );

        return clientDetails;
    }
}
