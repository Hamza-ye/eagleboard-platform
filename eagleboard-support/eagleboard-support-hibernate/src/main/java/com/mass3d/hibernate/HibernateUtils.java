package com.mass3d.hibernate;

import com.google.common.base.Preconditions;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import org.hibernate.Hibernate;
import org.hibernate.collection.internal.PersistentSet;
import org.hibernate.collection.spi.PersistentCollection;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.pojo.javassist.SerializableProxy;
import com.mass3d.commons.util.DebugUtils;

public class HibernateUtils
{
    public static boolean isProxy( Object object )
    {
        return ( ( object instanceof HibernateProxy ) || ( object instanceof PersistentCollection ) );
    }

    /**
     * If object is proxy, get unwrapped non-proxy object.
     *
     * @param proxy Object to check and unwrap
     * @return Unwrapped object if proxyied, if not just returns same object
     */
    @SuppressWarnings( "unchecked" )
    public static <T> T unwrap( T proxy )
    {
        if ( !isProxy( proxy ) )
        {
            return proxy;
        }

        Hibernate.initialize( proxy );

        if ( HibernateProxy.class.isInstance( proxy ) )
        {
            Object result = ((HibernateProxy) proxy).writeReplace();

            if ( !SerializableProxy.class.isInstance( result ) )
            {
                return (T) result;
            }
        }

        if ( PersistentCollection.class.isInstance( proxy ) )
        {
            PersistentCollection persistentCollection = (PersistentCollection) proxy;

            if ( PersistentSet.class.isInstance( persistentCollection ) )
            {
                Map<?, ?> map = (Map<?, ?>) persistentCollection.getStoredSnapshot();
                return (T) new LinkedHashSet<>( map.keySet() );
            }

            return (T) persistentCollection.getStoredSnapshot();
        }

        return proxy;
    }

    /**
     * Eager fetch all its collections
     *
     * @param proxy Object to check and unwrap
     * @return fully initialized object
     */
    public static <T> T initializeProxy( T proxy )
    {
        Preconditions.checkNotNull( proxy, "Proxy can not be null!" );

        if ( !Hibernate.isInitialized( proxy ) )
        {
            Hibernate.initialize( proxy );
        }

        Field[] fields = proxy.getClass().getDeclaredFields();

        Arrays.stream( fields )
            .filter( f -> Collection.class.isAssignableFrom( f.getType() ) )
            .forEach( f ->
            {
                try
                {
                    PropertyDescriptor pd = new PropertyDescriptor( f.getName(), proxy.getClass() );

                    Object persistentObject = pd.getReadMethod().invoke( proxy );

                    if ( PersistentCollection.class.isAssignableFrom( persistentObject.getClass() ) )
                    {
                        Hibernate.initialize( persistentObject );
                    }
                }
                catch ( IllegalAccessException | IntrospectionException | InvocationTargetException e )
                {
                    DebugUtils.getStackTrace( e );
                }
            });

        return proxy;
    }
}
