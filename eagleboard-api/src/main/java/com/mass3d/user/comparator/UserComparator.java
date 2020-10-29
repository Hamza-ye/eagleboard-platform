package com.mass3d.user.comparator;

import java.util.Comparator;
import com.mass3d.user.User;

public class UserComparator
    implements Comparator<User>
{
    public static final UserComparator INSTANCE = new UserComparator(); 
    
    @Override
    public int compare( User u0, User u1 )
    {
        if ( u0 == null )
        {
            return 1;
        }

        if ( u1 == null )
        {
            return -1;
        }

        int compare = u0.getSurname().compareTo( u1.getSurname() );
        
        if ( compare != 0 )
        {
            return compare;
        }
        
        return u0.getFirstName().compareTo( u1.getFirstName() );
    }
}
