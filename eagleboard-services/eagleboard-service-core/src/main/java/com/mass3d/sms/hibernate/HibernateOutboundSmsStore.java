package com.mass3d.sms.hibernate;

import java.util.Date;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import org.hibernate.SessionFactory;
import com.mass3d.common.hibernate.HibernateIdentifiableObjectStore;
import com.mass3d.hibernate.JpaQueryParameters;
import com.mass3d.security.acl.AclService;
import com.mass3d.sms.outbound.OutboundSms;
import com.mass3d.sms.outbound.OutboundSmsStatus;
import com.mass3d.sms.outbound.OutboundSmsStore;
import com.mass3d.user.CurrentUserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository( "com.mass3d.sms.hibernate.OutboundSmsStore" )
public class HibernateOutboundSmsStore
    extends HibernateIdentifiableObjectStore<OutboundSms>
    implements OutboundSmsStore
{
    public HibernateOutboundSmsStore( SessionFactory sessionFactory, JdbcTemplate jdbcTemplate,
        ApplicationEventPublisher publisher, CurrentUserService currentUserService, AclService aclService )
    {
        super( sessionFactory, jdbcTemplate, publisher, OutboundSms.class, currentUserService, aclService, true );
    }

    // -------------------------------------------------------------------------
    // Implementation
    // -------------------------------------------------------------------------

    @Override
    public void saveOutboundSms( OutboundSms sms )
    {
        checkDate( sms );
        save( sms );
    }

    private void checkDate( OutboundSms sms )
    {
        if ( sms.getDate() == null )
        {
            sms.setDate( new Date() );
        }
    }

    @Override
    public List<OutboundSms> get( OutboundSmsStatus status )
    {
        CriteriaBuilder builder = getCriteriaBuilder();

        JpaQueryParameters<OutboundSms> parameters = new JpaQueryParameters<OutboundSms>()
            .addOrder( root -> builder.desc( root.get( "date" ) ) );

        if ( status != null )
        {
            parameters.addPredicate( root -> builder.equal( root.get( "status" ), status ) );
        }

        return getList( builder, parameters );
    }

    @Override
    public List<OutboundSms> get( OutboundSmsStatus status, Integer min, Integer max, boolean hasPagination )
    {
        CriteriaBuilder builder = getCriteriaBuilder();

        JpaQueryParameters<OutboundSms> parameters = new JpaQueryParameters<OutboundSms>()
            .addOrder( root -> builder.desc( root.get( "date" ) ) );

        if ( status != null )
        {
            parameters.addPredicate( root -> builder.equal( root.get( "status" ), status ) );
        }

        if ( hasPagination )
        {
            parameters.setFirstResult( min ).setMaxResults( max );
        }

        return getList( builder, parameters );
    }

    @Override
    public List<OutboundSms> getAllOutboundSms( Integer min, Integer max, boolean hasPagination )
    {
        CriteriaBuilder builder = getCriteriaBuilder();

        JpaQueryParameters<OutboundSms> parameters = new JpaQueryParameters<OutboundSms>()
            .addOrder( root -> builder.desc( root.get( "date" ) ) );

        if ( hasPagination )
        {
            parameters.setFirstResult( min ).setMaxResults( max );
        }

        return getList( builder, parameters );
    }
}
