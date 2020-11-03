package com.mass3d.trackedentity.hibernate;

import java.util.List;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import com.mass3d.hibernate.HibernateGenericStore;
import com.mass3d.trackedentity.TrackedEntityProgramOwner;
import com.mass3d.trackedentity.TrackedEntityProgramOwnerStore;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository( "com.mass3d.trackedentity.TrackedEntityProgramOwnerStore" )
public class HibernateTrackedEntityProgramOwnerStore
    extends HibernateGenericStore<TrackedEntityProgramOwner>
        implements TrackedEntityProgramOwnerStore
{
    public HibernateTrackedEntityProgramOwnerStore( SessionFactory sessionFactory, JdbcTemplate jdbcTemplate, ApplicationEventPublisher publisher )
    {
        super( sessionFactory, jdbcTemplate, publisher, TrackedEntityProgramOwner.class, false );
    }

    @Override
    public TrackedEntityProgramOwner getTrackedEntityProgramOwner( long teiId, long programId )
    {
        return getQuery( "from TrackedEntityProgramOwner tepo where tepo.entityInstance.id="
            + teiId + " and tepo.program.id=" + programId ).uniqueResult();
    }

    @Override
    public List<TrackedEntityProgramOwner> getTrackedEntityProgramOwners( List<Long> teiIds )
    {
        String hql = "from TrackedEntityProgramOwner tepo where tepo.entityInstance.id in (:teiIds)";
        Query<TrackedEntityProgramOwner> q = getQuery( hql );
        q.setParameterList( "teiIds", teiIds );
        return q.list();
    }

    @Override
    public List<TrackedEntityProgramOwner> getTrackedEntityProgramOwners( List<Long> teiIds, long programId )
    {
        String hql = "from TrackedEntityProgramOwner tepo where tepo.entityInstance.id in (:teiIds) and tepo.program.id=(:programId) ";
        Query<TrackedEntityProgramOwner> q = getQuery( hql );
        q.setParameterList( "teiIds", teiIds );
        q.setParameter( "programId", programId );
        return q.list();
    }

}
