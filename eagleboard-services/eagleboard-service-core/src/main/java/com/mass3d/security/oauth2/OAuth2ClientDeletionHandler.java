package com.mass3d.security.oauth2;

import com.mass3d.system.deletion.DeletionHandler;
import org.springframework.stereotype.Component;

@Component
public class OAuth2ClientDeletionHandler extends DeletionHandler
{
    @Override
    protected String getClassName()
    {
        return OAuth2Client.class.getName();
    }
}
