package com.mass3d.dbms;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import com.mass3d.commons.util.DebugUtils;
import org.springframework.orm.hibernate5.SessionFactoryUtils;
import org.springframework.orm.hibernate5.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * @version $Id$
 */
public class DbmsUtils
{
    public static void bindSessionToThread( SessionFactory sessionFactory )
    {
        Session session = sessionFactory.openSession();

        TransactionSynchronizationManager.bindResource( sessionFactory, new SessionHolder( session ) );
    }

    public static void unbindSessionFromThread( SessionFactory sessionFactory )
    {
        SessionHolder sessionHolder = (SessionHolder) TransactionSynchronizationManager.unbindResource( sessionFactory );

        SessionFactoryUtils.closeSession( sessionHolder.getSession() );
    }

    public static void closeStatelessSession( StatelessSession session )
    {
        try
        {
            session.getTransaction().commit();
        }
        catch ( Exception exception )
        {
            session.getTransaction().rollback();
            DebugUtils.getStackTrace( exception );
        }
        finally
        {
            session.close();
        }
    }
}
