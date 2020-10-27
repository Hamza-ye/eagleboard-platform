package com.mass3d.webapi.controller.type;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.mass3d.common.DhisApiVersion;
import com.mass3d.webapi.mvc.annotation.ApiVersion;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@ApiVersion( DhisApiVersion.V31 )
public abstract class BaseWithVersionController
{
    @RequestMapping( value = "/{id}", method = RequestMethod.POST )
    public void testWithId( @PathVariable String id, HttpServletResponse response ) throws IOException
    {
        response.getWriter().println( id );
    }
}
