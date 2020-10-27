package com.mass3d.webapi.controller.type;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.mass3d.common.DhisApiVersion;
import com.mass3d.webapi.mvc.annotation.ApiVersion;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping( "/type/testDefaultV31" )
@ApiVersion( { DhisApiVersion.DEFAULT, DhisApiVersion.V31 } )
public class ApiTypeDefaultV31Controller
{
    @RequestMapping
    public void test( HttpServletResponse response ) throws IOException
    {
        response.getWriter().println( "TEST" );
    }
}
