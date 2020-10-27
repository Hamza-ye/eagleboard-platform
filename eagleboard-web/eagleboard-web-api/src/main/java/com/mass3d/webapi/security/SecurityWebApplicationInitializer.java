package com.mass3d.webapi.security;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

public class SecurityWebApplicationInitializer extends AbstractSecurityWebApplicationInitializer
{
    // This is needed for triggering the setup of the SpringSecurityFilterChain and ContextLoaderListener
}
