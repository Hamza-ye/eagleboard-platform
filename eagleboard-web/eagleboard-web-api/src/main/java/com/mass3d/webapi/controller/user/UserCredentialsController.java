package com.mass3d.webapi.controller.user;

import com.mass3d.schema.descriptors.UserCredentialsSchemaDescriptor;
import com.mass3d.user.UserCredentials;
import com.mass3d.webapi.controller.AbstractCrudController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.HttpServerErrorException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping( value = UserCredentialsSchemaDescriptor.API_ENDPOINT )
public class UserCredentialsController
    extends AbstractCrudController<UserCredentials>
{
    @Override
    public void postXmlObject( HttpServletRequest request, HttpServletResponse response ) throws Exception
    {
        throw new HttpServerErrorException( HttpStatus.BAD_REQUEST );
    }

    @Override
    public void postJsonObject( HttpServletRequest request, HttpServletResponse response ) throws Exception
    {
        throw new HttpServerErrorException( HttpStatus.BAD_REQUEST );
    }
}
