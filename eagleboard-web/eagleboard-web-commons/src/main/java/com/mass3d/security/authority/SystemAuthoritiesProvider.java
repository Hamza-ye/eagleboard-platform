package com.mass3d.security.authority;

import java.util.Collection;

/**
 * @version $Id: SystemAuthoritiesProvider.java 3160 2007-03-24 20:15:06Z torgeilo $
 */
public interface SystemAuthoritiesProvider
{
    String ID = SystemAuthoritiesProvider.class.getName();

    Collection<String> getSystemAuthorities();
}
