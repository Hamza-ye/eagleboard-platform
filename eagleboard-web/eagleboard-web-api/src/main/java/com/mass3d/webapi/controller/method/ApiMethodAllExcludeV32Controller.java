package com.mass3d.webapi.controller.method;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.mass3d.common.DhisApiVersion;
import com.mass3d.webapi.mvc.annotation.ApiVersion;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping( "/method/testAllExcludeV32" )
public class ApiMethodAllExcludeV32Controller
{
    @RequestMapping( "a" )
    @ApiVersion( value = DhisApiVersion.ALL, exclude = DhisApiVersion.V32 )
    public void testAllA( HttpServletResponse response ) throws IOException
    {
        response.getWriter().println( "TEST" );
    }

    @RequestMapping( "b" )
    @ApiVersion( value = DhisApiVersion.ALL, exclude = DhisApiVersion.V32 )
    public void testAllB( HttpServletResponse response ) throws IOException
    {
        response.getWriter().println( "TEST" );
    }
}
