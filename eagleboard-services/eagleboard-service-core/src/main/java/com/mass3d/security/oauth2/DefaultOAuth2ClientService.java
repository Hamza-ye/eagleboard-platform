package com.mass3d.security.oauth2;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service( "oAuth2ClientService" )
public class DefaultOAuth2ClientService
    implements OAuth2ClientService
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private final OAuth2ClientStore oAuth2ClientStore;

    public DefaultOAuth2ClientService( OAuth2ClientStore oAuth2ClientStore )
    {
        checkNotNull( oAuth2ClientStore );

        this.oAuth2ClientStore = oAuth2ClientStore;
    }

    // -------------------------------------------------------------------------
    // OAuth2ClientService
    // -------------------------------------------------------------------------

    @Override
    @Transactional
    public void saveOAuth2Client( OAuth2Client oAuth2Client )
    {
        oAuth2ClientStore.save( oAuth2Client );
    }

    @Override
    @Transactional
    public void updateOAuth2Client( OAuth2Client oAuth2Client )
    {
        oAuth2ClientStore.update( oAuth2Client );
    }

    @Override
    @Transactional
    public void deleteOAuth2Client( OAuth2Client oAuth2Client )
    {
        oAuth2ClientStore.delete( oAuth2Client );
    }

    @Override
    @Transactional(readOnly = true)
    public OAuth2Client getOAuth2Client( int id )
    {
        return oAuth2ClientStore.get( id );
    }

    @Override
    @Transactional(readOnly = true)
    public OAuth2Client getOAuth2Client( String uid )
    {
        return oAuth2ClientStore.getByUid( uid );
    }

    @Override
    @Transactional(readOnly = true)
    public OAuth2Client getOAuth2ClientByClientId( String cid )
    {
        return oAuth2ClientStore.getByClientId( cid );
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<OAuth2Client> getOAuth2Clients()
    {
        return oAuth2ClientStore.getAll();
    }
}
