package com.mass3d.sms.hibernate;

import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import org.hibernate.SessionFactory;
import com.mass3d.common.hibernate.HibernateIdentifiableObjectStore;
import com.mass3d.hibernate.JpaQueryParameters;
import com.mass3d.query.JpaQueryUtils;
import com.mass3d.security.acl.AclService;
import com.mass3d.sms.incoming.IncomingSms;
import com.mass3d.sms.incoming.IncomingSmsStore;
import com.mass3d.sms.incoming.SmsMessageStatus;
import com.mass3d.user.CurrentUserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository( "com.mass3d.sms.hibernate.IncomingSmsStore" )
public class HibernateIncomingSmsStore extends HibernateIdentifiableObjectStore<IncomingSms>
    implements IncomingSmsStore
{
    public HibernateIncomingSmsStore( SessionFactory sessionFactory, JdbcTemplate jdbcTemplate,
         ApplicationEventPublisher publisher, CurrentUserService currentUserService, AclService aclService )
    {
        super( sessionFactory, jdbcTemplate, publisher, IncomingSms.class, currentUserService, aclService, true );
    }

    // -------------------------------------------------------------------------
    // Implementation
    // -------------------------------------------------------------------------

    @Override
    public List<IncomingSms> getSmsByStatus( SmsMessageStatus status, String originator )
    {
        CriteriaBuilder builder = getCriteriaBuilder();

        JpaQueryParameters<IncomingSms> parameter = newJpaParameters()
        .addOrder( root -> builder.desc( root.get( "sentDate" ) ) );

        if ( status != null )
        {
            parameter.addPredicate( root -> builder.equal( root.get( "status" ), status ) );
        }

        if ( originator != null && !originator.isEmpty() )
        {
            parameter.addPredicate( root -> JpaQueryUtils.stringPredicateIgnoreCase( builder, root.get( "originator" ), originator, JpaQueryUtils.StringSearchMode.ANYWHERE ) );
        }

        return getList( builder, parameter );
    }

    @Override
    public List<IncomingSms> getAll( Integer min, Integer max, boolean hasPagination )
    {
        CriteriaBuilder builder = getCriteriaBuilder();

        JpaQueryParameters<IncomingSms> parameters = new JpaQueryParameters<IncomingSms>();

        if ( hasPagination )
        {
            parameters.setFirstResult( min ).setMaxResults( max );
        }

        return getList( builder, parameters );
    }

    @Override
    public List<IncomingSms> getSmsByOriginator( String originator )
    {
        CriteriaBuilder builder = getCriteriaBuilder();

        return getList( builder, newJpaParameters()
            .addPredicate( root -> builder.equal( root.get( "originator" ), originator ) ) );
    }

    @Override
    public List<IncomingSms> getAllUnparsedMessages()
    {
        CriteriaBuilder builder = getCriteriaBuilder();

        return getList( builder, newJpaParameters()
            .addPredicate( root -> builder.equal( root.get( "parsed" ), false ) ) );
    }

    @Override
    public List<IncomingSms> getSmsByStatus( SmsMessageStatus status, String keyword, Integer min, Integer max, boolean hasPagination )
    {
        CriteriaBuilder builder = getCriteriaBuilder();

        JpaQueryParameters<IncomingSms> parameters = newJpaParameters();


        if ( status != null )
        {
            parameters.addPredicate( root -> builder.equal( root.get( "status" ), status ) );
        }

        if ( keyword != null )
        {
            parameters.addPredicate( root -> JpaQueryUtils.stringPredicateIgnoreCase( builder, root.get( "originator" ), keyword, JpaQueryUtils.StringSearchMode.ANYWHERE ) );
        }

        if ( hasPagination )
        {
            parameters.setFirstResult( min ).setMaxResults( max );
        }

        return getList( builder, parameters );
    }
}
