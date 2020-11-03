package com.mass3d.webapi.controller;

import com.mass3d.schema.descriptors.OAuth2ClientSchemaDescriptor;
import com.mass3d.security.oauth2.OAuth2Client;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping( value = OAuth2ClientSchemaDescriptor.API_ENDPOINT )
public class OAuth2ClientController
    extends AbstractCrudController<OAuth2Client>
{
}
