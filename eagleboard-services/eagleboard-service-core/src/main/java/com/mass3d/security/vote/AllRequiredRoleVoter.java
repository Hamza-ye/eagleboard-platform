package com.mass3d.security.vote;

import java.util.Collection;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

/**
 * RoleVoter which requires all org.springframework.security.ConfigAttributes to
 * be granted authorities, given that the ConfigAttributes have the specified
 * prefix ("ROLE_" by default). If there are no supported ConfigAttributes it
 * abstains from voting.
 * 
 * @see RoleVoter
 * 
 * @version $Id: AllRequiredRoleVoter.java 6070 2008-10-28 17:49:23Z larshelg $
 */
public class AllRequiredRoleVoter
    extends RoleVoter
{
    @Override
    public int vote( Authentication authentication, Object object, Collection<ConfigAttribute> attributes )
    {
        int supported = 0;

        for ( ConfigAttribute attribute : attributes )
        {
            if ( this.supports( attribute ) )
            {
                ++supported;
                boolean found = false;

                for ( GrantedAuthority authority : authentication.getAuthorities() )
                {
                    if ( attribute.getAttribute().equals( authority.getAuthority() ) )
                    {
                        found = true;
                        break;
                    }
                }

                if ( !found )
                {
                    return ACCESS_DENIED;
                }
            }
        }

        if ( supported > 0 )
        {
            return ACCESS_GRANTED;
        }

        return ACCESS_ABSTAIN;
    }
}
