package com.mass3d.webapi.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mass3d.webapi.DhisWebSpringTest;
import org.junit.Test;
import org.springframework.mock.web.MockHttpSession;

public class ApiVersionInheritTypeTest extends DhisWebSpringTest
{
    @Test
    public void testGetInherited() throws Exception
    {
        MockHttpSession session = getSession( "ALL" );
        String endpoint = "/type/testInheritedFromBase";

        mvc.perform( get( endpoint ).session( session ) )
            .andExpect( status().isNotFound() );

        mvc.perform( post( endpoint + "/abc" ).session( session ) )
            .andExpect( status().isNotFound() );

        mvc.perform( get( "/31" + endpoint ).session( session ) )
            .andExpect( status().isNotFound() );

        mvc.perform( post( "/31" + endpoint + "/abc" ).session( session ) )
            .andExpect( status().isNotFound() );

        mvc.perform( get( "/32" + endpoint ).session( session ) )
            .andExpect( status().isOk() );

        mvc.perform( get( "/32" + endpoint + "/abc" ).session( session ) )
            .andExpect( status().isMethodNotAllowed() );

        mvc.perform( put( "/32" + endpoint + "/abc" ).session( session ) )
            .andExpect( status().isMethodNotAllowed() );

        mvc.perform( delete( "/32" + endpoint + "/abc" ).session( session ) )
            .andExpect( status().isMethodNotAllowed() );

        mvc.perform( post( "/32" + endpoint + "/abc" ).session( session ) )
            .andExpect( status().isOk() );
    }
}
