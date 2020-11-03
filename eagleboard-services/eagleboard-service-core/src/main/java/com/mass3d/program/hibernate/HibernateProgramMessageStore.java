package com.mass3d.program.hibernate;

import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import com.mass3d.common.hibernate.HibernateIdentifiableObjectStore;
import com.mass3d.commons.util.SqlHelper;
import com.mass3d.hibernate.JpaQueryParameters;
import com.mass3d.program.message.ProgramMessage;
import com.mass3d.program.message.ProgramMessageQueryParams;
import com.mass3d.program.message.ProgramMessageStore;
import com.mass3d.security.acl.AclService;
import com.mass3d.user.CurrentUserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository( "com.mass3d.program.ProgramMessageStore" )
public class HibernateProgramMessageStore
    extends HibernateIdentifiableObjectStore<ProgramMessage>
    implements ProgramMessageStore
{
    private static final String TABLE_NAME = "ProgramMessage";

    public HibernateProgramMessageStore( SessionFactory sessionFactory, JdbcTemplate jdbcTemplate,
        ApplicationEventPublisher publisher, CurrentUserService currentUserService, AclService aclService )
    {
        super( sessionFactory, jdbcTemplate, publisher, ProgramMessage.class, currentUserService, aclService, true );
    }

    // -------------------------------------------------------------------------
    // Implementation
    // -------------------------------------------------------------------------

    @Override
    public List<ProgramMessage> getProgramMessages( ProgramMessageQueryParams params )
    {
        Query<ProgramMessage> query = getHqlQuery( params );

        if ( params.hasPaging() )
        {
            query.setFirstResult( params.getPage() );
            query.setMaxResults( params.getPageSize() );
        }

        return query.list();
    }

    @Override
    public List<ProgramMessage> getAllOutboundMessages()
    {
        CriteriaBuilder builder = getCriteriaBuilder();

        JpaQueryParameters<ProgramMessage> parameters = newJpaParameters()
            .addPredicate( root -> builder.and(
                    builder.equal( root.get( "messageStatus" ), "OUTBOUND" ),
                    builder.equal( root.get( "messageCatagory" ), "OUTGOING" ) ) );

        return getList( builder, parameters );
    }

    @Override
    public boolean exists( String uid )
    {
        ProgramMessage programMessage = getByUid( uid );

        return programMessage != null && programMessage.getId() > 0;
    }

    // -------------------------------------------------------------------------
    // Supportive Methods
    // -------------------------------------------------------------------------

    private Query<ProgramMessage> getHqlQuery( ProgramMessageQueryParams params )
    {
        SqlHelper helper = new SqlHelper( true );

        String hql = " select distinct pm from " + TABLE_NAME + " pm ";

        if ( params.hasProgramInstance() )
        {
            hql += helper.whereAnd() + "pm.programInstance = :programInstance";
        }

        if ( params.hasProgramStageInstance() )
        {
            hql += helper.whereAnd() + "pm.programStageInstance = :programStageInstance";
        }

        hql += params.getMessageStatus() != null
            ? helper.whereAnd() + "pm.messageStatus = :messageStatus" : "";

        hql += params.getAfterDate() != null ? helper.whereAnd() + "pm.processeddate > :processeddate" : "" ;

        hql += params.getBeforeDate() != null
            ? helper.whereAnd() + "pm.processeddate < :processeddate" : "";

        Query<ProgramMessage> query = getQuery( hql );

        if ( params.hasProgramInstance() )
        {
            query.setParameter( "programInstance", params.getProgramInstance() );
        }

        if ( params.hasProgramStageInstance() )
        {
            query.setParameter( "programStageInstance", params.getProgramStageInstance() );
        }

        if ( params.getMessageStatus() != null)
        {
            query.setParameter( "messageStatus", params.getMessageStatus() );
        }

        if ( params.getAfterDate() != null )
        {
            query.setParameter( "processeddate", params.getAfterDate() );
        }

        if ( params.getBeforeDate() != null )
        {
            query.setParameter( "processeddate", params.getBeforeDate() );
        }

        return query;
    }
}
