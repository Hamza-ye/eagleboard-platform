package com.mass3d.security.oauth2;

import com.mass3d.common.IdentifiableObjectStore;

public interface OAuth2ClientStore
    extends IdentifiableObjectStore<OAuth2Client>
{
    String ID = OAuth2ClientStore.class.getName();

    /**
     * Get OAuth2 client by cid.
     *
     * @param cid ClientID
     * @return Matched OAuth2Client or null if not found
     */
    OAuth2Client getByClientId(String cid);
}
