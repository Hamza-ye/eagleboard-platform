package com.mass3d.user.comparator;

import java.util.Comparator;
import com.mass3d.user.UserCredentials;

/**
 * @version $Id: UsernameComparator.java 2869 2007-02-20 14:26:09Z andegje $
 */
public class UsernameComparator
    implements Comparator<UserCredentials>
{
    @Override
    public int compare( UserCredentials uc0, UserCredentials uc1 )
    {
        if ( uc0 == null )
        {
            return 1;
        }
        
        if ( uc1 == null )
        {
            return -1;
        }
        
        return uc0.getUsername().compareTo( uc1.getUsername() );
    }
}
