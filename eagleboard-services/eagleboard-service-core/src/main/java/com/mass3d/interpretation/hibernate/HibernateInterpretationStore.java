package com.mass3d.interpretation.hibernate;

import java.util.List;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import com.mass3d.common.hibernate.HibernateIdentifiableObjectStore;
import com.mass3d.interpretation.Interpretation;
import com.mass3d.interpretation.InterpretationStore;
import com.mass3d.security.acl.AclService;
import com.mass3d.user.CurrentUserService;
import com.mass3d.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository( "com.mass3d.interpretation.InterpretationStore" )
public class HibernateInterpretationStore
    extends HibernateIdentifiableObjectStore<Interpretation> implements InterpretationStore
{
    @Autowired
    public HibernateInterpretationStore( SessionFactory sessionFactory, JdbcTemplate jdbcTemplate,
        ApplicationEventPublisher publisher, CurrentUserService currentUserService, AclService aclService )
    {
        super( sessionFactory, jdbcTemplate, publisher, Interpretation.class, currentUserService, aclService, false );
    }

    public List<Interpretation> getInterpretations( User user )
    {
        String hql = "select distinct i from Interpretation i left join i.comments c " +
            "where i.user = :user or c.user = :user order by i.lastUpdated desc";

        Query<Interpretation> query = getQuery( hql )
            .setParameter( "user", user )
            .setCacheable( cacheable );

        return query.list();
    }

    public List<Interpretation> getInterpretations( User user, int first, int max )
    {
        String hql = "select distinct i from Interpretation i left join i.comments c " +
            "where i.user = :user or c.user = :user order by i.lastUpdated desc";

        Query<Interpretation> query = getQuery( hql )
            .setParameter( "user", user )
            .setMaxResults( first )
            .setMaxResults( max )
            .setCacheable( cacheable );

        return query.list();
    }

//    @Override
//    public long countMapInterpretations( Map map )
//    {
//        Query<Long> query = getTypedQuery( "select count(distinct c) from " + clazz.getName() + " c where c.map=:map" );
//        query.setParameter( "map", map );
//        return query.uniqueResult();
//    }

//    @Override
//    public long countVisualizationInterpretations( Visualization visualization )
//    {
//        Query query = getQuery( "select count(distinct c) from " + clazz.getName() + " c where c.visualization=:visualization" )
//            .setParameter( "visualization", visualization )
//            .setCacheable( cacheable );
//
//        return ((Long) query.uniqueResult()).intValue();
//    }

//    @Override
//    public Interpretation getByVisualizationId( long id )
//    {
//        String hql = "from Interpretation i where i.visualization.id = " + id;
//        Query<Interpretation> query = getQuery( hql );
//        return query.uniqueResult();
//    }
}
