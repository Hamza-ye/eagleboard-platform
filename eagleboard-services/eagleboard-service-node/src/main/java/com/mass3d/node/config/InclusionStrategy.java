package com.mass3d.node.config;

import java.util.Collection;
import org.springframework.util.StringUtils;

public interface InclusionStrategy
{
    <T> boolean include(T object);

    enum Include implements InclusionStrategy
    {
        /**
         * Inclusion strategy that includes all objects.
         */
        ALWAYS,

        /**
         * Inclusion strategy that only includes non null objects.
         */
        NON_NULL
            {
                @Override
                public <T> boolean include( T object )
                {
                    return object != null;
                }
            },

        /**
         * Inclusion strategy that only includes non empty objects:
         * -
         */
        NON_EMPTY
            {
                @Override
                public <T> boolean include( T object )
                {
                    if ( object == null )
                    {
                        return false;
                    }

                    if ( Collection.class.isAssignableFrom( object.getClass() ) )
                    {
                        return !((Collection<?>) object).isEmpty();
                    }
                    else if ( String.class.isAssignableFrom( object.getClass() ) )
                    {
                        return !StringUtils.isEmpty( object );
                    }

                    return true;
                }
            };

        @Override
        public <T> boolean include( T object )
        {
            return true;
        }
    }
}
