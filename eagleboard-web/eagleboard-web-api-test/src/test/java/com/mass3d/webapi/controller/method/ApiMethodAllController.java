package com.mass3d.webapi.controller.method;

import com.mass3d.common.DhisApiVersion;
import com.mass3d.webapi.mvc.annotation.ApiVersion;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping( "/method/testAll" )
public class ApiMethodAllController
{
    @RequestMapping( "a" )
    @ApiVersion( DhisApiVersion.ALL )
    public void testAllA( HttpServletResponse response ) throws IOException
    {
        response.getWriter().println( "TEST" );
    }

    @RequestMapping( "b" )
    @ApiVersion( DhisApiVersion.ALL )
    public void testAllB( HttpServletResponse response ) throws IOException
    {
        response.getWriter().println( "TEST" );
    }
}
