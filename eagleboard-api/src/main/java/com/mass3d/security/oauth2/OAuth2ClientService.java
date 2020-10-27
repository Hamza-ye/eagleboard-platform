package com.mass3d.security.oauth2;

import java.util.Collection;

public interface OAuth2ClientService
{
    void saveOAuth2Client(OAuth2Client oAuth2Client);

    void updateOAuth2Client(OAuth2Client oAuth2Client);

    void deleteOAuth2Client(OAuth2Client oAuth2Client);

    OAuth2Client getOAuth2Client(int id);

    OAuth2Client getOAuth2Client(String uid);

    OAuth2Client getOAuth2ClientByClientId(String cid);

    Collection<OAuth2Client> getOAuth2Clients();
}
