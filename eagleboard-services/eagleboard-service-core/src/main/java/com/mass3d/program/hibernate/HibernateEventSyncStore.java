package com.mass3d.program.hibernate;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import com.mass3d.program.EventSyncStore;
import com.mass3d.program.ProgramInstance;
import com.mass3d.program.ProgramStageInstance;
import org.springframework.stereotype.Repository;

@Repository( "com.mass3d.program.EventSyncStore" )
public class HibernateEventSyncStore implements EventSyncStore
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private final SessionFactory sessionFactory;

    public HibernateEventSyncStore( SessionFactory sessionFactory )
    {
        checkNotNull( sessionFactory );

        this.sessionFactory = sessionFactory;
    }

    // -------------------------------------------------------------------------
    // Implementation methods
    // -------------------------------------------------------------------------

    @Override
    @SuppressWarnings( "unchecked" )
    public List<ProgramStageInstance> getEvents( List<String> uids )
    {
        if ( uids.isEmpty() )
        {
            return new ArrayList<>();
        }

        Criteria criteria = sessionFactory.getCurrentSession().createCriteria( ProgramStageInstance.class );

        criteria.add( Restrictions.in( "uid", uids ) );

        return criteria.list();
    }

    @Override
    public ProgramStageInstance getEvent( String uid )
    {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria( ProgramStageInstance.class );
        criteria.add( Restrictions.eq( "uid", uid ) );

        return (ProgramStageInstance) criteria.uniqueResult();
    }

    @Override
    public ProgramInstance getEnrollment( String uid )
    {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria( ProgramInstance.class );
        criteria.add( Restrictions.eq( "uid", uid ) );

        return (ProgramInstance) criteria.uniqueResult();
    }
}