package com.mass3d.webapi.controller.type;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.mass3d.common.DhisApiVersion;
import com.mass3d.webapi.mvc.annotation.ApiVersion;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping( "/type/testAllExcludeV32" )
@ApiVersion( value = DhisApiVersion.ALL, exclude = DhisApiVersion.V32 )
public class ApiTypeAllExcludeV32Controller
{
    @RequestMapping
    public void test( HttpServletResponse response ) throws IOException
    {
        response.getWriter().println( "TEST" );
    }
}
