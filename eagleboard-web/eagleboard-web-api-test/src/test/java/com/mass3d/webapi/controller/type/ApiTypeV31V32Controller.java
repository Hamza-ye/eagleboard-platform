package com.mass3d.webapi.controller.type;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.mass3d.common.DhisApiVersion;
import com.mass3d.webapi.mvc.annotation.ApiVersion;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping( "/type/testV31V32" )
@ApiVersion( { DhisApiVersion.V31, DhisApiVersion.V32 } )
public class ApiTypeV31V32Controller
{
    @RequestMapping
    public void test( HttpServletResponse response ) throws IOException
    {
        response.getWriter().println( "TEST" );
    }
}
