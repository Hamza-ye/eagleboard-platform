package com.mass3d.webapi.controller.type;

import com.mass3d.common.DhisApiVersion;
import com.mass3d.webapi.mvc.annotation.ApiVersion;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping( "/type/testAll" )
@ApiVersion( DhisApiVersion.ALL )
public class ApiTypeAllController
{
    @RequestMapping
    public void test( HttpServletResponse response ) throws IOException
    {
        response.getWriter().println( "TEST" );
    }
}
